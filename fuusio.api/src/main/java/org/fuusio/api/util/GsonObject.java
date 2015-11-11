/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    public static <T extends GsonObject> T fromJson(final String jsonString, final Class<T> objectClass) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, objectClass);

    }

    public static <T extends GsonObject> T fromJson(final JsonObject jsonObject, final Class<T> objectClass) {
        return GsonObject.fromJson(jsonObject.toString(), objectClass);
    }
}
