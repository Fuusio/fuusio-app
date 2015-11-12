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
package org.fuusio.api.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.fuusio.api.util.AbstractListenable;

import java.util.ArrayList;

/**
 * {@link ShakeDetector} implements a {@link SensorEventListener} to implement a configurable
 * detector for shake gestures.
 */
public class ShakeDetector implements SensorEventListener {

    private static final int SAMPLING_CACHE_DEFAULT_SIZE = 1000;

    private final ArrayList<AccuracyListener> mAccuraryListeners;
    private final ArrayList<ShakeListener> mShakeListeners;
    private final SensorManager mSensorManager;
    private final Sensor mAccelerationSensor;

    private int mAccuracy;
    private boolean mStarted;

    public ShakeDetector(final SensorManager sensorManager) {
        mSensorManager = sensorManager;
        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
        mAccuraryListeners = new ArrayList<>();
        mShakeListeners = new ArrayList<>();
    }

    public void start() {

        if (!mStarted) {
            mStarted = true;
            mSensorManager.registerListener(this, mAccelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    /**
     * Stops this {@link ShakeDetector}. A stopped {@link ShakeDetector} stops receiving Accelerator
     * Sensor events.
     */
    public void stop() {

        if (mStarted) {
            mStarted = false;
            mSensorManager.unregisterListener(this, mAccelerationSensor);
        }
    }


    public void addAccuracyListener(final AccuracyListener listener) {
        if (!mAccuraryListeners.contains(listener)) {
            mAccuraryListeners.add(listener);
        }
    }

    public void removeAccuracyListener(final AccuracyListener listener) {
        mAccuraryListeners.remove(listener);
    }

    public void addShakeListener(final ShakeListener listener) {
        if (!mShakeListeners.contains(listener)) {
            mShakeListeners.add(listener);
        }
    }

    public void removeShakeListener(final ShakeListener listener) {
        mShakeListeners.remove(listener);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccuracy = accuracy;

            if (mAccuracy != accuracy) {
                for (final AccuracyListener listener : mAccuraryListeners) {
                    listener.onAccuracyChanged(mAccuracy, accuracy);
                    mAccuracy = accuracy;
                }
            }
        }
    }

    public interface AccuracyListener {
        void onAccuracyChanged(int newAccuracy, int oldAccuracy);
    }

    public interface ShakeListener {
        void onShakeDetected();
    }

}
