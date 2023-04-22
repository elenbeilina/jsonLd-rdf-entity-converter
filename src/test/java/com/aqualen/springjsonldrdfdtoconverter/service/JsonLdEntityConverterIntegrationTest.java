package com.aqualen.springjsonldrdfdtoconverter.service;

import com.aqualen.springjsonldrdfdtoconverter.config.EntityConfig;
import com.aqualen.springjsonldrdfdtoconverter.dto.Entity;
import com.aqualen.springjsonldrdfdtoconverter.service.processors.JsonLdProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.aqualen.springjsonldrdfdtoconverter.utils.FileUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonLdEntityConverterIntegrationTest {

    @Test
    void convertJsonLdToCollibra() {
        JsonLdCollibraConverter converter = new JsonLdCollibraConverter(new JsonLdProcessor(), new EntityConfig("2dc40841-02fe-45cc-9223-304935f75fd2", "ad0c1d99-8d57-4665-ac9f-555aff96a8d9"));
        String jsonld = readFile("withoutFrame/test.json");

        List<Entity> entiries = converter.convertJsonLdToEntity(jsonld);

        assertThat(entiries).hasSize(5);
    }

    @Test
    void convertJsonLdToCollibraWithEmptyResult() {
        JsonLdCollibraConverter converter = new JsonLdCollibraConverter(new JsonLdProcessor(), new EntityConfig());
        String jsonld = readFile("withoutFrame/test-empty.json");

        assertThrows(IllegalArgumentException.class, () -> converter.convertJsonLdToEntity(jsonld));
    }

}