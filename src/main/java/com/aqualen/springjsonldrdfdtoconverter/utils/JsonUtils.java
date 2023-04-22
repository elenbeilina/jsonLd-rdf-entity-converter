package com.aqualen.springjsonldrdfdtoconverter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.*;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@UtilityClass
public class JsonUtils {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns json with removed node
     *
     * @param origin   json
     * @param nodeName node name
     * @return json with removed node
     */
    public static JsonObject removeNode(JsonObject origin, String nodeName) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (Map.Entry<String, JsonValue> entry : origin.entrySet()) {
            if (!entry.getKey().equals(nodeName)) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    /**
     * Returns json with added node
     *
     * @param origin   json
     * @param nodeName node name
     * @param value    node value
     * @return json with added node
     */
    public static JsonObject addNode(JsonObject origin, String nodeName, JsonValue value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        origin.forEach(builder::add);
        builder.add(nodeName, value);

        return builder.build();
    }

    public static JsonObject getJsonObject(String jsonLd) {
        JsonReader reader = Json.createReader(new StringReader(jsonLd));

        JsonObject jsonObject = reader.readObject();
        reader.close();

        return jsonObject;
    }

    public static JsonObject getJsonObject(InputStream stream) {
        JsonReader reader = Json.createReader(stream);
        JsonObject jsonLdObject = reader.readObject();
        reader.close();

        return jsonLdObject;
    }

    public static InputStream getInputStream(JsonObject jsonObject) {
        byte[] bytes = jsonObject.toString().getBytes();

        return new ByteArrayInputStream(bytes);
    }

    public static InputStream getInputStream(String jsonObject) {
        byte[] bytes = jsonObject.getBytes();

        return new ByteArrayInputStream(bytes);
    }

    /**
     * Method for getting json value form json by filed name with logic of removing part from result string.
     *
     * @param json         parent json
     * @param valueName    name of the child json.
     * @param partToRemove string that need to be removed from result value.
     * @return value of the child json field with name "value" without removed string.
     */
    public static String getJsonValue(JsonObject json, String valueName, String partToRemove) {
        String jsonValue = getJsonValue(json, valueName);

        return jsonValue.replaceAll(partToRemove, EMPTY);
    }

    /**
     * Method for getting json value form json by filed name.
     *
     * @param json      parent json
     * @param valueName name of the child json.
     * @return value of the child json field with name "value"
     */
    public static String getJsonValue(JsonObject json, String valueName) {
        return json.getString(valueName);
    }

    @SneakyThrows
    public static String convertObjectToJson(Object object) {
        return object instanceof String ? (String) object : mapper.writeValueAsString(object);
    }
}
