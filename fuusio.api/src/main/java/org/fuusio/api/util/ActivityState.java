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
package org.fuusio.api.util;

import android.app.Activity;
import android.os.Bundle;

/**
 * {@link ActivityState} represents the current state of an {@link Activity}.
 */
public class ActivityState {

    private final Activity mActivity;
    private boolean mRestarted;
    private LifecycleState mLifecycleState;

    public ActivityState(final Activity pActivity) {
        mActivity = pActivity;
        mRestarted = false;
        mLifecycleState = LifecycleState.DORMANT;
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
     * To be invoked by {@link Activity#onCreate(Bundle)}.
     */
    public void onCreate() {
        mLifecycleState = LifecycleState.CREATED;
    }

    /**
     * To be invoked by {@link Activity#onStart()}.
     */
    public void onStart() {
        mLifecycleState = LifecycleState.STARTED;
    }

    /**
     * To be invoked by {@link Activity#onRestart()}.
     */
    public void onRestart() {
        mRestarted = true;
    }

    /**
     * To be invoked by {@link Activity#onPause()}.
     */
    public void onPause() {
        mLifecycleState = LifecycleState.PAUSED;
    }

    /**
     * To be invoked by {@link Activity#onResume()}.
     */
    public void onResume() {
        mLifecycleState = LifecycleState.RESUMED;
    }

    /**
     * To be invoked by {@link Activity#onStop()}.
     */
    public void onStop() {
        mLifecycleState = LifecycleState.STOPPED;
    }

    /**
     * To be invoked by {@link Activity#onDestroy()}.
     */
    public void onDestroy() {
        mLifecycleState = LifecycleState.DESTROYED;
    }
}
