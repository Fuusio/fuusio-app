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
package org.fuusio.api.dependency;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * {@link ApplicationScope} provides an abstract base class for implementing an application
 * specific {@link DependencyScope} that has the same lifecycle as the application. An instance of
 * {@link ApplicationScope} is not disposable.
 */
public abstract class ApplicationScope<T_Application extends Application> extends DependencyScope {

    private static ApplicationScope sInstance = null;

    private final T_Application mApplication;

    protected ApplicationScope(final T_Application application) {
        mApplication = application;
        sInstance = this;
    }

    /**
     * Gets the singleton instance of {@link ApplicationScope}. A singleton instance of
     * concreate implementation of {@link ApplicationScope} should be created by an
     * {@link Application}.
     *
     * @param <T> The parameter type extending {@link ApplicationScope}.
     * @return An instance of {@link ApplicationScope}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ApplicationScope> T getInstance() {
        return (T) sInstance;
    }

    @Override
    protected <T> T getDependency() {

        if (type(Application.class)) {
            return dependency(mApplication);
        } else if (type(Context.class)) {
            return dependency(mApplication.getApplicationContext());
        } else if (type(AccountManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.ACCOUNT_SERVICE));
        } else if (type(ActivityManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE));
        } else if (type(AlarmManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.ALARM_SERVICE));
        } else if (type(AudioManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.AUDIO_SERVICE));
        } else if (type(BatteryManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.BATTERY_SERVICE));
        } else if (type(BluetoothManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE));
        } else if (type(InputMethodManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        } else if (type(LocationManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.LOCATION_SERVICE));
        } else if (type(NfcManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.NFC_SERVICE));
        } else if (type(PackageManager.class)) {
            return dependency(getApplicationContext().getPackageManager());
        } else if (type(SensorManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.SENSOR_SERVICE));
        } else if (type(Vibrator.class)) {
            return dependency(getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE));
        } else if (type(WindowManager.class)) {
            return dependency(getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        }
        return null;
    }

    /**
     * Gets the application {@link Context}.
     *
     * @return A {@link Context}.
     */
    public final Context getApplicationContext() {
        return mApplication.getApplicationContext();
    }

    /**
     * Gets the {@link Application}.
     *
     * @return A {@link Application}.
     */
    @SuppressWarnings("unchecked")
    public final T_Application getApplication() {
        return mApplication;
    }

    /**
     * Overrides to return a {@code boolean} value {@code false }because an instance of
     * {@link ApplicationScope} is not disposable.
     *
     * @return A {@code boolean} value {@code false}.
     */
    @Override
    public final boolean isDisposable() {
        return false;
    }
}
