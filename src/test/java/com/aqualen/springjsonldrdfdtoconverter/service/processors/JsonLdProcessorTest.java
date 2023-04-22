package com.aqualen.springjsonldrdfdtoconverter.service.processors;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.document.JsonDocument;
import com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtils;
import jakarta.json.JsonObject;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.aqualen.springjsonldrdfdtoconverter.utils.FileUtils.readFile;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtil.compareJsonObjects;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtil.getJson;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
  Test suite files are taken from
  https://w3c.github.io/json-ld-framing/tests/frame-manifest.html#json-ld-object-comparison
 */
class JsonLdProcessorTest {

    private final JsonLdProcessor processor = new JsonLdProcessor();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"withoutFrame/test.json",
            "withoutFrame/test-empty-frame-internal-context.json"})
    void testWithoutFrame(String testFile) {
        JsonObject jsonld = JsonUtils.getJsonObject(readFile(testFile));

        JsonObject result = processor.getProcessedJsonLd(jsonld);

        JsonObject jsonLdObject = getJson(testFile);
        assertTrue(
                compareJsonObjects(
                        result, JsonLd.expand(JsonDocument.of(jsonLdObject)).get().getJsonObject(0)
                ));
    }

    @SneakyThrows
    @ParameterizedTest(name = "testFile={0}, resultFile={1}")
    @CsvSource({"framed/test-from-external-site.json, framed/result-from-external-site.json",
            "framed/test-from-external-site-2.json, framed/result-from-external-site-2.json",
            "framed/test-from-external-site-3.json, framed/result-from-external-site-3.json",
    })
    void testFraming(String testFile, String resultFile) {
        JsonObject jsonld = JsonUtils.getJsonObject(readFile(testFile));

        JsonObject result = processor.getProcessedJsonLd(jsonld);

        JsonObject jsonLdObject = getJson(resultFile);

        assertTrue(
                compareJsonObjects(
                        result, JsonLd.expand(JsonDocument.of(jsonLdObject)).get().getJsonObject(0)
                ));
    }
}