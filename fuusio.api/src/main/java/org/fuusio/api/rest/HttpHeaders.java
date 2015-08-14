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
package org.fuusio.api.rest;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {

    private final Map<String, String> mHeaders;

    public HttpHeaders() {
        mHeaders = new HashMap<>();
    }

    public HttpHeaders(final Map<String, String> pHeaders) {
        mHeaders = new HashMap<>(pHeaders);
    }

    public final HttpHeaders add(final HeaderRequestField pField, final String pValue) {
        return add(pField.getName(), pValue);
    }

    public final HttpHeaders add(final String pField, final String pValue) {
        mHeaders.put(pField, pValue);
        return this;
    }

    public final void addAll(final HttpHeaders pHeaders) {
        if (pHeaders != null) {
            final Map<String, String> map = pHeaders.getMap();

            for (final String key : map.keySet()) {
                mHeaders.put(key, map.get(key));
            }
        }
    }

    public final Map<String, String> getMap() {
        return mHeaders;
    }

    public void clear() {
        mHeaders.clear();
    }
}
