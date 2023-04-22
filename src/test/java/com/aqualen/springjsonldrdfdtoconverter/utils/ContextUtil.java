package com.aqualen.springjsonldrdfdtoconverter.utils;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ContextUtil {
    public static JsonObject emptyContext = Json.createObjectBuilder()
            .add("@context", Json.createObjectBuilder()
                    .add("@base", "")
                    .add("col", "")
                    .build())
            .build();
}
