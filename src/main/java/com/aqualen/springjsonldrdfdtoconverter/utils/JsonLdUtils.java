package com.aqualen.springjsonldrdfdtoconverter.utils;

import com.aqualen.springjsonldrdfdtoconverter.exception.ContextException;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Optional;

import static com.aqualen.springjsonldrdfdtoconverter.dto.Context.CONTEXT_EXCEPTION;
import static com.aqualen.springjsonldrdfdtoconverter.service.processors.JsonLdProcessor.CONTEXT;
import static com.aqualen.springjsonldrdfdtoconverter.utils.FramingUtils.getDocumentByUrl;
import static jakarta.json.JsonValue.ValueType.STRING;

@UtilityClass
public class JsonLdUtils {

    /**
     * Method that adds external context value to the jsonLd
     *
     * @param jsonLd jsonLd
     * @return jsonld object in JsonObject format with processed context
     */
    public static JsonObject getProcessedJsonLd(String jsonLd) {
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonLd);
        JsonValue context = jsonObject.get(CONTEXT);

        if (context.getValueType() == STRING) {
            JsonUtils.removeNode(jsonObject, CONTEXT);
            return JsonUtils.addNode(jsonObject, CONTEXT, getJsonByUrl(context).getJsonObject(CONTEXT));
        }

        return jsonObject;
    }

    @SneakyThrows
    public static JsonObject getJsonByUrl(JsonValue value) {
        Optional<JsonStructure> content = getDocumentByUrl(value).getJsonContent();
        return content.map(JsonValue::asJsonObject)
                .orElseThrow(() -> new ContextException(CONTEXT_EXCEPTION));

    }
}
