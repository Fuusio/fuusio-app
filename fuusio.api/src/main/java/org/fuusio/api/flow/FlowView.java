package org.fuusio.api.flow;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.View;

public interface FlowView<T_Presenter extends Presenter> extends View<T_Presenter> {

    /**
     * Gets the {@link Flow} that controls this {@link FlowView}.
     *
     * @return A {@link Flow}.
     */
    <T extends Flow> T getFlow();

    /**
     * Sets the {@link Flow} that controls this {@link FlowView}.
     *
     * @param flow A {@link Flow}.
     */
    void setFlow(Flow flow);
}
