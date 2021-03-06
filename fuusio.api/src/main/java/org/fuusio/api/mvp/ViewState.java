/*
 * Copyright (C) 2015 Marko Salmela, http://fuusio.org
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

import org.fuusio.api.util.LifecycleState;

/**
 * {@link ViewState} represents the current state of a {@link View}.
 */
public class ViewState {

    private final View mView;

    private LifecycleState mLifecycleState;
    private boolean mRestarted;

    public ViewState(final View view) {
        mView = view;
        mLifecycleState = LifecycleState.DORMANT;
        mRestarted = false;
    }

    public boolean isCreated() {
        return mLifecycleState.isCreated();
    }

    public boolean isPaused() {
        return mLifecycleState.isPaused();
    }

    public boolean isRestarted() {
        return mRestarted;
    }

    public boolean isResumed() {
        return mLifecycleState.isResumed();
    }

    public boolean isStarted() {
        return mLifecycleState.isStarted();
    }

    public boolean isStopped() {
        return mLifecycleState.isStopped();
    }

    /**
     * Invoked the {@link View} is created.
     */
    public void onCreate() {
        mLifecycleState = LifecycleState.CREATED;
    }

    /**
     * Invoked the {@link View} is started.
     */
    public void onStart() {
        mLifecycleState = LifecycleState.STARTED;
    }

    /**
     * Invoked the {@link View} is restarted.
     */
    public void onRestart() {
        mRestarted = true;
    }

    /**
     * Invoked the {@link View} is paused.
     */
    public void onPause() {
        mLifecycleState = LifecycleState.PAUSED;
    }

    /**
     * Invoked the {@link View} is resumed.
     */
    public void onResume() {
        mLifecycleState = LifecycleState.RESUMED;
    }

    /**
     * Invoked the {@link View} is stopped.
     */
    public void onStop() {
        mLifecycleState = LifecycleState.STOPPED;
    }

    /**
     * Invoked the {@link View} is destroyed.
     */
    public void onDestroy() {
        mLifecycleState = LifecycleState.DESTROYED;
    }
}
