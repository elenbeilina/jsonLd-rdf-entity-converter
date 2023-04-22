package com.aqualen.springjsonldrdfdtoconverter.service;

import com.aqualen.springjsonldrdfdtoconverter.config.EntityConfig;
import com.aqualen.springjsonldrdfdtoconverter.dto.Entity;
import com.aqualen.springjsonldrdfdtoconverter.dto.Context;
import com.aqualen.springjsonldrdfdtoconverter.service.processors.EntityMapper;
import com.aqualen.springjsonldrdfdtoconverter.service.processors.JsonLdProcessor;
import com.aqualen.springjsonldrdfdtoconverter.service.processors.SparqlProcessor;
import com.aqualen.springjsonldrdfdtoconverter.utils.JsonLdUtils;
import jakarta.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aqualen.springjsonldrdfdtoconverter.constants.LogConstants.*;
import static com.aqualen.springjsonldrdfdtoconverter.dto.Context.buildContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonLdCollibraConverter {

    private static final String EMPTY_RESULT_EXCEPTION = "Sparql result was empty!";

    private final JsonLdProcessor jsonLdProcessor;
    private final EntityConfig config;
    private SparqlProcessor sparqlProcessor;
    private EntityMapper entityMapper;

    public List<Entity> convertJsonLdToEntity(String jsonLd) {
        long jsonld = System.currentTimeMillis();
        log.info(STARTED_PROCESS_JSONLD);
        JsonObject jsonLdObject = JsonLdUtils.getProcessedJsonLd(jsonLd);
        initializer(jsonLdObject);
        JsonObject processedJsonLd = jsonLdProcessor.getProcessedJsonLd(jsonLdObject);
        log.info(FINISHED_PROCESS_JSONLD, (System.currentTimeMillis() - jsonld));

        long sparql = System.currentTimeMillis();
        log.info(STARTED_SPARQL_PROCESS);
        List<JsonObject> sparqlResult = sparqlProcessor.getSparqlResult(processedJsonLd);
        log.info(FINISHED_SPARQL_PROCESS, (System.currentTimeMillis() - sparql));

        if (sparqlResult.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_RESULT_EXCEPTION);
        }

        log.info(STARTED_MAPPING_PROCESS);
        return entityMapper.mapJsonArrayToCollibraFormat(sparqlResult);
    }

    private void initializer(JsonObject jsonObject) {
        Context context = buildContext(jsonObject);

        sparqlProcessor = new SparqlProcessor(context, config);
        entityMapper = new EntityMapper(context, config);
    }

}
