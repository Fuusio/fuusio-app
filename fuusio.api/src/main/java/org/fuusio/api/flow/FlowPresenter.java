package org.fuusio.api.flow;

import org.fuusio.api.mvp.Presenter;

/**
 * {@link FlowPresenter} ...
 */
public interface FlowPresenter<T_View extends FlowView, T_Listener extends Presenter.Listener> extends Presenter<T_View, T_Listener> {

    /**
     * Gets the {@link Flow} that controls this {@link FlowPresenter}.
     *
     * @return A {@link Flow}.
     */
    <T extends Flow> T getFlow();

    /**
     * Sets the {@link Flow} that controls this {@link FlowPresenter}.
     *
     * @param flow A {@link Flow}.
     */
    void setFlow(Flow flow);
}
