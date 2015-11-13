package org.fuusio.api.flow;

import org.fuusio.api.mvp.AbstractPresenter;
import org.fuusio.api.mvp.Presenter;

/**
 * {@link AbstractFlowPresenter} extends {@link AbstractPresenter} to provide an abstract base class
 * for implementing {@link FlowPresenter}s that are controlled by {@link Flow}s.
 */
public class AbstractFlowPresenter<T_View extends FlowView, T_Listener
        extends Presenter.Listener> extends AbstractPresenter<T_View, T_Listener>
        implements FlowPresenter<T_View, T_Listener> {

    private Flow mFlow;

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Flow> T getFlow() {
        return (T) mFlow;
    }

    @Override
    public void setFlow(final Flow flow) {
        mFlow = flow;
    }
}
