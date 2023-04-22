package com.aqualen.springjsonldrdfdtoconverter.service.processors;


import com.aqualen.springjsonldrdfdtoconverter.config.EntityConfig;
import com.aqualen.springjsonldrdfdtoconverter.dto.Context;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.aqualen.springjsonldrdfdtoconverter.utils.ContextUtil.emptyContext;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtil.getJson;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SparqlProcessorTest {

    @Spy
    private final EntityConfig config =
            new EntityConfig("2dc40841-02fe-45cc-9223-304935f75fd2","ad0c1d99-8d57-4665-ac9f-555aff96a8d9");
    @InjectMocks
    private SparqlProcessor sparqlProcessor;
    private JsonObject jsonLdObject;

    @BeforeEach
    void setUp() {
        jsonLdObject = getJson("withoutFrame/test.json");
        sparqlProcessor = new SparqlProcessor(Context.buildContext(jsonLdObject), config);
    }

    @Test
    void getProcessedJsonLd() {
        List<JsonObject> result = sparqlProcessor.getSparqlResult(jsonLdObject);

        assertThat(result).hasSize(10);
    }

    @Test
    void getProcessedJsonLdNegative() {
        sparqlProcessor = new SparqlProcessor(Context.buildContext(emptyContext), config);
        List<JsonObject> result = sparqlProcessor.getSparqlResult(jsonLdObject);

        assertThat(result).isEmpty();
    }
}