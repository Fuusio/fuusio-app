package org.fuusio.api.mvp;

import java.util.ArrayList;

public class AbstractModel<T_EventType> implements Model<T_EventType> {

    private final ArrayList<Listener> mListeners;

    protected AbstractModel() {
        mListeners = new ArrayList<>();
    }

    @Override
    public Listener addListener(final Listener pListener) {
        if (!mListeners.contains(pListener)) {
            mListeners.add(pListener);
            return pListener;
        }
        return null;
    }

    @Override
    public Listener removeListener(final Listener pListener) {
        if (mListeners.contains(pListener)) {
            mListeners.remove(pListener);
            return pListener;
        }
        return null;
    }

    protected ModelEvent createEvent(final T_EventType pType) {
        return null;
    }

    protected void notifyModelChanged(final T_EventType pType) {
        final ModelEvent event = createEvent(pType);

        if (event != null) {
            for (final Listener listener : mListeners) {
                listener.onModelChanged(event);
            }
        }
    }
}
