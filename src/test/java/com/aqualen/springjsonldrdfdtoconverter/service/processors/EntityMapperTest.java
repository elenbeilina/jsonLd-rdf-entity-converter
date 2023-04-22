package com.aqualen.springjsonldrdfdtoconverter.service.processors;

import com.aqualen.springjsonldrdfdtoconverter.config.EntityConfig;
import com.aqualen.springjsonldrdfdtoconverter.dto.Entity;
import com.aqualen.springjsonldrdfdtoconverter.dto.Context;
import jakarta.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtil.getJson;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtil.getJsonArray;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EntityMapperTest {

    @Spy
    private final EntityConfig config =
            new EntityConfig("2dc40841-02fe-45cc-9223-304935f75fd2","ad0c1d99-8d57-4665-ac9f-555aff96a8d9");
    @InjectMocks
    private EntityMapper mapper;

    @BeforeEach
    void setUp() {
        JsonObject object = getJson("withoutFrame/test.json");
        mapper = new EntityMapper(Context.buildContext(object), config);
    }

    @Test
    void mapJsonArrayToCollibraFormat() {
        List<JsonObject> list = getJsonArray("sparql-result.json");

        List<Entity> entiries = mapper.mapJsonArrayToCollibraFormat(list);
        assertThat(entiries).hasSize(5);

        Entity entityParent = entiries.get(0);
        String parentId = "00000-000-00021";
        assertThat(entityParent.getId()).isEqualTo(parentId);
    }

    @Test
    void mapEmptyArray() {
        List<Entity> entiries = mapper.mapJsonArrayToCollibraFormat(Collections.emptyList());
        assertThat(entiries).isEmpty();
    }
}