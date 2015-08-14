package org.fuusio.api.fsm.state;

import org.fuusio.api.fsm.TestStateMachine;

public class IdleState extends TestStateMachine {

    public IdleState() {
        super(PowerOnState.class, null);
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
    public void makeButtonPressed() {
        toState(MakingCoffeeState.class);
    }
}
