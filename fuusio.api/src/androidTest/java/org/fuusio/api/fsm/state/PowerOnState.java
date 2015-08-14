package org.fuusio.api.fsm.state;

import org.fuusio.api.fsm.TestStateMachine;

public class PowerOnState extends TestStateMachine {

    public PowerOnState() {
        super(TestStateMachine.class, IdleState.class);
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
    public void switchPowerOff() {
        toState(PowerOffState.class);
    }
}
