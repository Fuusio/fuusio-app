package org.fuusio.api.fsm;


import org.fuusio.api.fsm.state.PowerOffState;

public class TestStateMachine extends StateMachine<TestStateMachine, CoffeeMaker> implements BrewCoffeeButtonEvents, PowerSwitchEvents, WaterTankSensorEvents {

    private boolean mDisposed;

    public TestStateMachine(final CoffeeMaker controllable) {
        super(PowerOffState.class);
        setControllable(controllable);
        mDisposed = false;
    }

    protected TestStateMachine(final Class<? extends TestStateMachine> superStateClass, final Class<? extends TestStateMachine> initialStateClass) {
        super(superStateClass, initialStateClass);
    }

    public final CoffeeMaker getCoffeeMaker() {
        return getControllable();
    }


    @Override
    public void makeButtonPressed() {
        if (isStateMachine()) {
            mCurrentState.makeButtonPressed();
        } else if (mSuperState != getStateMachine()) {
            mSuperState.makeButtonPressed();
        } else {
            onError(this, Error.ERROR_UNHANDLED_EVENT, "makeButtonPressed");
        }
    }

    @Override
    public void switchPowerOn() {
        if (isStateMachine()) {
            mCurrentState.switchPowerOn();
        } else if (mSuperState != getStateMachine()) {
            mSuperState.switchPowerOn();
        } else {
            onError(this, Error.ERROR_UNHANDLED_EVENT, "switchPowerOn");
        }
    }

    @Override
    public void switchPowerOff() {
        if (isStateMachine()) {
            mCurrentState.switchPowerOff();
        } else if (mSuperState != getStateMachine()) {
            mSuperState.switchPowerOff();
        } else {
            onError(this, Error.ERROR_UNHANDLED_EVENT, "switchPowerOff");
        }
    }

    @Override
    public void waterTankFull() {
        if (isStateMachine()) {
            mCurrentState.waterTankFull();
        } else if (mSuperState != getStateMachine()) {
            mSuperState.waterTankFull();
        } else {
            onError(this, Error.ERROR_UNHANDLED_EVENT, "waterTankFull");
        }
    }

    @Override
    public void waterTankEmpty() {
        if (isStateMachine()) {
            mCurrentState.waterTankEmpty();
        } else if (mSuperState != getStateMachine()) {
            mSuperState.waterTankEmpty();
        } else {
            onError(this, Error.ERROR_UNHANDLED_EVENT, "waterTankEmpty");
        }
    }

    // The following methods are test utilities

    public boolean hasSubstates() {
        return !mSubStates.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public <T extends TestStateMachine> T getStateInstance(final Class<? extends TestStateMachine> stateClass) {
        return (T)this.getState(stateClass);
    }

    @Override
    public boolean isSuperState(final TestStateMachine parentState) {
        return super.isSuperState(parentState);
    }

    public boolean isDisposed() {
        return mDisposed;
    }

    @Override
    protected void onDisposeStateMachine() {
        mDisposed = true;
    }

    public boolean notStateMachine() {
        return !isStateMachine();
    }
}
