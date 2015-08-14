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

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.ViewFragment;

import java.util.HashMap;

/**
 * {@link ViewBindingManager} is component that can be added into an object using {@link ViewBinding}
 * to establish bindings between {@link View}s and {@link Presenter}s.
 */
public class ViewBindingManager {

    private final HashMap<Integer, ViewBinding<?>> mBindingsCache;
    private final Activity mActivity;

    public ViewBindingManager(final ViewFragment pFragment) {
        this(pFragment.getActivity());
    }

    public ViewBindingManager(final Activity pActivity) {
        mBindingsCache = new HashMap<>();
        mActivity = pActivity;
    }

    /**
     * Looks up and returns a {@link View} with the given layout id.
     * @param pViewId A view id used in a layout XML resource.
     * @return The found {@link View}.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(final int pViewId) {
        return (T)mActivity.findViewById(pViewId);
    }

    /**
     * Gets a {@link ViewBinding} for the specified {@link View}.
     * @param pViewId A view id used in a layout XML resource.
     * @return The found {@link View}.
     */
    @SuppressWarnings("unchecked")
    public <T extends ViewBinding> T getBinding(final int pViewId) {
        T binding = (T) mBindingsCache.get(pViewId);

        if (binding == null) {
            final View view = getView(pViewId);

            if (view instanceof TextView) {
                binding = (T) new TextViewBinding((TextView) view);
            } else {
                throw new IllegalStateException("View of type: " + view.getClass().getName() + " is not supported.");
            }
            mBindingsCache.put(pViewId, binding);
        }
        return binding;
    }

    /**
     * Disposes this {@link ViewBindingManager} by clearing the Binding Cache.
     */
    public void dispose() {
        mBindingsCache.clear();
    }

    /**
     * Creates and binds a {@link ViewBinding} to a {@link View} specified by the given view id.
     * @param pViewId A view id used in a layout XML resource.
     * @param <T> The parametrised type of the ViewDelagate.
     * @return The created {@link ViewBinding}.
     */
    @SuppressWarnings("unchecked")
    public <T extends ViewBinding<?>> T bind(final int pViewId) {
        final View view = getView(pViewId);
        ViewBinding<?> binding;

        if (view instanceof AdapterView) {
            throw new IllegalStateException("For AdapterView derived classes use AdapterViewBinding.");
        } else  {
            binding = new TextViewBinding((TextView)view);
        }

        mBindingsCache.put(pViewId, binding);
        return (T)binding;
    }

    /**
     * Binds the given {@link ViewBinding} to the specified {@link View}.
     * @param pViewId A view id in a layout XML specifying the target {@link View}.
     * @param pBinding An {@link ViewBinding}.
     * @return The found and bound {@link View}.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T bind(final int pViewId, final ViewBinding<T> pBinding) {
        final T view = getView(pViewId);

        if (pBinding.canBind(view)) {
            pBinding.setView(view);
            mBindingsCache.put(pViewId, pBinding);
        } else {
            throw new IllegalStateException("No View with id: " + Integer.toString(pViewId) + " compatible with the given ViewBinding was found");
        }
        return view;
    }

    /**
     * Binds the given {@link AdapterViewBinding} to the specified {@link AdapterView}.
     * @param pViewId A view id in a layout XML specifying the target {@link AdapterView}.
     * @param pBinding An {@link AdapterViewBinding}.
     * @param pAdapter An {@link AdapterViewBinding.Adapter} that is assigned to {@link AdapterViewBinding}.
     * @return The found and bound {@link AdapterView}.
     */
    @SuppressWarnings("unchecked")
    public <T extends AdapterView> T bind(final int pViewId, final AdapterViewBinding<?> pBinding, final AdapterViewBinding.Adapter<?> pAdapter) {
        final T view = getView(pViewId);

        if (pBinding.canBind(view)) {
            pBinding.setAdapter(pAdapter);
            pBinding.setView(view);
            mBindingsCache.put(pViewId, pBinding);
        } else {
            throw new IllegalStateException("No View with id: " + Integer.toString(pViewId) + " compatible with the given ViewBinding was found");
        }
        return view;
    }
}
