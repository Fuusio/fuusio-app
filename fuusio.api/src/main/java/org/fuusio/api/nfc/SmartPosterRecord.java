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
import java.util.Arrays;
import java.util.NoSuchElementException;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;


import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

public class SmartPosterRecord extends ParsedNdefRecord {

    private final RecommendedAction mAction;
    private final TextRecord mTitleRecord;
    private final UriRecord mUriRecord;
    private final String mType;

    private SmartPosterRecord(final UriRecord pUri, final TextRecord pTitle,
            final RecommendedAction pAction, final String pType) {
        mAction = Preconditions.checkNotNull(pAction);
        mUriRecord = Preconditions.checkNotNull(pUri);
        mTitleRecord = pTitle;
        mType = pType;
    }

    public final RecommendedAction getAction() {
        return mAction;
    }

    public final TextRecord getTitle() {
        return mTitleRecord;
    }

    public final String getType() {
        return mType;
    }

    public final UriRecord getUriRecord() {
        return mUriRecord;
    }

    public static SmartPosterRecord parse(final NdefRecord pRecord) {
        Preconditions.checkArgument(pRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN);
        Preconditions.checkArgument(Arrays.equals(pRecord.getType(), NdefRecord.RTD_SMART_POSTER));
        try {
            final NdefMessage subRecords = new NdefMessage(pRecord.getPayload());
            return parse(subRecords.getRecords());
        } catch (final FormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static SmartPosterRecord parse(final NdefRecord[] pRecordsRaw) {
        try {
            final Iterable<ParsedNdefRecord> records = getRecords(pRecordsRaw);
            final UriRecord uri = Iterables.getOnlyElement(Iterables.filter(records,
                    UriRecord.class));
            final TextRecord title = getFirstIfExists(records, TextRecord.class);
            final RecommendedAction action = parseRecommendedAction(pRecordsRaw);
            final String type = parseType(pRecordsRaw);
            return new SmartPosterRecord(uri, title, action, type);
        } catch (final NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean isSmartPoster() {
        return true;
    }

    /**
     * Returns the first element of {@code elements} which is an instance of {@code type}, or
     * {@code null} if no such element exists.
     */
    private static <T> T getFirstIfExists(final Iterable<?> pElements, final Class<T> pType) {
        final Iterable<T> filtered = Iterables.filter(pElements, pType);
        T instance = null;

        if (!Iterables.isEmpty(filtered)) {
            instance = Iterables.get(filtered, 0);
        }
        return instance;
    }

    private enum RecommendedAction {

        UNKNOWN((byte) -1), DO_ACTION((byte) 0), SAVE_FOR_LATER((byte) 1), OPEN_FOR_EDITING(
                (byte) 2);

        private static final ImmutableMap<Byte, RecommendedAction> LOOKUP;

        static {
            final ImmutableMap.Builder<Byte, RecommendedAction> builder = ImmutableMap.builder();

            for (final RecommendedAction action : RecommendedAction.values()) {
                builder.put(action.getByte(), action);
            }

            LOOKUP = builder.build();
        }

        private final byte mAction;

        private RecommendedAction(final byte pAction) {
            mAction = pAction;
        }

        private byte getByte() {
            return mAction;
        }
    }

    private static NdefRecord getByType(final byte[] pType, final NdefRecord[] pRecords) {
        for (final NdefRecord record : pRecords) {
            if (Arrays.equals(pType, record.getType())) {
                return record;
            }
        }
        return null;
    }

    private static final byte[] ACTION_RECORD_TYPE = new byte[] { 'a', 'c', 't' };

    private static RecommendedAction parseRecommendedAction(final NdefRecord[] pRecords) {
        final NdefRecord record = getByType(ACTION_RECORD_TYPE, pRecords);

        if (record == null) {
            return RecommendedAction.UNKNOWN;
        }

        final byte action = record.getPayload()[0];

        if (RecommendedAction.LOOKUP.containsKey(action)) {
            return RecommendedAction.LOOKUP.get(action);
        }
        return RecommendedAction.UNKNOWN;
    }

    private static final byte[] TYPE_TYPE = new byte[] { 't' };

    private static String parseType(final NdefRecord[] pRecords) {
        final NdefRecord type = getByType(TYPE_TYPE, pRecords);

        if (type == null) {
            return null;
        }
        return new String(type.getPayload(), CHARSET_UTF_8);
    }
}
