package com.aqualen.springjsonldrdfdtoconverter.dto;

import com.aqualen.springjsonldrdfdtoconverter.exception.ContextException;
import jakarta.json.JsonObject;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import static com.aqualen.springjsonldrdfdtoconverter.service.processors.JsonLdProcessor.CONTEXT;

/**
 * Context of jsonLd with all necessary info for connector.
 */
@Slf4j
@Value
@Builder
public class Context {

    public static final String CONTEXT_EXCEPTION = "Wrong context format!";

    String base;
    String col;
    String domain;

    public static Context buildContext(JsonObject jsonObject) {
        JsonObject contextJson = jsonObject.getJsonObject(CONTEXT);
        try {
            return Context.builder()
                    .base(contextJson.getString("@base"))
                    .col(contextJson.getString("col"))
                    .domain("")
                    .build();
        } catch (Exception e) {
            throw new ContextException(CONTEXT_EXCEPTION, e);
        }
    }
}
