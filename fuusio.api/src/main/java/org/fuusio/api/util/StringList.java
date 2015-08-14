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

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * {@link StringList} extends to {@link ArrayList} to implement a {@link java.util.List} that
 * contains {@link String} instances.
 */
public class StringList extends ArrayList<String> {

    private final static String DEFAULT_DELIMITER = ";";

    public String writeToString() {
        return writeToString(DEFAULT_DELIMITER);
    }

    public String writeToString(final String pDelimiter) {
        final StringBuilder builder = new StringBuilder();

        for (final String item : this) {
            if (builder.length() > 0) {
                builder.append(pDelimiter);
            }
            builder.append(item);
        }
        return builder.toString();
    }

    public void readFromString(final String pString) {
        readFromString(pString, DEFAULT_DELIMITER);
    }

    public void readFromString(final String pString, final String pDelimiter) {

        clear();

        if (pString != null) {
            final String string = pString.trim();
            final StringTokenizer t = new StringTokenizer(string, pDelimiter);

            while (t.hasMoreTokens()) {
                add(t.nextToken());
            }
        }
    }
}
