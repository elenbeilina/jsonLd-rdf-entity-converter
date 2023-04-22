package com.aqualen.springjsonldrdfdtoconverter.service.processors;


import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.aqualen.springjsonldrdfdtoconverter.utils.JsonUtils;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.aqualen.springjsonldrdfdtoconverter.utils.FramingUtils.checkFrameIsNeeded;
import static com.aqualen.springjsonldrdfdtoconverter.utils.FramingUtils.getDocumentByUrl;
import static jakarta.json.JsonValue.ValueType.STRING;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonLdProcessor {

    public static final String CONTEXT = "@context";
    private static final String FRAME = "@frame";

    public JsonObject getProcessedJsonLd(JsonObject jsonld) {
        JsonValue frame = jsonld.get(FRAME);

        if (checkFrameIsNeeded(frame)) {
            jsonld = frameJsonLd(jsonld, frame);
        }

        return expandJsonLd(jsonld);
    }

    @SneakyThrows
    private JsonObject expandJsonLd(JsonObject jsonObject) {
        return JsonLd.expand(JsonDocument.of(jsonObject))
                .get()
                .getJsonObject(0);
    }

    @SneakyThrows
    private JsonObject frameJsonLd(JsonObject jsonLd, JsonValue frame) {
        //Removing frame node, so there will be no warning
        JsonObject jsonWithoutFrame = JsonUtils.removeNode(jsonLd, FRAME);

        Document document = JsonDocument.of(jsonWithoutFrame);
        Document frameJson;

        if (frame.getValueType() == STRING) {
            frameJson = getDocumentByUrl(frame);
        } else {
            frameJson = JsonDocument.of(frame.asJsonObject());
        }

        return JsonLd.frame(document, frameJson)
                .ordered()
                .get();
    }
}
