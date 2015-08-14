// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: ExceptionToolkit
// Package: FloXP Utilites (org.fuusio.api.util)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2000-2011. All Rights Reserved.
//
// This software is the proprietary information of Marko Salmela.
// Use is subject to license terms. This software is protected by
// copyright and distributed under licenses restricting its use,
// copying, distribution, and decompilation. No part of this software
// or associated documentation may be reproduced in any form by any
// means without prior written authorization of Marko Salmela.
//
// Disclaimer:
// -----------
//
// This software is provided by the author 'as is' and any express or implied
// warranties, including, but not limited to, the implied warranties of
// merchantability and fitness for a particular purpose are disclaimed.
// In no event shall the author be liable for any direct, indirect,
// incidental, special, exemplary, or consequential damages (including, but
// not limited to, procurement of substitute goods or services, loss of use,
// data, or profits; or business interruption) however caused and on any
// theory of liability, whether in contract, strict liability, or tort
// (including negligence or otherwise) arising in any way out of the use of
// this software, even if advised of the possibility of such damage.
// ============================================================================

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
