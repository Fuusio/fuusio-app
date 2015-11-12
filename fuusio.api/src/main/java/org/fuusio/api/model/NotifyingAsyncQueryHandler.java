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
package org.fuusio.api.model;

import java.lang.ref.WeakReference;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class NotifyingAsyncQueryHandler extends AsyncQueryHandler {

    private WeakReference<AsyncQueryListener> mQueryListener;

    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);
    }

    public NotifyingAsyncQueryHandler(final ContentResolver resolver, final AsyncQueryListener listener) {
        super(resolver);
        setQueryListener(listener);
    }

    public void setQueryListener(final AsyncQueryListener listener) {
        mQueryListener = new WeakReference<AsyncQueryListener>(listener);
    }

    public void clearQueryListener() {
        mQueryListener = null;
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     */
    public void startQuery(final Uri uri, final String[] projection) {
        startQuery(-1, null, uri, projection, null, null, null);
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     * 
     * @param token Unique identifier passed through to
     *        {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)}
     */
    public void startQuery(final int token, final Uri uri, final String[] projection) {
        startQuery(token, null, uri, projection, null, null, null);
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     */
    public void startQuery(final Uri uri, final String[] projection, final String sortOrder) {
        startQuery(-1, null, uri, projection, null, null, sortOrder);
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     */
    public void startQuery(final Uri uri, final String[] projection, final String selection,
            final String[] selectionArgs, final String orderBy) {
        startQuery(-1, null, uri, projection, selection, selectionArgs, orderBy);
    }

    /**
     * Begin an asynchronous update with the given arguments.
     */
    public void startUpdate(final Uri uri, final ContentValues values) {
        startUpdate(-1, null, uri, values, null, null);
    }

    public void startInsert(final Uri uri, final ContentValues values) {
        startInsert(-1, null, uri, values);
    }

    public void startDelete(final Uri uri) {
        startDelete(-1, null, uri, null, null);
    }

    /** {@inheritDoc} */
    @Override
    protected void onQueryComplete(final int token, final Object cookie, final Cursor cursor) {
        final AsyncQueryListener listener = (mQueryListener == null) ? null : mQueryListener.get();

        if (listener != null) {
            listener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }
}
