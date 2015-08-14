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
package org.fuusio.api.binding;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class AdapterViewBinding<T_Item> extends ViewBinding<AdapterView>
        implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Adapter mAdapter;

    public AdapterViewBinding(final Adapter<T_Item> pAdapter) {
        setAdapter(pAdapter);
    }

    public AdapterViewBinding(final Adapter<T_Item> pAdapter, final AdapterView pView) {
        super(pView);
        setAdapter(pAdapter);
    }

    @Override
    public final void setView(final AdapterView pView) {
        super.setView(pView);

        if (mAdapter != null) {
            mView.setAdapter(mAdapter);
            mAdapter.setView(mView);
        }
    }

    /**
     * Set the items to {@link Adapter}.
     * @param items A {@link List} containing the items.
     */
    public final void setItems(final List<T_Item> items) {
       mAdapter.setItems(items);
    }

    /**
     * Sets the {@link Adapter}.
     * @param pAdapter An {@link Adapter}.
     */
    public final void setAdapter(final Adapter<?> pAdapter) {
        mAdapter = pAdapter;

        if (mAdapter != null && mView != null) {
            mAdapter.setView(mView);
            mView.setAdapter(mAdapter);
        }
    }

    /**
     * Gets the currently selected item.
     * @return The selected item. May be {@code null}.
     */
    public final T_Item getSelectedItem() {
        final int index = mView.getSelectedItemPosition();

        if (index >= 0) {
            return (T_Item)mAdapter.getItem(index);
        }
        return null;
    }

    @Override
    public final boolean canBind(final View pView) {
        return (pView instanceof AdapterView);
    }

    @Override
    protected final void attachListeners(final AdapterView pView) {
        super.attachListeners(pView);
        pView.setOnItemSelectedListener(this);
        pView.setOnItemClickListener(this);
    }

    @Override
    protected final void detachListeners(final AdapterView pView) {
        super.detachListeners(pView);
        pView.setOnItemSelectedListener(null);
        pView.setOnItemClickListener(null);
    }

    /**
     * {@link Adapter} provides an abstract base class for implementing adapter for
     * {@link AdapterViewBinding}s bound to {@link AdapterView}s.
     * @param <T_Item> The type of the items.
     */
    public static abstract class Adapter<T_Item> extends BaseAdapter {

        private final List<T_Item> mItems;

        private AdapterView mView;

        protected Adapter() {
            mItems = new ArrayList<T_Item>();
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(final int pPosition) {
            return mItems.get(pPosition);
        }

        @Override
        public long getItemId(final int pPosition) {
            return pPosition;
        }


        public void setItems(final List<T_Item> pItems) {
            mItems.clear();
            mItems.addAll(pItems);

            mView.post(new Runnable() {

                @Override
                public void run() {
                    Adapter.this.notifyDataSetChanged();
                    Adapter.this.notifyDataSetInvalidated();
                }
            });

        }

        @Override
        public abstract View getView(final int pPosition, final View pConvertView, final ViewGroup pParent);

        public void setView(final AdapterView pView) {
            mView = pView;
        }

    }

    @Override
    public final void onItemSelected(final AdapterView<?> pParent, final View pView, final int pPosition, final long pId) {
        final Object selectedItem = pParent.getItemAtPosition(pPosition);
        itemSelected(selectedItem);
        itemSelected(pParent, pView, pPosition, pId);
    }

    @Override
    public final void onNothingSelected(final AdapterView<?> pParent) {
        nothingSelected();
    }

    /**
     * This method should be overridden for dispatching {@link android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)} events.
     */
    protected void itemSelected(final Object pItem) {

    }

    /**
     * This method should be overridden for dispatching {@link android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)} events.
     */
    protected void itemSelected(final AdapterView<?> pParent, final View pView, final int pPosition, final long pId) {

    }

    /**
     * This method should be overridden for dispatching {@link android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)} events.
     */
    protected void nothingSelected() {

    }

    @Override
    public final void onItemClick(final AdapterView<?> pParent, final View pView, final int pPosition, final long pId) {
        final Object clickedItem = pParent.getItemAtPosition(pPosition);
        itemClicked(clickedItem);
        itemClicked(pParent, pView, pPosition, pId);
    }

    /**
     * This method should be overridden for dispatching {@link android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)} events.
     */
    protected void itemClicked(final Object pItem) {
    }

    /**
     * This method should be overridden for dispatching {@link android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)} events.
     */
    protected void itemClicked(final AdapterView<?> pParent, final View pView, final int pPosition, final long pId) {
    }

}
