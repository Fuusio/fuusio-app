/*
 * Copyright (C) 2014 - 2015 Marko Salmela, http://fuusio.org
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
package org.fuusio.api.flow;

import android.os.Bundle;

import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.Dependency;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.DependencyScopeOwner;

import java.lang.reflect.Constructor;

public class FlowManager {

    private static final String POSTFIX_IMPL = "Impl";

    private static FlowManager sInstance = null;
    private static DependencyScopeOwner sTestScopeManager = null;

    private Flow mActiveFlow;

    private FlowManager() {
    }

    /**
     * Gets the singleton of {@link FlowManager}.
     *
     * @return A {@link FlowManager}.
     */
    public static FlowManager getInstance() {

        if (sInstance == null) {
            sInstance = new FlowManager();
        }
        return sInstance;
    }

    /**
     * Sets a {@link DependencyScopeOwner} that is used to provide a {@link DependencyScope} for testing
     * purposes.
     *
     * @param provider A {@link DependencyScopeOwner}.
     */
    public static void setTestScopeProvider(final DependencyScopeOwner provider) {
        sTestScopeManager = provider;
    }

    /**
     * Gets an instance of {@link Flow} that is used a mock. Mock {@link Flow}s are made available
     * by setting a {@link DependencyScope} that provides them using method
     *
     * @param flowClass A class specifying the {@link Flow}. The class may an interface or implementation
     *                  class as long as the implementation class name is the interface class name with
     *                  postfix {@code Impl}.
     * @return A {@link Flow}. May return {@code null}
     */
    private static <T extends Flow> T getMockFlow(final Class<T> flowClass) {
        T flow = null;

        if (sTestScopeManager != null) {
            final DependencyScope savedScope = D.getActiveScope();
            D.activateScope(sTestScopeManager);

            flow = D.get(flowClass);

            if (flow == null && flowClass.isInterface()) {
                final Class<T> implClass;
                try {
                    implClass = (Class<T>) Class.forName(flowClass.getName() + POSTFIX_IMPL);
                    flow = D.get(implClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            D.deactivateScope(sTestScopeManager);

            if (savedScope != null) {
                D.activateScope(savedScope.getOwner());
            }
        }
        return flow;
    }

    /**
     * Gets the currently active {@link Flow}.
     *
     * @return A {@link Flow}. May return {@code null} if there is no currently active {@link Flow}.
     */
    public Flow getActiveFlow() {
        return mActiveFlow;
    }

    /**
     * Creates the specified {@link Flow}, but does not start it. If the flow is
     * a {@link DependencyScopeOwner} its {@link FlowScope} is added to cache of
     * {@link DependencyScope}s.
     *
     * @param flowClass A {@link Flow}
     * @param container A {@link FlowFragmentContainer}.
     * @param params    A {@link Bundle} containing parameters for the started {@link Flow}.
     * @param <T>       The type extended from {@link Flow}.
     * @return A {@link Flow}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Flow> T createFlow(final Class<T> flowClass, final FlowFragmentContainer container, final Bundle params) {

        T flow = getMockFlow(flowClass);

        if (flow == null) {
            Class<T> implClass = flowClass;

            try {
                if (flowClass.isInterface()) {
                    implClass = (Class<T>) Class.forName(flowClass.getName() + POSTFIX_IMPL);
                }

                final Class[] paramTypes = {FlowFragmentContainer.class, Bundle.class};
                final Object[] paramValues = {container, params};
                final Constructor<T> constructor = implClass.getConstructor(paramTypes);
                flow = constructor.newInstance(paramValues);
                Dependency.addScope(flow);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        return flow;
    }

    /**
     * Creates and starts the specified {@link Flow} whose {@link android.app.Fragment}s are hosted by
     * the given {@link FlowFragmentContainer}.
     *
     * @param flowClass A {@link Class} specifying the {@link Flow} to be created and started.
     * @param container A {@link FlowFragmentContainer}.
     * @param params    A {@link Bundle} containing parameters for the created and started {@link Flow}.
     * @param <T>       The type extended from {@link Flow}.
     * @return A {@link Flow}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Flow> T startFlow(final Class<T> flowClass, final FlowFragmentContainer container, final Bundle params) {

        T flow = getMockFlow(flowClass);

        if (flow == null) {
            Class<T> implClass = flowClass;

            try {
                if (flowClass.isInterface()) {
                    implClass = (Class<T>) Class.forName(flowClass.getName() + POSTFIX_IMPL);
                }

                final Class[] paramTypes = {FlowFragmentContainer.class, Bundle.class};
                final Object[] paramValues = {container, params};
                final Constructor<T> constructor = implClass.getConstructor(paramTypes);
                flow = constructor.newInstance(paramValues);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        final FlowManager flowManager = D.get(FlowManager.class);
        return flowManager.startFlow(flow, params);
    }

    /**
     * Starts the given {@link Flow}.
     *
     * @param flow   {The {@link Flow} to be started.
     * @param params A {@link Bundle} containing parameters for the started {@link Flow}.
     * @param <T>    The type extended from {@link Flow}.
     * @return A {@link Flow}.
     */
    public static <T extends Flow> T startFlow(final T flow, final Bundle params) {

        final FlowManager flowManager = D.get(FlowManager.class);

        flow.setFlowManager(flowManager);
        flow.start(params);
        flowManager.mActiveFlow = flow;
        return flow;
    }

    /**
     * Invoked to handle Back Pressed event received by the {@link FlowFragmentContainer}.
     *
     * @return A {@code boolean} value indicating if the event was consumed by this method.
     */
    public boolean onBackPressed() {

        boolean wasEventConsumed = false;

        if (mActiveFlow != null) {
            if (mActiveFlow.isBackPressedEventHandler()) {
                final boolean canGoBack = mActiveFlow.canGoBack();

                if (canGoBack) {
                    mActiveFlow.goBack();
                    wasEventConsumed = true;
                } else {
                    mActiveFlow.clearBackStack();
                }
            }
        }

        return wasEventConsumed;
    }

    /**
     * Invoked by a {@link Flow#stop()} when the {@link Flow}} has been stopped.
     *
     * @param flow A {@link Flow}. May not be {@code null}.
     */
    public void onFlowStopped(final Flow flow) {
        if (mActiveFlow == flow) {
            mActiveFlow = null;
        }
    }

    /**
     * Invoked by a {@link Flow#destroy()} when the {@link Flow}} has been destroyed.
     *
     * @param flow A {@link Flow}. May not be {@code null}.
     */
    public void onFlowDestroyed(final Flow flow) {
        if (mActiveFlow == flow) {
            mActiveFlow = null;
        }
    }
}
