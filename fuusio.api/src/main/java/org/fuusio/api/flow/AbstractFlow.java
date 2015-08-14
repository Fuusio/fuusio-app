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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;

import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.Dependency;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.ScopeProvider;
import org.fuusio.api.mvp.View;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.ui.action.ActionContext;
import org.fuusio.api.ui.action.ActionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractFlow} provides an abstract base class for implementing {@link Flow}s.
 */
public abstract class AbstractFlow implements Flow, Presenter.Listener, ScopeProvider {

    protected final ArrayList<View> mActiveViews;

    protected int mBackStackSize;
    protected Context mContext;
    protected FlowScope mDependencyScope;
    protected FlowManager mFlowManager;
    protected FlowFragmentContainer mFragmentContainer;


    protected AbstractFlow(final FlowFragmentContainer pContainer) {
        mFragmentContainer = pContainer;
        mActiveViews = new ArrayList<>();
        mBackStackSize = 0;
        mContext = pContainer.getContext();
    }

    @Override
    public <T extends DependencyScope> T getDependencyScope() {
        if (mDependencyScope == null) {
            mDependencyScope = Dependency.getScope(this);

            if (mDependencyScope == null) {
                mDependencyScope = createDependencyScope();
            }
        }
        return (T)mDependencyScope;
    }

    @Override
    public final List<View> getActiveViews() {
        return mActiveViews;

    }
    @Override
    public final View addActiveView(final View pView) {
        if (!mActiveViews.contains(pView)) {
            mActiveViews.add(pView);
            return pView;
        }
        return null;
    }

    @Override
    public final View removeActiveView(final View pView) {
        if (mActiveViews.contains(pView)) {
            mActiveViews.remove(pView);
            return pView;
        }
        return null;
    }

    protected boolean isActiveView(final View pView) {
        return mActiveViews.contains(pView);
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
     * Creates the {@link FlowScope} for this {@link ScopeProvider}.
     * @return A {@link FlowScope}. May not be {@code null}.
     */
    protected abstract FlowScope createDependencyScope();

    @SuppressWarnings("unchecked")
    @Override
    public void activateView(final View pView) {

        if (!mFragmentContainer.canShowView(pView)) {
            return;
        }

        final Class<? extends View> viewClass = getClass(pView);
        getDependencyScope().cache(viewClass, pView);

        if (pView instanceof FlowFragment) {
            final FlowFragment fragment = (FlowFragment) pView;
            fragment.setFlow(this);
            mFragmentContainer.showFlowFragment(this, fragment, fragment.getTag());
        } else {
            throw new IllegalStateException("Fragment has to be derived from org.fuusio.api.flow.FlowFragment class");
        }

        final Presenter presenter = pView.getPresenter();
        presenter.addListener(this);

        if (presenter instanceof ActionContext) {
            final ActionManager actionManager = D.get(ActionManager.class);
            actionManager.setActiveActionContext((ActionContext)presenter);
        }
    }

    @Override
    public void onNavigatedBackTo(final View pView) {
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
            final FlowFragment fragment = (FlowFragment)fragmentManager.findFragmentByTag(tag);

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

    protected static Class<? extends View> getClass(final View pView) {

        final Class viewClass = pView.getClass();

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
        onRestart();
    }

    @Override
    public final void start() {
        final FragmentManager manager = mFragmentContainer.getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);
        Dependency.activateScope(this);

        onStart();
    }

    @Override
    public final void pause() {
        onPause();
    }

    @Override
    public final void resume() {
        onResume();
    }

    @Override
    public final void stop() {
        final FragmentManager manager = mFragmentContainer.getSupportFragmentManager();
        manager.removeOnBackStackChangedListener(this);
        mFlowManager.onFlowStopped(this);

        onStop();
    }

    @Override
    public final void destroy() {
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
    public void onStart() {
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
    public void onPresenterStarted(final Presenter pPresenter) {
    }

    @Override
    public void onPresenterResumed(final Presenter pPresenter) {
        pPresenter.addListener(this);
    }

    @Override
    public void onPresenterPaused(final Presenter pPresenter) {
        pause();
    }

    @Override
    public void onPresenterStopped(final Presenter pPresenter) {
        pPresenter.removeListener(this);
    }
}
