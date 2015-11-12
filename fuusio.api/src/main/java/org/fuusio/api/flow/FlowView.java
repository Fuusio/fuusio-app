package org.fuusio.api.flow;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.View;

public interface FlowView<T_Presenter extends Presenter> extends View<T_Presenter> {

    /**
     * Gets the {@link Flow} that controls this {@link FlowView}.
     * @return A {@link Flow}.
     */
    Flow getFlow();

    /**
     * Sets the {@link Flow} that controls this {@link FlowView}.
     * @param pFlow A {@link Flow}.
     */
    void setFlow(Flow flow);
}
