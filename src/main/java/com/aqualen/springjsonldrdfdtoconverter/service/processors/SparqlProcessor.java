package com.aqualen.springjsonldrdfdtoconverter.service.processors;


import com.aqualen.springjsonldrdfdtoconverter.config.EntityConfig;
import com.aqualen.springjsonldrdfdtoconverter.dto.Context;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFLanguages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.aqualen.springjsonldrdfdtoconverter.utils.FileUtils.readFile;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtils.getInputStream;

@Slf4j
@RequiredArgsConstructor
public class SparqlProcessor {

    private final Context context;
    private final EntityConfig config;

    public List<JsonObject> getSparqlResult(JsonObject jsonObject) {
        Model model = createModelForJsonLd(jsonObject);

        return getQueryResult(model);
    }

    private Model createModelForJsonLd(JsonObject jsonObject) {
        Model model = ModelFactory.createDefaultModel();

        model.read(getInputStream(jsonObject), StringUtils.EMPTY, RDFLanguages.strLangJSONLD);

        return model;
    }

    @SuppressWarnings("squid:S2095")
    private List<JsonObject> getQueryResult(Model model) {
        Query query = generateDynamicQuery();

        QueryExecution executor = QueryExecutionFactory.create(query, model);
        ResultSet resultSet = executor.execSelect();

        return convertToJsonMultiThread(resultSet);
    }

    @SneakyThrows
    private List<JsonObject> convertToJsonMultiThread(ResultSet resultSet) {
        List<JsonObject> jsonList = Collections.synchronizedList(new ArrayList<>());
        int threadsCount = Runtime.getRuntime().availableProcessors();
        long rdf = System.currentTimeMillis();
        log.info("Number of processors: {}", threadsCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        CountDownLatch latch = new CountDownLatch(threadsCount);

        for (int i = 0; i < threadsCount; i++) {
            executorService.execute(() -> {
                try {
                    while (hasNextThreadSafe(resultSet)) {
                        JsonObjectBuilder json = Json.createObjectBuilder();
                        QuerySolution next = nextThreadSafe(resultSet);
                        next.varNames()
                                .forEachRemaining(nodeName -> convertNodeToJson(
                                        json, next, nodeName
                                ));
                        jsonList.add(json.build());
                    }
                } catch (NoSuchElementException ex) {
                    log.info("Thread: {} is done. Exception: {}",
                            Thread.currentThread().getName(), ex.getMessage());
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        log.info("Selecting from rdf triple store and Converting from rdf to Json took: "
                + (System.currentTimeMillis() - rdf) + " ms");
        return jsonList;
    }

    private synchronized boolean hasNextThreadSafe(ResultSet resultSet) {
        return resultSet.hasNext();
    }

    private synchronized QuerySolution nextThreadSafe(ResultSet resultSet) {
        return resultSet.next();
    }

    private void convertNodeToJson(JsonObjectBuilder json,
                                   QuerySolution querySolution,
                                   String nodeName) {
        Node node = querySolution.get(nodeName).asNode();
        if (node.isURI()) {
            json.add(nodeName, node.getURI());
        }

        if (node.isLiteral()) {
            json.add(nodeName, node.getLiteralLexicalForm());
        }
    }

    private Query generateDynamicQuery() {
        String queryTemplate = readFile("query.sql");
        String query = String.format(
                queryTemplate, context.getCol(), context.getCol() + getDomain()
        );

        return QueryFactory.create(query);
    }

    private String getDomain() {
        return StringUtils.isNoneEmpty(context.getDomain()) ?
                context.getDomain() :
                config.getDomain();
    }
}
