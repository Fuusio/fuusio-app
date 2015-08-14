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
import android.view.*;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import java.util.HashMap;

public abstract class DelegatedListAdapter<T_ListItemProvider extends ListItemProvider> implements ListAdapter, AbsListView.OnScrollListener {

    public static final UndoItem UNDO_ITEM = new UndoItem();

    protected HashMap<Class<?>, ListItemView> mItemViews;

    protected Context mContext;
    protected T_ListItemProvider mItemProvider;
    protected int mUndoPosition;

    protected DelegatedListAdapter(final Context pContext) {
        mContext = pContext;
        mItemViews = new HashMap<>();
        mUndoPosition = -1;
    }

    public final Context getContext() {
        return mContext;
    }

    public void setItemProvider(final T_ListItemProvider pItemProvider) {
        mItemProvider = pItemProvider;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mItemProvider.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(final int pPosition) {
        return mItemProvider.isEnabled(pPosition);
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver pObserver) {
        mItemProvider.registerDataSetObserver(pObserver);
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver pObserver) {
        mItemProvider.unregisterDataSetObserver(pObserver);
    }

    @Override
    public int getCount() {
        return mItemProvider.getCount();
    }

    @Override
    public Object getItem(final int pPosition) {

        if (pPosition == mUndoPosition) {
            return UNDO_ITEM;
        } else {
            return mItemProvider.getItem(pPosition);
        }
    }

    @Override
    public long getItemId(final int pPosition) {
        return mItemProvider.getItemId(pPosition);
    }

    @Override
    public boolean hasStableIds() {
        return mItemProvider.hasStableIds();
    }

    @Override
    public android.view.View getView(final int pPosition, final android.view.View pConvertView, final ViewGroup pParent) {
        ListItemView itemView = null;

        if (pConvertView == null) {
            itemView = createItemView(pPosition);
            itemView.getInflatedView().setTag(itemView);
        } else {
            itemView = (ListItemView)pConvertView.getTag();
        }

        if (pPosition != mUndoPosition) {
            mItemProvider.initializeItemView(itemView, pPosition);
        }

        return itemView.getInflatedView();
    }

    /**
     * Constructs and appropriate {@link ListItemView} for the given item position.
     * @param pPosition The item position on list.
     * @returnA {@link ListItemView}.
     */
    protected abstract ListItemView createItemView(final int pPosition);

    @Override
    public abstract int getItemViewType(final int pPosition);

    @Override
    public abstract int getViewTypeCount();

    @Override
    public boolean isEmpty() {
        return mItemProvider.isEmpty();
    }

    public final int getUndoPosition() {
        return mUndoPosition;
    }

    public void remove(final int pPosition) {

        int position = pPosition;

        if (mUndoPosition >= 0) {
            commitRemove();

            if (mUndoPosition < position) {
                position--;
            }
        }

        mUndoPosition = position;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mItemProvider.notifyDataChanged();
    }

    public boolean canDismiss(final int pPosition) {
        return mItemProvider.canDismiss(pPosition);
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
    public void onScrollStateChanged(final AbsListView pView, final int pScrollState) {
        if (mUndoPosition >= 0) {
            commitRemove();
        }
    }

    @Override
    public void onScroll(final AbsListView pView, final int pFirstVisibleItem, final int pVisibleItemCount, final int pTotalItemCount) {
        if (mUndoPosition >= 0) {
            commitRemove();
        }
    }

    public static class UndoItem {

        private UndoItem() {
        }
    }
}

