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

import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;

import org.fuusio.api.binding.AdapterViewBinding;
import org.fuusio.api.binding.ViewBinding;
import org.fuusio.api.binding.ViewBindingManager;
import org.fuusio.api.app.ActivityState;

/**
 * {@link ViewActivity} provides an abstract base class for concrete {@link AppCompatActivity}
 * implementations that implement {@link View} components for a MVP architectural pattern
 * implementation.
 * @param <T_Presenter> The type of the {@link Presenter}.
 */
public abstract class ViewActivity<T_Presenter extends Presenter> extends AppCompatActivity
    implements View<T_Presenter> {

    private final ViewBindingManager mDelegateManager;
    private final ActivityState mState;

    protected T_Presenter mPresenter;

    protected ViewActivity() {
        mDelegateManager = new ViewBindingManager(this);
        mState = new ActivityState(this);
    }

    /**
     * Gets the {@link Presenter} assigned for this {@link ViewActivity}.
     * @return A {@link Presenter}.
     */
    public T_Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mState.onStart();

        if (!mState.isRestarted()) {
            createBindings();
        }

        if (mPresenter != null) {
            mPresenter.onViewStart(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mState.onResume();

        if (mPresenter != null) {
            mPresenter.onViewResume(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mState.onStop();

        if (mPresenter != null) {
            mPresenter.onViewStop(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mState.onPause();

        if (mPresenter != null) {
            mPresenter.onViewPause(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mState.onDestroy();
        mDelegateManager.dispose();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mState.onRestart();
    }

    /**
     * Tests if the given {@link View} can be shown.
     * @param pView A {@link View}.
     * @return A {@code boolean}.
     */
    public boolean canShowView(View pView) {
        return !isPaused();
    }

    /**
     * Test if this {@link ViewActivity} has been paused.
     * @return A {@code boolean}.
     */
    public boolean isPaused() {
        return mState.isPaused();
    }

    /**
     * Test if this {@link ViewActivity} has been restarted.
     * @return A {@code boolean}.
     */
    public boolean isRestarted() {
        return mState.isRestarted();
    }

    /**
     * Invoked to bind {@link ViewBinding}s to {@link View}s. This method has to be overridden in
     * classes extended from {@link ViewFragment}.
     */
    protected void createBindings() {
        // Do nothing by default
    }

    /**
     * Looks up and returns a {@link android.view.View} with the given layout id.
     * @param pViewId A view id used in a layout XML resource.
     * @return The found {@link android.view.View}.
     */
    public <T extends android.view.View> T getView(final int pViewId) {
        return (T)findViewById(pViewId);
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
     * @param pBinding An {@link ViewBinding}.
     * @return The found and bound {@link android.view.View}.
     */
    @SuppressWarnings("unchecked")
    public <T extends android.view.View> T bind(final int pViewId, final ViewBinding<T> pBinding) {
        return mDelegateManager.bind(pViewId, pBinding);
    }

    /**
     * Binds the given {@link AdapterViewBinding} to the specified {@link AdapterView}.
     * @param pViewId A view id in a layout XML specifying the target {@link AdapterView}.
     * @param pBinding An {@link AdapterViewBinding}.
     * @param pAdapter An {@link AdapterViewBinding.Adapter} that is assigned to {@link AdapterViewBinding}.
     * @return The found and bound {@link AdapterView}.
     */
    @SuppressWarnings("unchecked")
    public AdapterView bind(final int pViewId, final AdapterViewBinding<?> pBinding, final AdapterViewBinding.Adapter<?> pAdapter) {
        return mDelegateManager.bind(pViewId, pBinding, pAdapter);
    }
}
