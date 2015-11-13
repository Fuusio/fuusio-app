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

import android.nfc.NdefRecord;

import com.google.common.base.Preconditions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class TextRecord extends ParsedNdefRecord {

    private final String mLanguageCode;

    private final String mText;

    private TextRecord(final String languageCode, final String text) {
        mLanguageCode = Preconditions.checkNotNull(languageCode);
        mText = Preconditions.checkNotNull(text);
    }

    public static NdefRecord newTextRecord(final String text, final Locale locale, final boolean encodeInUtf8) {
        final byte[] langBytes = locale.getLanguage().getBytes(Charset.forName(CHARSET_NAME_US_ASCII));

        final Charset utfEncoding = encodeInUtf8 ? CHARSET_UTF_8 : Charset
                .forName(ENCODING_UTF_16);
        final byte[] textBytes = text.getBytes(utfEncoding);

        final int utfBit = encodeInUtf8 ? 0 : (1 << 7);
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

    public static TextRecord parse(final NdefRecord record) {
        Preconditions.checkArgument(record.getTnf() == NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(record.getType(), NdefRecord.RTD_TEXT));
        try {
            byte[] payload = record.getPayload();
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
            final String textEncoding = ((payload[0] & 0200) == 0) ? ENCODING_UTF_8 : ENCODING_UTF_16;
            final int languageCodeLength = payload[0] & 0077;
            final String languageCode = new String(payload, 1, languageCodeLength, CHARSET_NAME_US_ASCII);
            final String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
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
