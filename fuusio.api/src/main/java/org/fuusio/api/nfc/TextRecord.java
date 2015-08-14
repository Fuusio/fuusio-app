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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import android.nfc.NdefRecord;

import com.google.common.base.Preconditions;

public class TextRecord extends ParsedNdefRecord {

    private final String mLanguageCode;

    private final String mText;

    private TextRecord(final String languageCode, final String text) {
        mLanguageCode = Preconditions.checkNotNull(languageCode);
        mText = Preconditions.checkNotNull(text);
    }

    public static NdefRecord newTextRecord(final String pText, final Locale pLocale,
            final boolean pEncodeInUtf8) {
        final byte[] langBytes = pLocale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        final Charset utfEncoding = pEncodeInUtf8 ? CHARSET_UTF_8 : Charset
                .forName("UTF-16");
        final byte[] textBytes = pText.getBytes(utfEncoding);

        final int utfBit = pEncodeInUtf8 ? 0 : (1 << 7);
        final char status = (char) (utfBit + langBytes.length);

        final byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;

        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public final String getLanguageCode() {
        return mLanguageCode;
    }

    public final String getText() {
        return mText;
    }

    public static TextRecord parse(final NdefRecord pRecord) {
        Preconditions.checkArgument(pRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(pRecord.getType(), NdefRecord.RTD_TEXT));
        try {
            byte[] payload = pRecord.getPayload();
            /*
             * payload[0] contains the "Status Byte Encodings" field, per the NFC Forum
             * "Text Record Type Definition" section 3.2.1.
             * 
             * bit7 is the Text Encoding Field.
             * 
             * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1): The text is encoded in
             * UTF16
             * 
             * Bit_6 is reserved for future use and must be set to zero.
             * 
             * Bits 5 to 0 are the length of the IANA language code.
             */
            final String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
            final int languageCodeLength = payload[0] & 0077;
            final String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            final String text = new String(payload, languageCodeLength + 1, payload.length
                    - languageCodeLength - 1, textEncoding);
            return new TextRecord(languageCode, text);
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean isText() {
        return true;
    }
}
