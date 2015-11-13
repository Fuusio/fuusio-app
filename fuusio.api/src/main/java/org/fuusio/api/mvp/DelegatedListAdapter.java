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

import android.content.Context;
import android.database.DataSetObserver;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import java.util.HashMap;

public abstract class DelegatedListAdapter<T_ListItemProvider extends ListItemProvider> implements ListAdapter, AbsListView.OnScrollListener {

    public static final UndoItem UNDO_ITEM = new UndoItem();

    protected HashMap<Class<?>, ListItemView> mItemViews;

    protected Context mContext;
    protected T_ListItemProvider mItemProvider;
    protected int mUndoPosition;

    protected DelegatedListAdapter(final Context context) {
        mContext = context;
        mItemViews = new HashMap<>();
        mUndoPosition = -1;
    }

    public final Context getContext() {
        return mContext;
    }

    public void setItemProvider(final T_ListItemProvider itemProvider) {
        mItemProvider = itemProvider;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mItemProvider.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(final int position) {
        return mItemProvider.isEnabled(position);
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {
        mItemProvider.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {
        mItemProvider.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return mItemProvider.getCount();
    }

    @Override
    public Object getItem(final int position) {

        if (position == mUndoPosition) {
            return UNDO_ITEM;
        } else {
            return mItemProvider.getItem(position);
        }
    }

    @Override
    public long getItemId(final int position) {
        return mItemProvider.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return mItemProvider.hasStableIds();
    }

    @Override
    public android.view.View getView(final int position, final android.view.View convertView, final ViewGroup parent) {
        ListItemView itemView = null;

        if (convertView == null) {
            itemView = createItemView(position);
            itemView.getInflatedView().setTag(itemView);
        } else {
            itemView = (ListItemView) convertView.getTag();
        }

        if (position != mUndoPosition) {
            mItemProvider.initializeItemView(itemView, position);
        }

        return itemView.getInflatedView();
    }

    /**
     * Constructs and appropriate {@link ListItemView} for the given item position.
     *
     * @param position The item position on list.
     * @returnA {@link ListItemView}.
     */
    protected abstract ListItemView createItemView(final int position);

    @Override
    public abstract int getItemViewType(final int position);

    @Override
    public abstract int getViewTypeCount();

    @Override
    public boolean isEmpty() {
        return mItemProvider.isEmpty();
    }

    public final int getUndoPosition() {
        return mUndoPosition;
    }

    public void remove(final int position) {

        int removePosition = position;

        if (mUndoPosition >= 0) {
            commitRemove();

            if (mUndoPosition < position) {
                removePosition--;
            }
        }

        mUndoPosition = position;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mItemProvider.notifyDataChanged();
    }

    public boolean canDismiss(final int position) {
        return mItemProvider.canDismiss(position);
    }

    public void undoRemove() {
        if (mUndoPosition >= 0) {
            mUndoPosition = -1;
            notifyDataSetChanged();
        }
    }

    public void commitRemove() {
        if (mUndoPosition >= 0) {
            mItemProvider.remove(mUndoPosition);
            mUndoPosition = -1;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        if (mUndoPosition >= 0) {
            commitRemove();
        }
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        if (mUndoPosition >= 0) {
            commitRemove();
        }
    }

    public static class UndoItem {

        private UndoItem() {
        }
    }
}

