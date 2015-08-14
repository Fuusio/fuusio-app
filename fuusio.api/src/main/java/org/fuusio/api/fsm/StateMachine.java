/*
 * Copyright (C) 2000-2014 Marko Salmela.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.fsm;

import org.fuusio.api.util.L;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.HashSet;

public abstract class StateMachine<T_State extends StateMachine, T_Controllable> {

    /**
     * {@link Error} defines error types and messages for {@link StateMachine}.
     */
    public enum Error {

        ERROR_UNKNOWN("An unknown state machine error occurred while in State: %s"),
        ERROR_STATE_REENTERED("State: %s re-entered"),
        ERROR_UNHANDLED_EVENT("Unhandled event: %s received while in State: %s"),
        ERROR_UNHANDLED_ENTRY_POINT("Unhandled entry point: %s in State: %s"),
        ERROR_UNHANDLED_DEEP_HISTORY("Unhandled deep history in State: %s"),
        ERROR_UNHANDLED_SHALLOW_HISTORY("Unhandled shallow history in State: %s");

        private final String mDescription;

        Error(final String pDescription) {
            mDescription = pDescription;
        }

        public final String getDescription(final Object... pArgs) {
            return String.format(mDescription, pArgs);
        }
    }

    /**
     *  {@link HashMap} containing the substates of a state if this instance of {@link StateMachine}
     *  represents a state or the top-level states if this instance of {@link StateMachine}
     *  represents a state machice.
     */
    protected final HashSet<T_State> mSubStates;

    /**
     *The current state,
     */
    protected T_State mCurrentState;


    /**
     * A {@link Class} specifying the initial state for a state machine of for a composed state.
     */
    protected Class<? extends T_State> mInitialStateClass;

    /**
     * A {@link HashMap} used for caching state instances. {@link Class}es of states can be used as
     * keys to the {@link HashMap}, because each state object has its own {@link Class}.
     */
    protected HashMap<Class<?>, T_State> mStateCache;

    /**
     * The reference to an instance of {@link StateMachine} that represents a state machine. All
     * instances of a concrete implementation of {@link StateMachine} that represente individual
     * states of a state machine have a reference to an instance of {@link StateMachine} that
     * represents a setaet machine.
     */
    protected T_State mStateMachine;

    /**
     * The class of the super state of the state represented by this instance of {@link StateMachine}.
     * If this instance of {@link StateMachine} is a state machine, then this field has {@code null}
     * value.
     */
    protected Class<? extends T_State> mSuperStateClass;

    /**
     * The super state of the state represented by this instance of {@link StateMachine}. If this
     * instance of {@link StateMachine} is a state machine, then this field has {@code null} value.
     */
    protected T_State mSuperState;

    /**
     * Controllable is an objet that is controlled by this {@link StateMachine}.
     */
    private T_Controllable mControllable;

    /**
     * Private default constructor.
     */
    private StateMachine() {
        mSubStates = new HashSet<>();
    }

    /**
     * This constructor is to be used by a concrete implementation of {@link StateMachine}.
     * @param pInitialStateClass The {@link Class} of a state that is the initial state to be entered
     *                           at top-level when the represented state machine is started.
     */
    @SuppressWarnings("unchecked")
    protected StateMachine(final Class<? extends T_State> pInitialStateClass) {
        this();

        mInitialStateClass = pInitialStateClass;

        // We initialise these two field only in this constructor
        mStateMachine = (T_State)this;
        mStateCache = new HashMap<>();

        mStateCache.put(getClass(), (T_State) this);
    }

    /**
     * This constructor is to be used by all actual state classes which are extended from the concrete
     * implementation of this {@link StateMachine}. The class providing a concrete implementation
     * @param pSuperStateClass The {@link Class} of a state that is the super state of the state using
     *                         this constructor.
     * @param pInitialStateClass The {@link Class} of a state that is the initial state to be entered
     *                           at top-level when the represented state machine is started.
     */
    protected StateMachine(final Class<? extends T_State> pSuperStateClass, final Class<? extends T_State> pInitialStateClass) {
        this();

        mInitialStateClass = pInitialStateClass;
        mSuperStateClass = pSuperStateClass;
    }

    /**
     * Gets the controllable object that is controlled by this {@link StateMachine}.
     * @return A controllable object.
     */
    @SuppressWarnings("unchecked")
    public final T_Controllable getControllable() {
        if (isStateMachine()) {
            return mControllable;
        } else {
            return (T_Controllable) getStateMachine().getControllable();
        }
    }

    /**
     * Set the controllable object that is controlled by this {@link StateMachine}.
     * @param pControllable  A controllable object.
     */
    public void setControllable(final T_Controllable pControllable) {
        mControllable = pControllable;
    }

    /**
     * Gets the super state of this state.
     * @param <T> A type parameter for the state type.
     * @return The state object.
     */
    protected final <T extends T_State> T getSuperState() {
        return (T)mSuperState;
    }

    /**
     * Gets the current state of this {@link StateMachine} or state.
     * @param <T> A type parameter for the state type.
     * @return A state object.
     */
    public final <T extends T_State> T getCurrentState() {
        return (T)mCurrentState;
    }

    /**
     * Sets the current state of this {@link StateMachine} or state.
     * @param pState A state object representing the new current state.
     */
    protected final void setCurrentState(final T_State pState) {
        mCurrentState = pState;
    }

    /**
     * Gets the reference to a {@link StateMachine}.
     * @return A {@link StateMachine}.
     */
    protected final T_State getStateMachine() {
        return mStateMachine;
    }

    /**
     * Tests if this instance of {@link StateMachine} represents a state machine and not a state
     * that belongs to it.
     * @return A {@code boolean} value.
     */
    protected final boolean isStateMachine() {
        return (this == getStateMachine());
    }

    /**
     * Gets an instance of the specified state.
     * @param pStateClass A state object {@link Class}.
     * @return A state object. May not return {@code null}.
     */
    @SuppressWarnings("unchecked")
    protected final T_State getState(final Class<? extends T_State> pStateClass) {

        if (isStateMachine()) {
            T_State state = mStateCache.get(pStateClass);

            if (state == null) {
                try {
                    state = pStateClass.newInstance();

                    state.mStateMachine = this.mStateMachine;
                    state.mSuperState = getState(state.mSuperStateClass);
                    state.mSuperState.mSubStates.add(state);

                    mStateCache.put(pStateClass, state);

                } catch (final Exception pException) {
                }
            }
            return state;
        } else {
            return (T_State)getStateMachine().getState(pStateClass);
        }
    }

    /**
     * Causes transition from the current state to the specified state.
     * @param pStateClass A {@link Class} specifying the target state for the state transition.
     * @return The current state.
     */
    protected final T_State toState(final Class<? extends T_State> pStateClass) {
        return toState(pStateClass, 0);
    }

    /**
     * Causes transition from the current state to the specified state optionally via an entry point
     * @param pStateClass A {@link Class} specifying the target state for the state transition.
     * @param pEntryPoint A {@code int} value specifying if the optional entry point. Value zero
     *                    represents a non entry point.
     * @return The current state.
     */
    protected final T_State toState(final Class<? extends T_State> pStateClass, final int pEntryPoint) {

        if (isStateMachine()) {
            final T_State currentState = getCurrentState();

            if (currentState != null) {

                if (currentState.getClass().equals(pStateClass)) {
                    onError(currentState, Error.ERROR_STATE_REENTERED);
                    return currentState;
                }
            }

            T_State newState = getState(pStateClass);
            setCurrentState(newState);

            if (currentState != null && !newState.isSuperState(currentState)) {
                currentState.exit(newState);
            }

            return (T_State)newState.enter(pEntryPoint);
        } else {
            return (T_State) getStateMachine().toState(pStateClass, pEntryPoint);
        }
    }

    /**
     * Causes transition from the current state to the specified history state via a deep or
     * shallow history point.
     * @param pStateClass A {@link Class} specifying the target state for the state transition.
     * @param pDeepHistory A {@code boolean} value specifying if the state is entered via a deep
     *                     history point instead of shallow history point.
     * @return The current state.
     */
    protected final T_State toHistoryState(final Class<? extends T_State> pStateClass, final boolean pDeepHistory) {

        if (isStateMachine()) {
            final T_State oldCurrentState = getCurrentState();

            if (oldCurrentState != null) {

                if (oldCurrentState.getClass().equals(pStateClass)) {
                    onError(oldCurrentState, Error.ERROR_STATE_REENTERED);
                    return oldCurrentState;
                }
            }

            T_State newCurrentState = getState(pStateClass);

            if (oldCurrentState != null && !newCurrentState.isSuperState(oldCurrentState)) {
                oldCurrentState.exit(newCurrentState);
            }

            if (pDeepHistory) {
                newCurrentState = (T_State) newCurrentState.enterDeepHistory();
            } else {
                newCurrentState = (T_State) newCurrentState.enterShallowHistory();
            }

            mCurrentState = newCurrentState;
            return mCurrentState;
        } else {
            return (T_State) getStateMachine().toHistoryState(pStateClass, pDeepHistory);
        }
    }

    /**
     * Enters the represented by this instance of {@link StateMachine}. If the state is entered via
     * an entry point the index of the entry point has to be given and it has to be greater than zero.
     * @param pEntryPoint The index of the entry point to be entered. Value zero represents a non
     *                    entry point.
     * @return The current state.
     */
    protected final T_State enter(final int pEntryPoint) {
        onEnter();

        if (pEntryPoint == 0) {
            if (mInitialStateClass != null) {
                return toState(mInitialStateClass);
            } else {
                return (T_State) this;
            }
        } else {
            return enterEntryPoint(pEntryPoint);
        }
    }

    /**
     * Enters the specified entry point. If a state implementation has one or more entry points, it
     * has to override this method.
     * @param pEntryPoint The index of the entry point to be entered.
     * @return The current state after entering the deep history point.
     */
    protected T_State enterEntryPoint(final int pEntryPoint) {
        throw new IllegalStateException(Error.ERROR_UNHANDLED_ENTRY_POINT.getDescription(pEntryPoint, getClass().getSimpleName()));
    }

    /**
     * Enters a deep history point.
     * @return The current state after entering the deep history point.
     */
    @SuppressWarnings("unchecked")
    protected final T_State enterDeepHistory() {
        if (mCurrentState != null) {
            return (T_State)mCurrentState.enterDeepHistory();
        } else {
            if (mInitialStateClass != null) {
                return toState(mInitialStateClass);
            } else {
                return (T_State) this;
            }
        }
    }

    /**
     * Enters a shallow history point.
     * @return The current state after entering the shallow history point.
     */
    @SuppressWarnings("unchecked")
    protected final T_State enterShallowHistory() {
        if (mCurrentState != null) {
            return mCurrentState;
        } else {
            if (mInitialStateClass != null) {
                return toState(mInitialStateClass);
            } else {
                return (T_State) this;
            }
        }
    }

    /**
     * Invoked to exit the state  represented by this instance of {@link StateMachine}. The state
     * machine will enter the given new target state.
     * @param pNewState A new target state.
     */
    protected final void exit(final T_State pNewState) {

        if (!isStateMachine()) {
            onExit();

            if (mSuperState != null && mSuperState != getStateMachine()) {
                mSuperState.mCurrentState = this;
            }

            if (pNewState != mSuperState) {
                if (!mSuperState.isStateMachine() && !mSuperState.isSuperStateFor(pNewState)) {
                    mSuperState.exit(pNewState);
                }
            }
        }
    }

    /**
     * Invoked by {@link StateMachine#exit(StateMachine)}.
     */
    protected void onExit() {
        // By default do nothing
    }

    /**
     * Invoked by {@link StateMachine#enter(int)}.
     */
    protected void onEnter() {
        // By default do nothing
    }

    /**
     * Invoked when the specified error has occurred while in the given state.
     * @param pState A state object.
     * @param pError An {@link Error} value specifying the occurred error.
     */
    protected final void onError(final T_State pState, final Error pError) {
        final Object[] args = {pState};
        final String message = pError.getDescription(args);
        L.wtf(this, "onError", message);
    }

    /**
     /**
     * Invoked when the specified error has occurred while in the named event has been received in
     * the given state.
     * @param pState A state object.
     * @param pError An {@link Error} value specifying the occurred error.
     * @param pEventName The name of an event.
     */
    protected final void onError(final T_State pState, final Error pError, final String pEventName) {
        final Object[] args = {pEventName, pState};
        final String message = pError.getDescription(args);
        L.wtf(this, "onError", message);
    }

    /**
     * Invoked by {@link StateMachine#dispose()} for an instance of {@link StateMachine} that
     * represents a state machine.
     */
    protected void onDisposeStateMachine() {
        // By default do nothing
    }

    /**
     * Invoked by {@link StateMachine#dispose()} for an instance of {@link StateMachine} that
     * represents a state of a state machine.
     */
    protected void onDisposeState() {
        // By default do nothing
    }

    /**
     * Disposes this {@link StateMachine} and the all state objects contained by it. The method is
     * recursively invoked to all substates of a each state.
     */
    protected final void dispose() {

        if (this.isStateMachine()) {
            onDisposeStateMachine();
        } else {
            onDisposeState();
        }

        for (final T_State state : mSubStates) {
            state.dispose();
        }

        mSubStates.clear();
        mControllable = null;

        if (mStateCache != null) {
            mStateCache.clear();
        }
    }

    /**
     * Starts this {@link StateMachine}. When started, the top-level initial state is entered.
     */
    public synchronized final void start() {
        onStart();
        mCurrentState = toState(mInitialStateClass);
    }

    /**
     * Resets this {@link StateMachine}. Resetting clear the cache of state machines. A state machine
     * has to be started again after resetting.
     */
    public synchronized final void reset() {
        final T_State stateMachine = getStateMachine();

        if (isStateMachine()) {
            mStateCache.clear();
            mStateCache.put(stateMachine.getClass(), stateMachine);

            onReset();
        } else {
            stateMachine.reset();
        }
    }

    /**
     * Stops this {@link StateMachine}. Stopping causes the state machine and all of its state to be
     * disposed.
     */
    public synchronized final void stop() {
        onStop();
        dispose();
    }

    /**
     * Invoked by {@link StateMachine#start()}.
     */
    protected void onStart() {
        // Do nothing by default
    }

    /**
     * Invoked by {@link StateMachine#stop()}.
     */
    protected void onStop() {
        // Do nothing by default
    }

    /**
     * Invoked by {@link StateMachine#reset()}.
     */
    protected void onReset() {
        // Do nothing by default
    }

    /**
     * Tests if the given state object is a direct or an indirect super state of the state
     * represented by this instance of {@link StateMachine}.
     * @param pState A state object to be tested.
     * @return A {@code boolean} value.
     */
    protected boolean isSuperState(final T_State pState) {

        if (pState != null) {

            if (pState == this) {
                return false;
            }

            if (pState == mSuperState) {
                return true;
            } else if (mSuperState != null) {
                return mSuperState.isSuperState(pState);
            }
        }
        return false;
    }

    /**
     * Tests if the state object represented by this instance of {@link StateMachine} is a direct or
     * an indirect super state of the given state.
     * @param pState A state object to be tested.
     * @return A {@code boolean} value.
     */
    protected final boolean isSuperStateFor(final T_State pState) {
        if (pState.isStateMachine()) {
            return false;
        } else if (this == pState.getSuperState()) {
            return true;
        } else {
            return isSuperStateFor((T_State) pState.getSuperState());
        }
    }


    /**
     * A {@link String} representation of this {@link StateMachine} is simply the name of the class
     * implementing abstract class {@link StateMachine}.
     * @return A {@link String}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    public @interface Events {
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    public @interface Trigger {
    }
}