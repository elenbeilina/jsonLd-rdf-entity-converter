package com.aqualen.springjsonldrdfdtoconverter.service.processors;

import com.aqualen.springjsonldrdfdtoconverter.config.EntityConfig;
import com.aqualen.springjsonldrdfdtoconverter.dto.AssetInstance;
import com.aqualen.springjsonldrdfdtoconverter.dto.Entity;
import com.aqualen.springjsonldrdfdtoconverter.dto.Context;
import jakarta.json.JsonObject;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtils.getJsonValue;

@RequiredArgsConstructor
public class EntityMapper {

    private final Context context;
    private final EntityConfig config;

    public List<Entity> mapJsonArrayToCollibraFormat(List<JsonObject> array) {
        Map<String, Entity> entityMap = new HashMap<>();

        array.forEach(value -> {
            JsonObject json = value.asJsonObject();

            processAsset(json, entityMap, AssetInstance.getAssetInstance());
        });

        return new ArrayList<>(entityMap.values());
    }

    private void processAsset(JsonObject json, Map<String, Entity> entityMap,
                              AssetInstance asset) {
        String identifier = getJsonValue(json, asset.getAsset(), context.getBase());
        entityMap.computeIfAbsent(identifier, k -> createNewCollibra(json, k, asset));

        Entity entity = entityMap.get(identifier);
        addAttributesIfNeeded(entity, json, asset);
    }

    private Entity createNewCollibra(JsonObject json, String identifier,
                                     AssetInstance asset) {
        return Entity.builder()
                .resourceType(asset.getUpperAsset())
                .id(identifier)
                .domain(config.getDomain())
                .type(getJsonValue(json, asset.getAssetType(), context.getCol()))
                .status(getStatus(json, asset))
                .build();
    }

    private String getStatus(JsonObject json, AssetInstance asset) {
        return Objects.nonNull(json.getJsonObject(asset.getStatus())) ?
                getJsonValue(json, asset.getStatus(), context.getCol()) :
                config.getStatus();
    }

    private void addAttributesIfNeeded(Entity entity, JsonObject json,
                                       AssetInstance asset) {
        String type = json.getString(asset.getAttributeType(), "");
        if (type.isBlank()) {
            return;
        }

        String attributeType = type
                .replaceAll(context.getCol(), "");
        String attributeValue = getJsonValue(json, asset.getAttribute());

        if (attributeType.equals(asset.getDisplayName())) {
            entity.setName(attributeValue);
            return;
        }

        if (attributeType.equals(asset.getName())) {
            entity.setId(attributeValue);
        }
    }
}
