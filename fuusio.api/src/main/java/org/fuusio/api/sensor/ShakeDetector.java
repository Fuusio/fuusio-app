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

    public ShakeDetector(final SensorManager pSensorManager) {
        mSensorManager = pSensorManager;
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


    public void addAccuracyListener(final AccuracyListener pListener) {
        if (!mAccuraryListeners.contains(pListener)) {
            mAccuraryListeners.add(pListener);
        }
    }

    public void removeAccuracyListener(final AccuracyListener pListener) {
        mAccuraryListeners.remove(pListener);
    }

    public void addGestureListener(final ShakeListener pListener) {
        if (!mShakeListeners.contains(pListener)) {
            mShakeListeners.add(pListener);
        }
    }

    public void removeGestureListener(final ShakeListener pListener) {
        mShakeListeners.remove(pListener);
    }

    @Override
    public void onSensorChanged(final SensorEvent pEvent) {

    }

    @Override
    public void onAccuracyChanged(final Sensor pSensor, final int pAccuracy) {
        if (pSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccuracy = pAccuracy;

            if (mAccuracy != pAccuracy) {
                for (final AccuracyListener listener : mAccuraryListeners) {
                    listener.onAccuracyChanged(mAccuracy, pAccuracy);
                    mAccuracy = pAccuracy;
                }
            }
        }
    }

    public interface AccuracyListener {
        void onAccuracyChanged(int pNewAccuracy, int pOldAccuracy);
    }

    public interface ShakeListener {
        void onShakeDetected();
    }

}
