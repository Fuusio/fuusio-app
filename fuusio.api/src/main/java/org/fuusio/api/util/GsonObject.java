package org.fuusio.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class GsonObject {

    public JsonObject toJson() {
        final JsonParser jsonParser = new JsonParser();
        return (JsonObject)jsonParser.parse(toJsonString());
    }

    public String toJsonString() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T extends GsonObject> T fromJson(final String pJsonString, final Class<T> pClass) {
        final Gson gson = new Gson();
        return gson.fromJson(pJsonString, pClass);

    }

    public static <T extends GsonObject> T fromJson(final JsonObject pJsonObject, final Class<T> pClass) {
        return GsonObject.fromJson(pJsonObject.toString(), pClass);
    }
}
