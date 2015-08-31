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
package org.fuusio.api.dependency;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * {@link DependencyScope} provides an abstract base class for implementing objects that act
 * as scoped object graphs for group of objects that have dependency relationships. A concrete
 * implementation of {@link DependencyScope}. Instances of {@link DependencyScope} should not be
 * used directly to access dependencies, but via class {@link Dependency} (or convenience class
 * {@link D}) that provides a static methods for using the currently active {@link DependencyScope}.
 */
public abstract class DependencyScope {

    /**
     * A cache of dependants that have request dependencies. These dependants are cached because
     * they can also be dependencies for other objects.
     */
    protected final ArrayList<Object> mDependants;

    /**
     * A cache of dependencies.
     */
    protected final HashMap<Class, Object> mDependencies;

    /**
     * A reference to an optional parent {@link DependencyScope}.
     */
    protected DependencyScope mParentScope;

    /**
     * A reference to an optional overriding {@link DependencyScope} used providing mock dependencies.
     */
    protected DependencyScope mMockedScope;

    /**
     * The {@link ScopeManager} that manages this {@link DependencyScope}.
     */
    private ScopeManager mScopeManager;

    /**
     * A helper field for storing the currently requested type of dependency.
     */
    private Class mDependencyType;

    protected DependencyScope() {
        mDependencies = new HashMap<>();
        mDependants = new ArrayList<>();
    }

    /**
     * Get the {@link ScopeManager} that manages the lifecycle this {@link DependencyScope}.
     * @return A {@link ScopeManager}.
     */
    public final ScopeManager getManager() {
        return mScopeManager;
    }

    /**
     * Set the {@link ScopeManager} that manages the lifecycle this {@link DependencyScope}.
     * @param pManager A {@link ScopeManager}.
     */
    protected void setManager(final ScopeManager pManager) {
        mScopeManager = pManager;
    }

    /**
     * Gets the parent {@link DependencyScope}.
     * @return A {@link DependencyScope}. May return {@code null} if the parent is not set.
     */
    public final DependencyScope getParentScope() {
        return mParentScope;
    }

    /**
     * Sets the parent {@link DependencyScope}.
     * @param pParent A {@link DependencyScope}. May be {@code null}.
     */
    public void setParentScope(final DependencyScope pParent) {
        mParentScope = pParent;
    }

    /**
     * Gets the {@link DependencyScope} that overrides this {@link DependencyScope}. An overriding
     * {@link DependencyScope} can be used for providing mock dependencies for unit tests.
     * @return A {@link DependencyScope}. May return {@code null}.
     */
    public final DependencyScope getMockedScope() {
        return mMockedScope;
    }

    /**
     * Sets the given {@link DependencyScope} to override this {@link DependencyScope}. An overriding
     * {@link DependencyScope} can be used for providing mock dependencies for unit tests.
     * @param pScope A {@link DependencyScope}.
     */
    public void setMockedScope(final DependencyScope pScope) {
        mMockedScope = pScope;
    }

    /**
     * Tests if this {@link DependencyScope} can be disposed.
     * @return A {@code boolean} value.
     */
    public boolean isDisposable() {
        return true;
    }

    /**
     * Adds the given {@link Object} as a dependant to this {@link DependencyScope}.
     * @param pDependant An {@link Object}.
     */
    public void addDependant(final Object pDependant) {
        mDependants.add(pDependant);
    }

    /**
     * Tests if the specified type represents the requested dependency type.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @return A {@code boolean} value.
     */
    @SuppressWarnings("unchecked")
    protected boolean type(final Class<?> pDependencyType) {
        return mDependencyType.isAssignableFrom(pDependencyType);
    }

    /**
     * Caches the requested dependency {@link Object} before it is returned to a requester.
     * @param pDependency The requested dependency {@link Object} to be cached.
     * @param <T> The generic return type of the cached dependency.
     * @return The cached dependency {@link Object}.
     */
    @SuppressWarnings("unchecked")
    protected <T> T dependency(final Object pDependency) {
        if (pDependency != null) {
            if (mDependencyType != null) {
                if (mDependencyType.isInstance(pDependency)) {
                    return (T) cache(mDependencyType, pDependency);
                } else {
                    throw new IllegalStateException("The given dependency object is not an instance of: " + mDependencyType.getName());
                }
            } else {
                throw new IllegalStateException("The method DependencyScope#dependency(Object) is meant to be used only in method DependencyScope#getDependency()");
            }
        } else {
            throw new IllegalArgumentException("Parameter may not be null");
        }
    }

    /**
     * Caches the given requested dependency {@link Object} using the requested type as a key.
     * @param pDependencyType The dependence type as a {@link Class} used as a key.
     * @param pDependency The requested dependency {@link Object} to be cached.
     * @param <T> The generic return type of the cached dependency.
     * @return The cached dependency {@link Object}.
     */
    @SuppressWarnings("unchecked")
    public <T> T cache(final Class<T> pDependencyType, final Object pDependency) {
        if (pDependency != null) {
            mDependencies.put(pDependencyType, pDependency);
            return (T) pDependency;
        } else {
            throw new IllegalArgumentException("Parameter 'pDependency' may not be null");
        }
    }

    /**
     * This method is meant to be implemented by each concrete implementation of {@link DependencyScope}.
     * The requested dependency instance is returned by the implementation, if it is capable of providing
     * a such instance. The implementation of this method should not delegate the request to any other
     * {@link DependencyScope}. If the implementation is not able to cache the requested instance,
     * the design contract is to return {@code null} instead.
     * @param <T> A type parameter for casting the requested instance to expected type.
     * @return The requested dependency instance if is this {@link DependencyScope} implementation is
     * capable of providing such instance otherwise {@code null}.
     */
    protected abstract <T> T getDependency();

    /**
     * Gets an dependency instance of the specified type. The requested dependency is first searched
     * from the currently active {@link DependencyScope}. If a requested instance is not found the search
     * is delegated to parent {@link DependencyScope}. As a last attempt,
     * the {@link ApplicationScope} is searched.
     * @param pDependencyType A {@link Class} specifying the type of the requested instance.
     * @param pDependant The requesting object. This parameter is required when the requesting dependant
     *                is also a requested within the object graph represented by a {@link DependencyScope}.
     * @param <T> A type parameter for casting the requested instance to expected type.
     * @param pCreateNew A {@code boolean} parameter specifying if an instance of the requested
     *                   type should be automatically created.
     * @return The returned instance. If {@code null} is returned it indicates an error in
     * the implementation of the {@link DependencyScope}.
     */
    @SuppressWarnings("unchecked")
    protected <T> T getDependency(final Class<T> pDependencyType, final Object pDependant, final boolean pCreateNew) {

        final Class savedDependencyType = mDependencyType;

        mDependencyType = pDependencyType;

        if (pDependant != null) {
            mDependants.add(pDependant);
        }

        T dependency = null;

        if (!pCreateNew) {
            dependency = (T) mDependencies.get(pDependencyType);
        }

        if (dependency == null) {
            dependency = lookDependencyAmongDependants(pDependencyType);

            if (dependency == null) {

                if (mMockedScope != null) {
                    dependency = mMockedScope.getDependency(pDependencyType, pDependant, pCreateNew);
                } else {
                    dependency = getDependency();
                }

                if (dependency == null) {

                    if (mParentScope != null) {
                        dependency = mParentScope.getDependency(pDependencyType, null, pCreateNew);
                    }

                    if (dependency == null && !(this instanceof ApplicationScope)) {
                        dependency = ApplicationScope.getInstance().getDependency(pDependencyType, pDependant, pCreateNew);
                    }
                }

                if (dependency == null && pCreateNew) {
                    return createDependency();
                }
            }
        }

        mDependencyType = savedDependencyType;
        return dependency;
    }

    /**
     * Creates a new instance of the specified dependency type. The currently active
     * {@link DependencyScope} is first requested to create the new instance. If it does not create
     * instance, the requested is delegated to parent {@link DependencyScope}. As a last attempt,
     * the {@link ApplicationScope} is requested to create a new instance.
     * @param pDependencyType A {@link Class} specifying the type of the requested instance.
     * @param pDependant The requesting object. This parameter is required when the requesting dependant
     *                is also a requested within the object graph represented by a {@link DependencyScope}.
     * @param <T> A type parameter for casting the requested instance to expected type.
     * @return The returned instance. If {@code null} is returned it indicates an error in
     * the implementation of the {@link DependencyScope}.
     */
    @SuppressWarnings("unchecked")
    protected <T> T createDependency(final Class<T> pDependencyType, final Object pDependant) {

        final Class savedDependencyType = mDependencyType;

        mDependencyType = pDependencyType;

        if (pDependant != null) {
            mDependants.add(pDependant);
        }

        T dependency = createDependency();

        if (dependency == null) {

            if (mParentScope != null) {
                dependency = mParentScope.createDependency(pDependencyType, pDependant);
            }

            if (dependency == null && !(this instanceof ApplicationScope)) {
                dependency = ApplicationScope.getInstance().createDependency(pDependencyType, null);
            }
        }

        mDependencyType = savedDependencyType;
        return dependency;
    }

    /**
     * This method is meant to be implemented by each concrete implementation of {@link DependencyScope}.
     * The requested dependency instance is created and returned by the implementation.
     * @param <T> A type parameter for casting the requested instance to expected type.
     * @return The created instance of the requested dependency type. May not return {@code null}.
     */
    protected <T> T createDependency() {
        return null;
    }

    /**
     * Checks it the requested dependency is one of the cached dependent instances.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The found requested instance or {@code null}.
     */
    private <T> T lookDependencyAmongDependants(final Class<T> pDependencyType) {

        for (int i = mDependants.size() - 1; i >= 0; i--) {
            final Object dependant = mDependants.get(i);

            if (pDependencyType.isAssignableFrom(dependant.getClass())) {
                mDependants.remove(dependant);
                return cache(pDependencyType, dependant);
            }
        }
        return null;
    }

    /**
     * Disposes this {@link DependencyScope} to support effective GC and to avoid memory leaks.
     */
    protected void dispose() {
        mDependencies.clear();
        mDependants.clear();
        mParentScope = null;
        mScopeManager = null;
        mDependencyType = null;

        if (mMockedScope != null) {
            mMockedScope.dispose();
            mMockedScope = null;
        }
    }

    /**
     * This method is invoked by {@link Dependency} when this {@link DependencyScope} is activated
     * for the given {@link ScopeManager}.
     * @param pManager A {@link ScopeManager}.
     */
    public void onActivated(final ScopeManager pManager) {
        // By default do nothing
    }

    /**
     * This method is invoked by {@link Dependency} when this {@link DependencyScope} is deactivated
     * for the given {@link ScopeManager}.
     * @param pManager A {@link ScopeManager}.
     */
    public void onDeactivated(final ScopeManager pManager) {
        // By default do nothing
    }

}
