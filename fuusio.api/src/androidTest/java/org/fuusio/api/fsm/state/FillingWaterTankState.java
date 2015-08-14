package org.fuusio.api.fsm.state;

import org.fuusio.api.fsm.TestStateMachine;

public class FillingWaterTankState extends TestStateMachine {

    public FillingWaterTankState() {
        super(MakingCoffeeState.class, null);
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
    public void waterTankFull() {
        toState(FilteringCoffeeState.class);
    }
}
