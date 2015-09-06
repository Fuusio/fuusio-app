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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateToolkit {

    public final static String DEFAULT = "MM/dd/yyyy hh:mm:ss a Z";
    public final static String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ";
    public final static String ISO8601_NOMS = "yyyy-MM-dd'T'HH:mm:ssZ";
    public final static String RFC822 = "EEE, dd MMM yyyy HH:mm:ss Z";
    public final static String SIMPLE = "MM/dd/yyyy hh:mm:ss a";

    @SuppressLint("SimpleDateFormat")
    public static String format(final String pFormat, final Date pDate) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pFormat);
        return dateFormat.format(pDate);
    }

    public static String format(final Date pDate) {
        return format(DEFAULT, pDate);
    }

    public static String formatISO8601(final Date pDate) {
        return format(ISO8601, pDate);
    }

    public static String formatISO8601NoMilliseconds(final Date pDate) {
        return format(ISO8601_NOMS, pDate);
    }

    public static String formatRFC822(final Date pDate) {
        return format(RFC822, pDate);
    }

    public static Date parse(final String pDate) throws ParseException {
        return parse(DEFAULT, pDate);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date parse(final String pFormat, final String pDate) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pFormat);
        return dateFormat.parse(pDate);
    }

    public static Date parseISO8601(final String pDate) throws ParseException {
        return parse(ISO8601, pDate);
    }

    public static Date parseISO8601NoMilliseconds(final String pDate) throws ParseException {
        return parse(ISO8601_NOMS, pDate);
    }

    public static Date parseRFC822(final String pDate) throws ParseException {
        return parse(RFC822, pDate);
    }
}
