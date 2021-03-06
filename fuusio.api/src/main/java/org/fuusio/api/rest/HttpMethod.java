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

import com.android.volley.Request;

/**
 * {@link HttpMethod} defines an enumerated type for representing HTTP methods.
 */
public enum HttpMethod {

    GET("GET", Request.Method.GET),
    POST("POST", Request.Method.POST),
    PUT("PUT", Request.Method.PUT),
    DELETE("DELETE", Request.Method.DELETE),
    HEAD("HEAD", Request.Method.HEAD),
    OPTIONS("OPTIONS", Request.Method.OPTIONS),
    TRACE("TRACE", Request.Method.TRACE);

    private final int mCode;
    private final String mName;

    HttpMethod(final String name, final int code) {
        mName = name;
        mCode = code;
    }

    public int getMethodCode() {
        return mCode;
    }

    public final String getName() {
        return mName;
    }

    public final String toString() {
        return mName;
    }
}
