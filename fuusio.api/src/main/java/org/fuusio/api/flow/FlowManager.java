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

import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.Dependency;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.ScopeManager;

import java.lang.reflect.Constructor;

public class FlowManager {

    private static final String POSTFIX_IMPL = "Impl";

    private static FlowManager sInstance = null;
    private static ScopeManager sTestScopeManager = null;

    private Flow mActiveFlow;

    private FlowManager() {
    }

    /**
     * Gets the singleton of {@link FlowManager}.
     * @return A {@link FlowManager}.
     */
    public static FlowManager getInstance() {

        if (sInstance == null) {
            sInstance = new FlowManager();
        }
        return sInstance;
    }

    /**
     * Sets a {@link ScopeManager} that is used to provide a {@link DependencyScope} for testing
     * purposes.
     * @param pProvider A {@link ScopeManager}.
     */
    public static void setTestScopeProvider(final ScopeManager pProvider) {
        sTestScopeManager = pProvider;
    }

    /**
     * Gets an instance of {@link Flow} that is used a mock. Mock {@link Flow}s are made available
     * by setting a {@link DependencyScope} that provides them using method
     * @param pFlowClass A class specifying the {@link Flow}. The class may an interface or implementation
     *                   class as long as the implementation class name is the interface class name with
     *                   postfix {@code Impl}.
     * @return A {@link Flow}. May return {@code null}
     */
    private static <T extends Flow> T getMockFlow(final Class<T> pFlowClass) {
        T flow = null;

        if (sTestScopeManager != null) {
            final DependencyScope savedScope = D.getActiveScope();
            D.activateScope(sTestScopeManager);

            flow = D.get(pFlowClass);

            if (flow == null && pFlowClass.isInterface()) {
                final Class<T> implClass;
                try {
                    implClass = (Class<T>)Class.forName(pFlowClass.getName() + POSTFIX_IMPL);
                    flow = D.get(implClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            D.deactivateScope(sTestScopeManager);

            if (savedScope != null) {
                D.activateScope(savedScope.getManager());
            }
        }
        return flow;
    }

    /**
     * Gets the currently active {@link Flow}.
     * @return A {@link Flow}. May return {@code null} if there is no currently active {@link Flow}.
     */
    public Flow getActiveFlow() {
        return mActiveFlow;
    }

    /**
     * Creates the specified {@link Flow}, but does not start it. If the flow is
     * a {@link ScopeManager} its {@link FlowScope} is added to cache of
     * {@link DependencyScope}s.
     * @param pFlowClass A {@link Flow}
     * @param pContainer A {@link FlowFragmentContainer}.
     * @param <T>
     * @return A {@link Flow}.
     */
    @SuppressWarnings("unchecked")
    public <T extends Flow> T createFlow(final Class<T> pFlowClass, final FlowFragmentContainer pContainer) {

        T flow = getMockFlow(pFlowClass);

        if (flow == null) {
            Class<T> implClass = pFlowClass;

            try {
                if (pFlowClass.isInterface()) {
                    implClass = (Class<T>)Class.forName(pFlowClass.getName() + POSTFIX_IMPL);
                }

                final Class[] paramTypes = {FlowFragmentContainer.class};
                final Object[] paramValues = {pContainer};
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
     * Starts the specified {@link Flow} whose {@link android.app.Fragment}s are hosted by
     * the given {@link FlowFragmentContainer}.
     * @param pFlowClass A {@link Class} specifying the {@link Flow} to be created and started.
     * @param pContainer A {@link FlowFragmentContainer}.
     * @param <T> The generic type of started {@link Flow}.
     * @return A {@link Flow}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Flow> T startFlow(final Class<T> pFlowClass, final FlowFragmentContainer pContainer) {

        T flow = getMockFlow(pFlowClass);

        if (flow == null) {
            Class<T> implClass = pFlowClass;

            try {
                if (pFlowClass.isInterface()) {
                    implClass = (Class<T>) Class.forName(pFlowClass.getName() + POSTFIX_IMPL);
                }

                final Class[] paramTypes = {FlowFragmentContainer.class};
                final Object[] paramValues = {pContainer};
                final Constructor<T> constructor = implClass.getConstructor(paramTypes);
                flow = constructor.newInstance(paramValues);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        final FlowManager flowManager = D.get(FlowManager.class);
        return flowManager.startFlow(flow);
    }

    /**
     * Starts the given {@link Flow}.
     * @param pFlow {The {@link Flow} tostarted.
     * @param <T> The generic type of started {@link Flow}.
     * @return A {@link Flow}.
     */
    public static <T extends Flow> T startFlow(final T pFlow) {

        final FlowManager flowManager = D.get(FlowManager.class);

        pFlow.setFlowManager(flowManager);
        pFlow.start();
        flowManager.mActiveFlow = pFlow;
        return pFlow;
    }

    /**
     * Invoked to handle Back Pressed event received by the {@link FlowFragmentContainer}.
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
     * @param pFlow A {@link Flow}. May not be {@code null}.
     */
    public void onFlowStopped(final Flow pFlow) {
        if (mActiveFlow == pFlow) {
            mActiveFlow = null;
        }
    }

    /**
     * Invoked by a {@link Flow#destroy()} when the {@link Flow}} has been destroyed.
     * @param pFlow A {@link Flow}. May not be {@code null}.
     */
    public void onFlowDestroyed(final Flow pFlow) {
        if (mActiveFlow == pFlow) {
            mActiveFlow = null;
        }
    }
}
