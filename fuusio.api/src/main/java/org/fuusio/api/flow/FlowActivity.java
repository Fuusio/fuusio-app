package org.fuusio.api.flow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.fuusio.api.dependency.D;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.View;
import org.fuusio.api.mvp.ViewActivity;

/**
 * {@link FlowActivity} is an abstract base class for implementing a {@link ViewActivity} that uses
 * a {@link Flow} to implement UI flow logic. A {@link Flow} implementation receives the lifecycle
 * events of an {@link Activity} and can control UI control logic according to the events.
 * @param <T_Flow> A type parameter for {@link Flow}.
 */
public abstract class FlowActivity<T_Flow extends Flow, T_Presenter extends Presenter> extends ViewActivity<T_Presenter> implements FlowFragmentContainer {

    protected T_Flow mFlow;
    protected Class<T_Flow> mFlowClass;

    public final T_Flow getFlow() {
        return mFlow;
    }

    public void setFlow(final T_Flow flow) {
        mFlow = flow;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(final Bundle inState) {
        super.onCreate(inState);

        mFlow = createFlow(inState);
        D.activateScope(mFlow);
    }

    /**
     * Sets the {@link Class} for creating the {@link Flow} in method
     * {@link FlowActivity#setFlowClass(Class)}.
     * @param flowClass A {@link Class}.
     */
    protected void setFlowClass(final Class<T_Flow> flowClass) {
        mFlowClass = flowClass;
    }

    /**
     * Creates the {@link Flow} for this {@link FlowActivity}. This method can be overridden in
     * concrete implementations of {@link FlowActivity} if method {@link FlowActivity} used.
     * @param inState
     * @return
     */
    protected T_Flow createFlow(final Bundle inState) {

        T_Flow flow = null;

        if (mFlowClass != null) {
            flow = FlowManager.createFlow(mFlowClass, this, inState);
        }
        return flow;
    }

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
