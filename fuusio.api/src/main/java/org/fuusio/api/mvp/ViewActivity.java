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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;

import org.fuusio.api.binding.AdapterViewBinding;
import org.fuusio.api.binding.ViewBinding;
import org.fuusio.api.binding.ViewBindingManager;
import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.DependencyScopeCache;
import org.fuusio.api.dependency.DependencyScopeOwner;

/**
 * {@link ViewActivity} provides an abstract base class for concrete {@link AppCompatActivity}
 * implementations that implement {@link View} components for a MVP architectural pattern
 * implementation.
 *
 * @param <T_Presenter> The type of the {@link Presenter}.
 */
public abstract class ViewActivity<T_Presenter extends Presenter> extends AppCompatActivity
        implements View<T_Presenter> {

    private final ViewBindingManager mBindingManager;
    private final ViewState mState;

    protected T_Presenter mPresenter;

    protected ViewActivity() {
        mBindingManager = new ViewBindingManager(this);
        mState = new ViewState(this);
    }

    /**
     * Gets the {@link Presenter} assigned for this {@link ViewActivity}.
     *
     * @return A {@link Presenter}.
     */
    @Override
    public T_Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = getPresenterDependency();
        }
        return mPresenter;
    }

    /**
     * This method has to implemented by concrete implementations of this class.
     *
     * @return A {@link Presenter}.
     */
    protected abstract T_Presenter getPresenterDependency();

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
        mBindingManager.dispose();

        if (this instanceof DependencyScopeOwner) {
            final DependencyScopeCache cache = D.get(DependencyScopeCache.class);
            final DependencyScopeOwner owner = (DependencyScopeOwner) this;
            cache.removeDependencyScope(owner);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mState.onRestart();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        onSaveState(outState);

        if (this instanceof DependencyScopeOwner) {
            final DependencyScopeOwner owner = (DependencyScopeOwner) this;
            final DependencyScopeCache cache = D.get(DependencyScopeCache.class);
            cache.saveDependencyScope(owner, owner.getDependencyScope());
        }
    }

    protected void onSaveState(final Bundle outState) {
        // By default do nothing
    }

    @Override
    public void onRestoreInstanceState(final Bundle inState) {
        super.onRestoreInstanceState(inState);
        onRestoreState(inState);

        if (this instanceof DependencyScopeOwner) {
            final DependencyScopeCache cache = D.get(DependencyScopeCache.class);
            final DependencyScopeOwner owner = (DependencyScopeOwner) this;
            final DependencyScope scope = cache.removeDependencyScope(owner);
            D.activateScope(owner, scope);
        }
    }

    protected void onRestoreState(final Bundle outState) {
        // By default do nothing
    }

    /**
     * Tests if the given {@link View} can be shown.
     *
     * @param view A {@link View}.
     * @return A {@code boolean}.
     */
    public boolean canShowView(View view) {
        return !isPaused();
    }

    @Override
    public ViewState getState() {
        return mState;
    }

    @Override
    public boolean isPaused() {
        return mState.isPaused();
    }

    @Override
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
     *
     * @param viewId A view id used in a layout XML resource.
     * @return The found {@link android.view.View}.
     */
    public <T extends android.view.View> T getView(final int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * Creates and binds a {@link ViewBinding} to a {@link android.view.View} specified by the given view id.
     *
     * @param viewId A view id used in a layout XML resource.
     * @param <T>    The parametrised type of the ViewDelagate.
     * @return The created {@link ViewBinding}.
     */
    @SuppressWarnings("unchecked")
    public <T extends ViewBinding<?>> T bind(final int viewId) {
        return mBindingManager.bind(viewId);
    }

    /**
     * Binds the given {@link ViewBinding} to the specified {@link android.view.View}.
     *
     * @param viewId  A view id in a layout XML specifying the target {@link android.view.View}.
     * @param binding An {@link ViewBinding}.
     * @return The found and bound {@link android.view.View}.
     */
    @SuppressWarnings("unchecked")
    public <T extends android.view.View> T bind(final int viewId, final ViewBinding<T> binding) {
        return mBindingManager.bind(viewId, binding);
    }

    /**
     * Binds the given {@link AdapterViewBinding} to the specified {@link AdapterView}.
     *
     * @param viewId  A view id in a layout XML specifying the target {@link AdapterView}.
     * @param binding An {@link AdapterViewBinding}.
     * @param adapter An {@link AdapterViewBinding.Adapter} that is assigned to {@link AdapterViewBinding}.
     * @return The found and bound {@link AdapterView}.
     */
    @SuppressWarnings("unchecked")
    public AdapterView bind(final int viewId, final AdapterViewBinding<?> binding, final AdapterViewBinding.Adapter<?> adapter) {
        return mBindingManager.bind(viewId, binding, adapter);
    }
}
