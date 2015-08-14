package org.fuusio.api.fsm;


import java.util.ArrayList;

public class CoffeeMaker {

    private final ArrayList<String> mTraces;

    private boolean mHeaterOn;
    private boolean mPowerOn;
    private boolean mValveOpen;

    public CoffeeMaker() {
        mTraces = new ArrayList<>();
        mHeaterOn = false;
        mPowerOn = false;
        mValveOpen = false;
    }

    public void powerOn() {
        mPowerOn = true;
    }

    public void powerOff() {
        mPowerOn = false;
    }

    public void openWaterValve() {
        mValveOpen = true;
    }

    public void closeWaterValve() {
        mValveOpen = false;
    }

    public void switchHeaterOn() {
        mHeaterOn = true;
    }

    public void switchHeaterOff() {
        mHeaterOn = true;
    }

    public void addTrace(final String trace) {
        mTraces.add(trace);
    }

    public ArrayList<String> getTraces() {
        return mTraces;
    }

    public boolean isHeaterOn() {
        return mHeaterOn;
    }

    public boolean isPowerOn() {
        return mPowerOn;
    }

    public boolean isValveOpen() {
        return mValveOpen;
    }
}
