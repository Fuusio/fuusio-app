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

import java.text.MessageFormat;

/**
 * {@code ExceptionToolkit} provides a set of convenience methods and utilities for throwing,
 * handling, and using {@link Exception}s.
 * 
 * @author Marko Salmela
 */
public class ExceptionToolkit {

    /**
     * Asserts that the given parameter value is not {@code null}. If {@code null}, throws an
     * {@link IllegalArgumentException}.
     * 
     * @param pParameterValue The parameter value to be tested.
     * @param pParameterName The name of the parameter.
     */
    public static void assertParameterNotNull(final Object pParameterValue,
            final String pParameterName) {
        if (pParameterValue == null) {
            final String message = "Parameter '{0}' may not be null.";
            throw new IllegalArgumentException(StringToolkit.formatString(message,
                    pParameterName));
        }
    }

    /**
     * Formats the given message {@link String} with given two arguments and by using the
     * {@link MessageFormat} class.
     * 
     * @param pMessage The message to be displayed as {@link String}.
     * @param pArgs The optional arguments for {@link MessageFormat} class.
     * @return A {@link String} containing the formatted message.
     */
    public static String formatMessageString(final String pMessage, final Object... pArgs) {
        return StringToolkit.formatString(pMessage, pArgs);
    }

    /**
     * Creates and throws an {@link IllegalArgumentException} with the given message.
     * 
     * @param pMessage The message to be displayed as {@link String}.
     */
    public static void throwIllegalArgumentException(final String pMessage) {
        throw new IllegalArgumentException(pMessage);
    }

    /**
     * Creates and throws an {@link IllegalStateException} with the given message.
     * 
     * @param pMessage The message to be displayed as {@link String}.
     */
    public static void throwIllegalStateException(final String pMessage) {
        throw new IllegalStateException(pMessage);
    }

    /**
     * Creates and throws an {@link IllegalArgumentException} with the given message and message
     * formatting arguments.
     * 
     * @param pMessage The message to be displayed as {@link String}.
     * @param pArgs The optional message formatting arguments.
     */
    public static void throwIllegalArgumentException(final String pMessage, final Object... pArgs) {
        final String errorMessage = StringToolkit.formatString(pMessage, pArgs);
        throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Creates and throws an {@link IllegalArgumentException} with the given message and message
     * formatting arguments.
     * 
     * @param pParameterName The name of the parameter as {@link String}.
     */
    public static void throwNullParameterException(final String pParameterName) {
        final String message = "Parameter '{0}' may not be null.";
        throw new IllegalArgumentException(StringToolkit.formatString(message,
                pParameterName));
    }
}
