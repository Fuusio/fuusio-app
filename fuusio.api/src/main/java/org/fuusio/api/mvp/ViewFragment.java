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

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.AdapterView;

import org.fuusio.api.binding.ViewBindingManager;
import org.fuusio.api.binding.AdapterViewBinding;
import org.fuusio.api.binding.ViewBinding;

/**
 * {@link ViewFragment} provides an abstract base class for concrete {@link Fragment} implementations
 * that implement {@link View} components for a MVP architectural pattern implementation.
 * @param <T_Presenter> The type of the {@link Presenter}.
 */
public abstract class ViewFragment<T_Presenter extends Presenter> extends Fragment
        implements View<T_Presenter> {

    private final ViewBindingManager mDelegateManager;

    protected T_Presenter mPresenter;

    protected ViewFragment() {
        mDelegateManager = new ViewBindingManager(this);
    }

    /**
     * Gets the {@link Presenter}.
     * @return A {@link Presenter}.
     */
    @Override
    public T_Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onViewCreated(final android.view.View pView, final Bundle pInState) {
        super.onViewCreated(pView, pInState);
        getPresenter().onViewCreated(this, pInState);
    }

    @Override
    public void onActivityCreated(final Bundle pInState) {
        super.onActivityCreated(pInState);
        createBindings();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onViewStart(this);
    }

    /**
     * Invoked to bind {@link ViewBinding}s to {@link View}s. This method has to be overridden in
     * classes extended from {@link ViewFragment}.
     */
    protected void createBindings() {
        // Do nothing by default
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onViewResume(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onViewStop(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onViewPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDelegateManager.dispose();
    }

    /**
     * Looks up and returns a {@link android.view.View} with the given layout id.
     * @param pViewId A view id used in a layout XML resource.
     * @return The found {@link android.view.View}.
     */
    public <T extends android.view.View> T getView(final int pViewId) {
        return (T)getActivity().findViewById(pViewId);
    }

    /**
     * Creates and binds a {@link ViewBinding} to a {@link android.view.View} specified by the given view id.
     * @param pViewId A view id used in a layout XML resource.
     * @param <T> The parametrised type of the ViewDelagate.
     * @return The created {@link ViewBinding}.
     */
    @SuppressWarnings("unchecked")
    public <T extends ViewBinding<?>> T bind(final int pViewId) {
        return mDelegateManager.bind(pViewId);
    }

    /**
     * Binds the given {@link ViewBinding} to the specified {@link android.view.View}.
     * @param pViewId A view id in a layout XML specifying the target {@link android.view.View}.
     * @param pDelegate An {@link ViewBinding}.
     * @return The found and bound {@link android.view.View}.
     */
    @SuppressWarnings("unchecked")
    public <T extends android.view.View> T bind(final int pViewId, final ViewBinding<T> pDelegate) {
        return mDelegateManager.bind(pViewId, pDelegate);
    }

    /**
     * Binds the given {@link AdapterViewBinding} to the specified {@link AdapterView}.
     * @param pViewId A view id in a layout XML specifying the target {@link AdapterView}.
     * @param pDelegate An {@link AdapterViewBinding}.
     * @param pAdapter An {@link AdapterViewBinding.Adapter} that is assigned to {@link AdapterViewBinding}.
     * @return The found and bound {@link AdapterView}.
     */
    @SuppressWarnings("unchecked")
    public AdapterView bind(final int pViewId, final AdapterViewBinding<?> pDelegate, final AdapterViewBinding.Adapter<?> pAdapter) {
        return mDelegateManager.bind(pViewId, pDelegate, pAdapter);
    }
}
