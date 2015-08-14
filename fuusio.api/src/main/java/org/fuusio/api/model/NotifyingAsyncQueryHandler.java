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

    public NotifyingAsyncQueryHandler(final ContentResolver pResolver,
            final AsyncQueryListener listener) {
        super(pResolver);
        setQueryListener(listener);
    }

    public void setQueryListener(final AsyncQueryListener pListener) {
        mQueryListener = new WeakReference<AsyncQueryListener>(pListener);
    }

    public void clearQueryListener() {
        mQueryListener = null;
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     */
    public void startQuery(final Uri pUri, final String[] pProjection) {
        startQuery(-1, null, pUri, pProjection, null, null, null);
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     * 
     * @param token Unique identifier passed through to
     *        {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)}
     */
    public void startQuery(final int pToken, final Uri pUri, final String[] pProjection) {
        startQuery(pToken, null, pUri, pProjection, null, null, null);
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     */
    public void startQuery(final Uri pUri, final String[] pProjection, final String pSortOrder) {
        startQuery(-1, null, pUri, pProjection, null, null, pSortOrder);
    }

    /**
     * Begin an asynchronous query with the given arguments. When finished,
     * {@link AsyncQueryListener#onQueryComplete(int, Object, Cursor)} is called if a valid
     * {@link AsyncQueryListener} is present.
     */
    public void startQuery(final Uri pUri, final String[] pProjection, final String pSelection,
            final String[] selectionArgs, final String orderBy) {
        startQuery(-1, null, pUri, pProjection, pSelection, selectionArgs, orderBy);
    }

    /**
     * Begin an asynchronous update with the given arguments.
     */
    public void startUpdate(final Uri pUri, final ContentValues pValues) {
        startUpdate(-1, null, pUri, pValues, null, null);
    }

    public void startInsert(final Uri pUri, final ContentValues pValues) {
        startInsert(-1, null, pUri, pValues);
    }

    public void startDelete(final Uri pUri) {
        startDelete(-1, null, pUri, null, null);
    }

    /** {@inheritDoc} */
    @Override
    protected void onQueryComplete(final int pToken, final Object pCookie, final Cursor pCursor) {
        final AsyncQueryListener listener = (mQueryListener == null) ? null : mQueryListener.get();

        if (listener != null) {
            listener.onQueryComplete(pToken, pCookie, pCursor);
        } else if (pCursor != null) {
            pCursor.close();
        }
    }
}
