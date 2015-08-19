package org.fuusio.api.flow;

import android.app.Activity;
import android.os.Bundle;

import org.fuusio.api.dependency.D;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.ViewActivity;

/**
 * {@link FlowActivity} is an abstract base class for implementing a {@link ViewActivity} that uses
 * a {@link Flow} to implement UI flow logic. A {@link Flow} implementation receives the lifecycle
 * events of an {@link Activity} and can control UI control logic according to the events.
 * @param <T_Flow> A type parameter for {@link Flow}.
 */
public abstract class FlowActivity<T_Flow extends Flow, T_Presenter extends Presenter> extends ViewActivity<T_Presenter> {

    protected T_Flow mFlow;

    public final T_Flow getFLow() {
        return mFlow;
    }

    public void setFlow(final T_Flow pFlow) {
        mFlow = pFlow;
    }

    @Override
    protected void onCreate(final Bundle pSavedState) {
        super.onCreate(pSavedState);

        mFlow = createFlow(pSavedState);
        D.activateScope(mFlow);
    }

    protected abstract T_Flow createFlow(final Bundle pSavedState);

    @Override
    protected void onStart() {
        super.onStart();
        mFlow.start(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFlow.restart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFlow.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFlow.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFlow.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFlow.destroy();
    }
}
