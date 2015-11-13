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

import org.fuusio.api.util.KeyValue;
import org.fuusio.api.util.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParams {

    public static final String DEFAULT_ENCODING = "UTF-8";

    private final List<KeyValue<String, String>> mKeyValues;
    private final String mParamsEncoding;

    public HttpParams() {
        this(DEFAULT_ENCODING);
    }

    public HttpParams(final String paramsEncoding) {
        mKeyValues = new ArrayList<>();
        mParamsEncoding = paramsEncoding;
    }

    public List<KeyValue<String, String>> getKeyValues() {
        return mKeyValues;
    }

    public HttpParams add(final String key, final String value) {
        mKeyValues.add(new KeyValue<>(key, value));
        return this;
    }

    public void addAll(final HttpParams pParams) {
        if (pParams != null) {
            mKeyValues.addAll(pParams.mKeyValues);
        }
    }

    public void clear() {
        mKeyValues.clear();
    }

    public Map<String, String> getMap() {
        final Map<String, String> map = new HashMap<>();

        for (final KeyValue<String, String> keyValue : mKeyValues) { // TODO Deal with multiple key occurrences
            map.put(keyValue.getKey(), keyValue.getValue());
        }

        return map;
    }

    public final int getSize() {
        return mKeyValues.size();
    }

    public byte[] encodeParameters() {
        return encodeParameters(mParamsEncoding);
    }

    public byte[] encodeParameters(final String paramsEncoding) {
        final StringBuilder encodedParams = new StringBuilder();

        boolean firstParameter = true;

        try {
            for (final KeyValue<String, String> keyValue : mKeyValues) {
                final String key = URLEncoder.encode(keyValue.getKey(), paramsEncoding);
                final String value = URLEncoder.encode(keyValue.getValue(), paramsEncoding);

                if (!firstParameter) {
                    encodedParams.append('&');
                } else {
                    firstParameter = false;
                }

                encodedParams.append(key);
                encodedParams.append('=');
                encodedParams.append(value);
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (final UnsupportedEncodingException pException) {
            final RuntimeException runtimeException = new RuntimeException("Encoding not supported: " + paramsEncoding, pException);
            L.wtf(this, "encodeParameters", runtimeException);
            throw runtimeException;
        }
    }

    public void encodeParameters(final StringBuilder encodedParams) {
        encodeParameters(encodedParams, mParamsEncoding);
    }

    public void encodeParameters(final StringBuilder encodedParams, final String paramsEncoding) {
        boolean firstParameter = true;

        try {
            for (final KeyValue<String, String> keyValue : mKeyValues) {
                final String key = URLEncoder.encode(keyValue.getKey(), paramsEncoding);
                final String value = URLEncoder.encode(keyValue.getValue(), paramsEncoding);

                if (!firstParameter) {
                    encodedParams.append('&');
                } else {
                    firstParameter = false;
                }

                encodedParams.append(key);
                encodedParams.append('=');
                encodedParams.append(value);
            }
        } catch (final UnsupportedEncodingException e) {
            final RuntimeException runtimeException = new RuntimeException("Encoding not supported: " + paramsEncoding, e);
            L.wtf(this, "encodeParameters", runtimeException);
            throw runtimeException;
        }
    }
}
