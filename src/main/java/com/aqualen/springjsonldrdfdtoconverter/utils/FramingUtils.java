package com.aqualen.springjsonldrdfdtoconverter.utils;

import com.apicatalog.jsonld.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;
import com.apicatalog.jsonld.uri.UriUtils;
import jakarta.json.JsonValue;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URI;
import java.util.Objects;

import static jakarta.json.JsonValue.ValueType.OBJECT;
import static jakarta.json.JsonValue.ValueType.STRING;

@UtilityClass
public class FramingUtils {

    /**
     * Method for checking that framing is needed.
     * Frame can be either inline - json object or external - url
     *
     * @param frame frame object
     * @return boolean value of framing
     */
    public static boolean checkFrameIsNeeded(JsonValue frame) {
        if (Objects.isNull(frame)) {
            return false;
        }
        return frame.getValueType() == OBJECT ||
                frame.getValueType() == STRING && !removeDoubleQuotes(frame).isEmpty();
    }

    @SneakyThrows
    public static Document getDocumentByUrl(JsonValue value) {
        URI url = UriUtils.create(removeDoubleQuotes(value));

        JsonLdOptions options = new JsonLdOptions();
        DocumentLoaderOptions loaderOptions = new DocumentLoaderOptions();

        return options.getDocumentLoader().loadDocument(url, loaderOptions);
    }

    private String removeDoubleQuotes(JsonValue value) {
        return value.toString().replace("\"", "");
    }

}
