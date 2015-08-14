package org.fuusio.api.fsm.state;

import org.fuusio.api.fsm.TestStateMachine;

public class CoffeeReadyState extends TestStateMachine {

    public CoffeeReadyState() {
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
}
