/*
 * Copyright (C) 2014 - 2015 Marko Salmela, http://fuusio.org
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
package org.fuusio.api.mvp;

import android.database.DataSetObserver;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractListItemProvider implements ListItemProvider {

    private final Set<DataSetObserver> mObservers;

    protected AbstractListItemProvider() {
        mObservers = new HashSet<DataSetObserver>();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(final int position) {
        return areAllItemsEnabled();
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return (getCount() == 0);
    }

    @Override
    public void remove(final int position) {
    }

    @Override
    public boolean canDismiss(final int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyDataChanged() {
        for (final DataSetObserver observer : mObservers) {
            observer.onChanged();
        }
    }

    @Override
    public void notifyDataInvalidated() {
        for (final DataSetObserver observer : mObservers) {
            observer.onInvalidated();
        }
    }
}
