/**
 * Fuusio.org
 * 
 * Copyright (C) Marko Salmela 2012. All rights reserved.
 * 
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
