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
package org.fuusio.api.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.fuusio.api.dependency.ApplicationScope;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.DependencyScopeCache;
import org.fuusio.api.dependency.DependencyScopeOwner;
import org.fuusio.api.util.AppToolkit;
import org.fuusio.api.util.UIToolkit;

public abstract class FuusioApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static FuusioApplication sInstance = null;

    protected final ApplicationScope mDependencyScope;
    protected final DependencyScopeCache mDependencyScopeCache;

    protected Activity mForegroundActivity;

    protected FuusioApplication() {
        setInstance(this);
        mDependencyScope = createDependencyScope();
        mDependencyScopeCache = createDependencyScopeCache();
        AppToolkit.setApplication(this);
        UIToolkit.setApplication(this);
    }

    public DependencyScopeCache getDependencyScopeCache() {
        return mDependencyScopeCache;
    }

    @SuppressWarnings("unchecked")
    public static <T extends FuusioApplication> T getInstance() {
        return (T) sInstance;
    }

    private static void setInstance(final FuusioApplication instance) {
        sInstance = instance;
    }

    protected abstract ApplicationScope createDependencyScope();

    protected DependencyScopeCache createDependencyScopeCache() {
        return new DependencyScopeCache(this);
    }

    /**
     * Return the Google Analytics Property ID.
     *
     * @return The property ID as an {@code int} value.
     */
    public int getPropertyId() {
        return -1;
    }

    /**
     * Return the {@link Resources}.
     *
     * @return A {@link Resources}.
     */
    public static Resources getApplicationResources() {
        return sInstance.getResources();
    }


    /**
     * Return the {@link Activity} that is currently in foreground.
     *
     * @return An {@link Activity}. May return {@code null}.
     */
    public Activity getForegroundActivity() {
        return mForegroundActivity;
    }

    /**
     * Return the {@link SharedPreferences}.
     *
     * @return A {@link SharedPreferences}.
     */
    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Read the application preferences.
     */
    protected final void readPreferences() {
        final SharedPreferences preferences = getPreferences();
        onReadPreferences(preferences);
    }

    /**
     * Invoked by {@link FuusioApplication#readPreferences()}. This method should be overridden
     * in extended classes.
     *
     * @param preferences A {@link SharedPreferences} for reading the preferences.
     */
    protected void onReadPreferences(final SharedPreferences preferences) {
        // Do nothing by default
    }

    /**
     * Write the application preferences.
     */
    protected final void writePreferences() {
        final SharedPreferences preferences = getPreferences();
        final SharedPreferences.Editor editor = preferences.edit();
        onWritePreferences(preferences);
        editor.commit();
    }

    /**
     * Invoked by {@link FuusioApplication#writePreferences()}. This method should be overridden
     * in extended classes.
     *
     * @param preferences A {@link SharedPreferences} for reading the preferences.
     */
    protected void onWritePreferences(final SharedPreferences preferences) {
        // Do nothing by default
    }

    @SuppressWarnings("unchecked")
    public static <T extends FuusioApplication> T getApplication(final Activity activity) {
        return (T) activity.getApplicationContext();
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        AppToolkit.setApplication(this);
        readPreferences();
    }

    @Override
    public void onActivityCreated(final Activity activity, final Bundle inState) {
        // Do nothing by default
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        // Do nothing by default
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        mForegroundActivity = activity;
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        if (activity == mForegroundActivity) {
            mForegroundActivity = null;
        }
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        // Do nothing by default
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
        // Do nothing by default
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        if (activity instanceof DependencyScopeOwner) {
            final DependencyScopeOwner owner = (DependencyScopeOwner) activity;
            final DependencyScope scope = owner.getDependencyScope();

            if (scope != null && scope.isDisposable()) {
                mDependencyScopeCache.removeDependencyScope(owner);
            }
        }
    }
}
