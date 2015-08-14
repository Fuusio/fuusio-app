package org.fuusio.api.fsm.state;

import org.fuusio.api.fsm.TestStateMachine;

public class PowerOffState extends TestStateMachine {

    public PowerOffState() {
        super(TestStateMachine.class, null);
    }

    @Override
    protected void onExit() {
        getCoffeeMaker().addTrace("EXIT: " + getClass().getSimpleName());
    }

    @Override
    protected void onEnter() {
        getCoffeeMaker().addTrace("ENTER: " + getClass().getSimpleName());
    }

    @Override
    public void switchPowerOn() {
        toState(PowerOnState.class);
    }
}
