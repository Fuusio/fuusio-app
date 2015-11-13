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
package org.fuusio.api.flow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.Dependency;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.DependencyScopeOwner;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.View;
import org.fuusio.api.ui.action.ActionContext;
import org.fuusio.api.ui.action.ActionManager;
import org.fuusio.api.util.LifecycleState;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractFlow} provides an abstract base class for implementing {@link Flow}s.
 */
public abstract class AbstractFlow implements Flow, Presenter.Listener, DependencyScopeOwner {

    protected final ArrayList<View> mActiveViews;
    protected final FlowFragmentContainer mFragmentContainer;
    protected final Bundle mParams;

    protected int mBackStackSize;
    protected Context mContext;
    protected FlowScope mDependencyScope;
    protected FlowManager mFlowManager;
    protected LifecycleState mLifecycleState;
    protected boolean mRestarted;

    /**
     * Construct a new instance of {@link AbstractFlow} with the given {@link FlowFragmentContainer}.
     *
     * @param container A {@link FlowFragmentContainer}.
     * @param params    A {@link Bundle} containing parameters for starting the {@link Flow}.
     */
    protected AbstractFlow(final FlowFragmentContainer container, final Bundle params) {
        mFragmentContainer = container;
        mParams = params;
        mActiveViews = new ArrayList<>();
        mBackStackSize = 0;
        mContext = container.getContext();
        mLifecycleState = LifecycleState.DORMANT;
        mRestarted = false;
    }

    @Override
    public <T extends DependencyScope> T getDependencyScope() {
        if (mDependencyScope == null) {
            mDependencyScope = Dependency.getScope(this);

            if (mDependencyScope == null) {
                mDependencyScope = createDependencyScope();
            }
        }
        return (T) mDependencyScope;
    }

    @Override
    public String getScopeId() {
        return getClass().getSimpleName();
    }

    @Override
    public final List<View> getActiveViews() {
        return mActiveViews;

    }

    /**
     * Adds the given {@link View} to the set of currently active Views. This method is
     * meant to be used only by the Flow Framework.
     *
     * @param view A {@link View}.
     */
    @Override
    public final View addActiveView(final View view) {
        if (!mActiveViews.contains(view)) {
            mActiveViews.add(view);
            return view;
        }
        return null;
    }

    /**
     * Removes the given {@link View} from the set of currently active Views. This method
     * is meant to be used only by the Flow Framework.
     *
     * @param view A {@link View}.
     */
    @Override
    public final View removeActiveView(final View view) {
        if (mActiveViews.contains(view)) {
            mActiveViews.remove(view);
            return view;
        }
        return null;
    }

    /**
     * Tests if the given {@link View} is currently active one.
     *
     * @param view A {@link View}.
     * @return A {@code boolean} value.
     */
    @Override
    public boolean isActiveView(final View view) {
        return mActiveViews.contains(view);
    }

    @Override
    public final FlowManager getFlowManager() {
        return mFlowManager;
    }

    @Override
    public void setFlowManager(final FlowManager pManager) {
        mFlowManager = pManager;
    }

    /**
     * Creates the {@link FlowScope} for this {@link DependencyScopeOwner}.
     *
     * @return A {@link FlowScope}. May not be {@code null}.
     */
    protected abstract FlowScope createDependencyScope();

    @SuppressWarnings("unchecked")
    @Override
    public void activateView(final View view) {

        if (!mFragmentContainer.canShowView(view)) {
            return;
        }

        final Class<? extends View> viewClass = getClass(view);
        getDependencyScope().cache(viewClass, view);

        if (view instanceof FlowFragment) {
            final FlowFragment fragment = (FlowFragment) view;
            fragment.setFlow(this);
            mFragmentContainer.showFlowFragment(this, fragment, fragment.getTag());
        } else {
            throw new IllegalStateException("Fragment has to be derived from org.fuusio.api.flow.FlowFragment class");
        }

        final Presenter presenter = view.getPresenter();
        presenter.addListener(this);

        if (presenter instanceof ActionContext) {
            final ActionManager actionManager = D.get(ActionManager.class);
            actionManager.setActiveActionContext((ActionContext) presenter);
        }
    }

    @Override
    public void onNavigatedBackTo(final View view) {
        // Do nothing by default
    }

    @Override
    public void clearBackStack() {
        final FragmentManager fragmentManager = mFragmentContainer.getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        mBackStackSize = 0;
    }

    @Override
    @SuppressWarnings("uncheckked")
    public void goBack() {
        final FragmentManager fragmentManager = mFragmentContainer.getSupportFragmentManager();
        int index = fragmentManager.getBackStackEntryCount() - 1;

        while (index >= 0) {
            final FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(index);
            final String tag = entry.getName();
            final FlowFragment fragment = (FlowFragment) fragmentManager.findFragmentByTag(tag);

            if (isActiveView(fragment)) {
                fragmentManager.popBackStackImmediate();
                index--;
            } else {
                mFragmentContainer.showFlowFragment(this, fragment, tag);
                mBackStackSize = fragmentManager.getBackStackEntryCount();
                break;
            }
        }
    }

    protected static Class<? extends View> getClass(final View view) {

        final Class viewClass = view.getClass();

        if (viewClass.isInterface() && View.class.isAssignableFrom(viewClass)) {
            return viewClass;
        }

        final Class[] interfaceClasses = viewClass.getInterfaces();

        for (int i = 0; i < interfaceClasses.length; i++) {
            if (View.class.isAssignableFrom(interfaceClasses[i])) {
                return interfaceClasses[i];
            }
        }
        return null;
    }

    @Override
    public boolean isBackPressedEventHandler() {
        return true;
    }

    @Override
    public boolean canGoBack() {
        return false;
    }

    @Override
    public final void restart() {
        mRestarted = true;
        onRestart();
    }

    @Override
    public final void start(final Bundle params) {
        mLifecycleState = LifecycleState.STARTED;
        final FragmentManager manager = mFragmentContainer.getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);
        Dependency.activateScope(this);

        onStart(params);
    }

    @Override
    public final void pause() {
        mLifecycleState = LifecycleState.PAUSED;
        onPause();
    }

    @Override
    public final void resume() {
        mLifecycleState = LifecycleState.RESUMED;
        onResume();
    }

    @Override
    public final void stop() {
        mLifecycleState = LifecycleState.STOPPED;
        final FragmentManager manager = mFragmentContainer.getSupportFragmentManager();
        manager.removeOnBackStackChangedListener(this);
        mFlowManager.onFlowStopped(this);

        onStop();
    }

    @Override
    public final void destroy() {
        mLifecycleState = LifecycleState.DESTROYED;
        Dependency.deactivateScope(this);
        mFlowManager.onFlowDestroyed(this);
        onDestroy();
        clearBackStack();
    }

    @Override
    public void onPause() {
        // Do nothing by default
    }

    @Override
    public void onRestart() {
        // Do nothing by default
    }

    @Override
    public void onStart(final Bundle params) {
        // Do nothing by default
    }

    @Override
    public void onResume() {
        // Do nothing by default
    }

    @Override
    public void onStop() {
        // Do nothing by default
    }

    @Override
    public void onDestroy() {
        // Do nothing by default
    }

    @Override
    public void onBackStackChanged() {
        final FragmentManager fragmentManager = mFragmentContainer.getSupportFragmentManager();
        final int oldBackStackSize = mBackStackSize;

        mBackStackSize = fragmentManager.getBackStackEntryCount();

        if (mBackStackSize > 0 && oldBackStackSize > mBackStackSize) {
            final FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(mBackStackSize - 1);
            final String tag = entry.getName();
            final Fragment fragment = fragmentManager.findFragmentByTag(tag);

            if (fragment instanceof View) {
                onNavigatedBackTo((View) fragment);
            }
        }
    }

    @Override
    public void onPresenterStarted(final Presenter presenter) {
    }

    @Override
    public void onPresenterResumed(final Presenter presenter) {
    }

    @Override
    public void onPresenterPaused(final Presenter presenter) {
    }

    @Override
    public void onPresenterStopped(final Presenter presenter) {
    }

    @Override
    public boolean isPaused() {
        return mLifecycleState.isPaused();
    }

    @Override
    public boolean isRestarted() {
        return mRestarted;
    }

    @Override
    public boolean isResumed() {
        return mLifecycleState.isResumed();
    }

    @Override
    public boolean isStarted() {
        return mLifecycleState.isStarted();
    }

    @Override
    public boolean isStopped() {
        return mLifecycleState.isStopped();
    }

}
