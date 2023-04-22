package com.aqualen.springjsonldrdfdtoconverter.utils;

import jakarta.json.JsonObject;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Test;

import static com.aqualen.springjsonldrdfdtoconverter.service.processors.JsonLdProcessor.CONTEXT;
import static com.aqualen.springjsonldrdfdtoconverter.utils.FileUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonLdUtilsTest {
    @Test
    void testWithExternalContext() {
        String jsonld = readFile("withoutFrame/test-empty-frame.json");

        JsonObject result = JsonLdUtils.getProcessedJsonLd(jsonld);

        assertDoesNotThrow(() -> result.getJsonObject(CONTEXT));
        assertThrows(ClassCastException.class, () -> result.getString(CONTEXT));
    }
}