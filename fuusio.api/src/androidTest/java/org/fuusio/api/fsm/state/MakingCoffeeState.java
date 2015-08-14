package org.fuusio.api.fsm.state;

import org.fuusio.api.fsm.TestStateMachine;

public class MakingCoffeeState extends TestStateMachine {

    public MakingCoffeeState() {
        super(PowerOnState.class, FillingWaterTankState.class);
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
