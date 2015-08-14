package org.fuusio.api.mvp;

import org.fuusio.api.component.Component;

/**
 * {@link Model} is interface for Model components in a MVP architectural pattern implementation.
 * @param <T_EventType> The parametrised model event type.
 */
public interface Model<T_EventType> extends Component {

    Listener addListener(Listener pListener);

    Listener removeListener(Listener pListener);

    interface Listener {

        void onModelChanged(ModelEvent pEvent);
    }
}
