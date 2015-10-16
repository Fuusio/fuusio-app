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

import android.content.res.Resources;

import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * {@link StringToolkit} provides a set of convenience methods and utilities for using and modifying
 * Strings.
 * 
 * @author Marko Salmela
 */
public class StringToolkit {

    public static final String CHARSET_UTF8 = "UTF8";

    public static final String INVALID_FILENAME_CHARS = "|\\?*<\":>+[]/'";

    /**
     * Creates a valid identifier string from the given {@link String}.
     * 
     * @param pString The object name as a {@link String}.
     * @return The valid identifier as a {@link String}.
     * @throws IllegalArgumentException If the given string is {@code null}.
     */
    public static String createValidIdentifier(final String pString) {
        if (pString == null) {
            throw new IllegalArgumentException();
        }

        final StringBuffer id = new StringBuffer();
        final int length = pString.length();

        for (int i = 0; i < length; i++) {
            final char character = pString.charAt(i);

            if (character == '.' || Character.isJavaIdentifierPart(character)) {
                id.append(character);
            } else {
                id.append('_');
            }
        }

        return id.toString();
    }

    /**
     * Tests if the given {@link String} contains only whitespaces. If the {@link String} is
     * {@code null}, boolean value {@code true} is returned.
     * 
     * @param pString The {@link String} to be tested.
     * @param pStart The start offset.
     * @param pLength The length of data.
     * @return A {@code boolean} value.
     */
    public static boolean containsOnlyWhitespaces(final String pString, final int pStart,
            final int pLength) {
        if (pString == null) {
            return true;
        }
        final char[] buffer = new char[pLength - pStart];
        pString.getChars(pStart, pStart + pLength, buffer, 0);
        return containsOnlyWhitespaces(buffer, 0, pLength - pStart);
    }

    /**
     * Tests if the given char array contains only whitespaces
     * 
     * @param pCharacters The array of {@code chars}.
     * @param pStart The start offset.
     * @param pLength The length of data.
     * @return A {@code boolean} value.
     */
    public static boolean containsOnlyWhitespaces(final char[] pCharacters, final int pStart,
            final int pLength) {
        final String string = new String(pCharacters, pStart, pLength);
        final int stringLength = string.length();

        for (int index = 0; index < stringLength; index++) {
            final char character = string.charAt(index);

            if (character >= 0x21 && character <= 0x7E) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tests if the given {@code char} array equals with the given {@link String}.
     * 
     * @param pChars The given char array.
     * @param pLength The number of characters to be compared. Should not exceed the length of the
     *        {@code char} array.
     * @param pString The given {@link String}. Cannot be {@code null}.
     * @return A boolean value {@code true} if the {@code char} array and the {@link String} contain
     *         the same characters.
     */
    public static boolean equals(final char[] pChars, final int pLength, final String pString) {
        if (pLength == pString.length()) {
            for (int i = 0; i < pLength; i++) {
                if (pChars[i] != pString.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Tests if the given {@code char} array equals with the given {@link String}.
     * 
     * @param pChars The given {@code char} array.
     * @param pOffset The offset for the {@code char} array.
     * @param pLength The number of characters to be compared. Should not exceed the length of the
     *        {@code char} array.
     * @param pString The given {@link String}. Cannot be {@code null}.
     * @return true if the {@code char} array and the {@link String} contain the same characters.
     */
    public static boolean equals(final char[] pChars, final int pOffset, final int pLength,
            final String pString) {
        if (pLength == pString.length()) {
            for (int i = 0; i < pLength; i++) {
                if (pChars[i + pOffset] != pString.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Tests whether the given {@link String} represents a syntactically valid Java identifier.
     * 
     * @param pIdentifier The given {@link String} to be tested.
     * @return A {@code boolean}.
     */
    public static boolean isValidIdentifier(final String pIdentifier) {
        if (pIdentifier == null) {
            return false;
        }

        final int length = pIdentifier.length();

        if (length == 0) {
            return false;
        }

        char character = pIdentifier.charAt(0);

        if (!Character.isJavaIdentifierStart(character)) {
            return false;
        }

        for (int i = 1; i < length; i++) {
            character = pIdentifier.charAt(i);

            if (!Character.isJavaIdentifierPart(character)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tests whether the given {@link String} represents a syntactically valid namespace name.
     * 
     * @param pString The given {@link String} to be tested.
     * @return A {@code boolean}.
     */

    public static boolean isValidNamespace(final String pString) {
        if (pString == null) {
            return false;
        }

        int counter = 0;
        int beginIndex = 0;
        int endIndex = 0;

        final int length = pString.length();

        while (endIndex >= 0 && endIndex < length) {
            endIndex = pString.indexOf('.', beginIndex);

            if (endIndex == -1) {
                endIndex = length;
            } else if (endIndex + 1 == length) {
                return false;
            }

            final String identifier = pString.substring(beginIndex, endIndex);

            if (identifier == null || !isValidIdentifier(identifier)) {
                return false;
            }

            beginIndex = endIndex + 1;
            counter++;
        }

        return (counter > 0);
    }

    public static String normalize(final String pString) {
        final Form form = Normalizer.Form.NFC;
        return normalize(pString, form);
    }

    public static String normalize(final String pString, final Normalizer.Form pForm) {
        return Normalizer.normalize(pString, pForm);
    }

    public static String encodeFileName(final String fileName) {
        return new String(fileName.getBytes(), Charset.forName(CHARSET_UTF8));
    }

    /**
     * Formats the given {@link String} with given arguments.
     * 
     * @param pString The pString {@link String}.
     * @param pArgs A {@link List} containing the arguments for {@link String#format} method.
     * @return A formatted pString {@link String}.
     */
    public static String formatString(final String pString, final List<?> pArgs) {
        if (pArgs != null) {
            return formatString(pString, pArgs.toArray());
        } else {
            return pString;
        }
    }

    /**
     * Formats the given  {@link String} with given arguments and by using the
     * {@link String#format} method.
     * 
     * @param pString The pString {@link String}.
     * @param pArgs The arguments for {@link String#format} method.
     * @return A formatted pString {@link String}.
     */
    public static String formatString(final String pString, final Object... pArgs) {

        try {
            return String.format(pString, pArgs);
        } catch (final Exception pException) {
            String string = pString;

            if (pArgs != null) {
                for (Object arg : pArgs) {
                    string += arg;
                    string += ',';
                }
            }
            return string;
        }
    }

    public static String formatString(final int pStringResId, final int... pFormatArgResIds) {
        final Resources resources = AppToolkit.getResources();
        final int count = pFormatArgResIds.length;
        final Object[] formatArgs = new String[count];

        for (int i = 0; i < count; i++) {
            formatArgs[i] = resources.getString(pFormatArgResIds[i]);
        }

        return resources.getString(pStringResId, formatArgs);
    }

    /**
     * Changes the first character of the given {@link String} to be a lowercase character.
     * 
     * @param pString The given {@link String}. It must contain at least one character.
     * @return The modified {@link String}.
     */
    public static String lowerCaseFirstCharacter(final String pString) {
        final char firstChar = pString.charAt(0);

        if (Character.isLowerCase(firstChar)) {
            return pString;
        } else {
            final StringBuffer buffer = new StringBuffer(pString);
            buffer.setCharAt(0, Character.toLowerCase(firstChar));
            return buffer.toString();
        }
    }

    /**
     * Parses the property key to getOrCreate an array of {@link String}s containing individual tokens of
     * the key.
     * 
     * @param pPropertyKey The specified property key {@link String}.
     * @return An array of {@link String}s containing the key tokens.
     */
    public static String[] parseKeyTokens(final String pPropertyKey) {
        final StringTokenizer tokenizer = new StringTokenizer(pPropertyKey, ".");
        final ArrayList<String> tokens = new ArrayList<String>();

        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextToken());
        }

        final String[] tokenStrings = new String[tokens.size()];
        tokens.toArray(tokenStrings);
        return tokenStrings;
    }

    /**
     * Parses the tokens separated by the specified separator from the given {@link String}. Parsed
     * tokens are stored into the given Vector.
     * 
     * @param pTokensString The given tokens {@link String}.
     * @param pSeparator The specified separator {@link String}.
     * @param pTokens A {@link List} used for storing the parsed tokens.
     */
    public static void parseKeyTokens(final String pTokensString, final String pSeparator,
            final List<String> pTokens) {
        final StringTokenizer tokenizer = new StringTokenizer(pTokensString, pSeparator);

        while (tokenizer.hasMoreElements()) {
            pTokens.add(tokenizer.nextToken());
        }
    }

    /**
     * Strips all the white spaces from the given {@link String}.
     * 
     * @param pString The given {@link String}.
     * @return A {@link String}.
     */
    public static String stripWhiteSpaces(final String pString) {
        final StringBuffer buffer = new StringBuffer(pString.length());
        final int length = pString.length();

        for (int index = 0; index < length; index++) {
            final char character = pString.charAt(index);

            if (character > 32) {
                buffer.append(character);
            }
        }

        return buffer.toString();
    }

    /**
     * Changes the first character of the given {@link String} to be a uppercase character.
     * 
     * @param pString The given {@link String}. It must contain at least one character.
     * @return The modified {@link String}.
     */
    public static String upperCaseFirstCharacter(final String pString) {
        final char firstChar = pString.charAt(0);

        if (Character.isUpperCase(firstChar)) {
            return pString;
        } else {
            final StringBuffer buffer = new StringBuffer(pString);
            buffer.setCharAt(0, Character.toUpperCase(firstChar));
            return buffer.toString();
        }
    }

    public static boolean isBlank(final CharSequence pString) {
        return (pString == null || pString.toString().trim().length() == 0);
    }

    public static String valueOrDefault(final String pString, final String pDefaultString) {
        return isBlank(pString) ? pDefaultString : pString;
    }

    public static String truncateAt(final String pString, final int pLength) {
        return pString.length() > pLength ? pString.substring(0, pLength) : pString;
    }

    public static String reverse(final String pString) {
        return new StringBuilder(pString).reverse().toString();
    }

    public static boolean isEmpty(final String pString) {
        return (pString == null || pString.isEmpty());
    }
}
