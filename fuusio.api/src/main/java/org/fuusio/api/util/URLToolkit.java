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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import android.webkit.URLUtil;

public class URLToolkit {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static String encode(final String pFormat, final Charset pCharset, final Object... pArgs) {
        final String data = String.format(pFormat, pArgs);
        try {
            return URLEncoder.encode(data, pCharset.name());
        } catch (UnsupportedEncodingException e) {
            L.wtf(URLToolkit.class, "encode", e.getMessage());
        }
        return null;
    }

    public static String encode(final String pFormat, final Object... pArgs) {
        return encode(pFormat, DEFAULT_CHARSET, pArgs);
    }

    public static boolean isValidUrl(final String pUrlString) {
        return URLUtil.isValidUrl(pUrlString);
    }
}
