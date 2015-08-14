package org.fuusio.api.mvp;

import java.util.EventObject;

/**
 * {@link ModelEvent} extends {@link  EventObject} to provide an abstract base class for implementing
 * {@link Model} events.
 * @param <T_Model> A type parameter for {@link Model}.
 * @param <T_Type> A type parameter for a type (e.g. enum type) representing Model event types.
 */
public abstract class ModelEvent<T_Model extends Model, T_Type> extends EventObject {

    private final long mTimeStamp;
    private final T_Type mType;

    protected ModelEvent(final T_Model pModel, final T_Type pType) {
        super(pModel);
        mType = pType;
        mTimeStamp = System.currentTimeMillis();
    }

    @SuppressWarnings("unchecked")
    public final T_Model getModel() {
        return (T_Model)getSource();
    }

    public final T_Type getType() {
        return mType;
    }

    public final long getTimeStamp() {
        return mTimeStamp;
    }
}
