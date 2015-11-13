/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.fuusio.api.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ParsedNdefRecord {

    protected final static String ENCODING_UTF_8 = "UTF-8";
    protected final static String ENCODING_UTF_16 = "UTF-16";
    protected final static Charset CHARSET_UTF_8 = Charset.forName(ENCODING_UTF_8);
    protected final static String CHARSET_NAME_US_ASCII = "US-ASCII";

    public boolean isSmartPoster() {
        return false;
    }

    public static boolean isSmartPosterRecord(final NdefRecord record) {
        try {
            SmartPosterRecord.parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isText() {
        return false;
    }

    public static boolean isTextRecord(final NdefRecord record) {
        try {
            TextRecord.parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isUri() {
        return false;
    }

    public static boolean isUriRecord(final NdefRecord record) {
        try {
            UriRecord.parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static List<ParsedNdefRecord> parse(final NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(final NdefRecord[] records) {
        final List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();

        for (final NdefRecord record : records) {
            if (isUriRecord(record)) {
                elements.add(UriRecord.parse(record));
            } else if (isTextRecord(record)) {
                elements.add(TextRecord.parse(record));
            } else if (isSmartPosterRecord(record)) {
                elements.add(SmartPosterRecord.parse(record));
            }
        }
        return elements;
    }
}
