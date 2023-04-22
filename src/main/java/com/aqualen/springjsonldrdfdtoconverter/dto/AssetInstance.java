package com.aqualen.springjsonldrdfdtoconverter.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AssetInstance {

    private static final String ASSET_FIELD = "asset";
    private static final String STATUS_FIELD = "status";
    private static final String ASSET_TYPE_FIELD = "assettype";
    private static final String ATTRIBUTE_FIELD = "attribute";
    private static final String ATTRIBUTE_TYPE_FIELD = "attributetype";
    private static final String NAME_FIELD = "name";
    private static final String DISPLAY_NAME_FIELD = "displayname";
    private static final String UPPER_ASSET_FIELD = "Asset";

    String asset;
    String status;
    String assetType;
    String name;
    String displayName;
    String attribute;
    String attributeType;
    String upperAsset;

    public static AssetInstance getAssetInstance() {
        return AssetInstance.builder()
                .name(NAME_FIELD)
                .upperAsset(UPPER_ASSET_FIELD)
                .displayName(DISPLAY_NAME_FIELD)
                .asset(ASSET_FIELD)
                .assetType(ASSET_TYPE_FIELD)
                .attribute(ATTRIBUTE_FIELD)
                .attributeType(ATTRIBUTE_TYPE_FIELD)
                .build();
    }
}
