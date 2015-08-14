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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

public class ParsedNdefRecord {

    protected final static Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

    public boolean isSmartPoster() {
        return false;
    }

    public static boolean isSmartPosterRecord(final NdefRecord pRecord) {
        try {
            SmartPosterRecord.parse(pRecord);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isText() {
        return false;
    }

    public static boolean isTextRecord(final NdefRecord pRecord) {
        try {
            TextRecord.parse(pRecord);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isUri() {
        return false;
    }

    public static boolean isUriRecord(final NdefRecord pRecord) {
        try {
            UriRecord.parse(pRecord);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static List<ParsedNdefRecord> parse(final NdefMessage pMessage) {
        return getRecords(pMessage.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(final NdefRecord[] pRecords) {
        final List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();

        for (final NdefRecord record : pRecords) {
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
