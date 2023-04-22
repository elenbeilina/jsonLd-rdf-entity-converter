package com.aqualen.springjsonldrdfdtoconverter.utils;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.InputStream;
import java.util.List;

import static com.aqualen.springjsonldrdfdtoconverter.utils.FileUtils.getFileInputStream;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtils.convertObjectToJson;
import static com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtils.getJsonObject;

@UtilityClass
public class JsonUtil {

    public static JsonObject getJson(String fileName) {
        InputStream stream = getFileInputStream(fileName);
        return getJsonObject(stream);
    }

    @SneakyThrows
    public static List<JsonObject> getJsonArray(String fileName) {
        InputStream stream = getFileInputStream(fileName);
        JsonReader reader = Json.createReader(stream);
        JsonArray array = reader.readArray();
        reader.close();

        return array.getValuesAs(JsonObject.class);
    }

    @SneakyThrows
    public static boolean compareJsonObjects(Object object1, Object object2) {
        String s1 = convertObjectToJson(object1);
        String s2 = convertObjectToJson(object2);

        JSONAssert.assertEquals(s1, s2, false);

        return true;
    }
}
