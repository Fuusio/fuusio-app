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

public interface ListItemProvider {

    boolean areAllItemsEnabled();

    boolean isEnabled(int pPosition);

    int getCount();

    Object getItem(int pPosition);

    long getItemId(int pPosition);

    boolean hasStableIds();

    boolean isEmpty();

    void registerDataSetObserver(DataSetObserver pObserver);

    void unregisterDataSetObserver(DataSetObserver pObserver);

    void initializeItemView(ListItemView pItemView, int pPosition);

    void remove(int pPosition);

    boolean canDismiss(int pPosition);

    void notifyDataChanged();

    void notifyDataInvalidated();
}
