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
     * A reference to an optional overriding {@link DependencyScope} used providing mock dependencies.
     */
    protected DependencyScope mMockScope;

    /**
     * The {@link DependencyScopeOwner} that manages this {@link DependencyScope}.
     */
    private DependencyScopeOwner mOwner;

    /**
     * A reference to an optional parent {@link DependencyScope}.
     */
    protected DependencyScope mParentScope;

    /**
     * A helper field for storing the currently requested type of dependency.
     */
    private Class mDependencyType;

    protected DependencyScope() {
        mDependencies = new HashMap<>();
        mDependants = new ArrayList<>();
    }

    /**
     * Get the {@link DependencyScopeOwner} that manages the lifecycle this {@link DependencyScope}.
     *
     * @return A {@link DependencyScopeOwner}.
     */
    public final DependencyScopeOwner getOwner() {
        return mOwner;
    }

    /**
     * Set the {@link DependencyScopeOwner} that manages the lifecycle this {@link DependencyScope}.
     *
     * @param owner A {@link DependencyScopeOwner}.
     */
    protected void setOwner(final DependencyScopeOwner owner) {
        mOwner = owner;
    }

    /**
     * Gets the parent {@link DependencyScope}.
     *
     * @return A {@link DependencyScope}. May return {@code null} if the parent is not set.
     */
    public final DependencyScope getParentScope() {
        return mParentScope;
    }

    /**
     * Sets the parent {@link DependencyScope}.
     *
     * @param parent A {@link DependencyScope}. May be {@code null}.
     */
    public void setParentScope(final DependencyScope parent) {
        mParentScope = parent;
    }

    /**
     * Gets the {@link DependencyScope} that overrides this {@link DependencyScope}. An overriding
     * {@link DependencyScope} can be used for providing mock dependencies for unit tests.
     *
     * @return A {@link DependencyScope}. May return {@code null}.
     */
    public final DependencyScope getMockedScope() {
        return mMockScope;
    }

    /**
     * Sets the given {@link DependencyScope} to override this {@link DependencyScope}. An overriding
     * {@link DependencyScope} can be used for providing mock dependencies for unit tests.
     *
     * @param scope A {@link DependencyScope}.
     */
    public void setMockedScope(final DependencyScope scope) {
        mMockScope = scope;
    }

    /**
     * Tests if this {@link DependencyScope} can be disposed.
     *
     * @return A {@code boolean} value.
     */
    public boolean isDisposable() {
        return true;
    }

    /**
     * Adds the given {@link Object} as a dependant to this {@link DependencyScope}.
     *
     * @param dependant An {@link Object}.
     */
    public void addDependant(final Object dependant) {
        mDependants.add(dependant);
    }

    /**
     * Tests if the specified type represents the requested dependency type.
     *
     * @param dependencyType A {@link Class} specifying the type of the requested dependency.
     * @return A {@code boolean} value.
     */
    @SuppressWarnings("unchecked")
    protected boolean type(final Class<?> dependencyType) {
        return mDependencyType.isAssignableFrom(dependencyType);
    }

    /**
     * Caches the requested dependency {@link Object} before it is returned to a requester.
     *
     * @param dependency The requested dependency {@link Object} to be cached.
     * @param <T>        The generic return type of the cached dependency.
     * @return The cached dependency {@link Object}.
     */
    @SuppressWarnings("unchecked")
    protected <T> T dependency(final Object dependency) {
        if (dependency != null) {
            if (mDependencyType != null) {
                if (mDependencyType.isInstance(dependency)) {
                    return (T) cache(mDependencyType, dependency);
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
     *
     * @param dependencyType The dependence type as a {@link Class} used as a key.
     * @param dependency     The requested dependency {@link Object} to be cached.
     * @param <T>            The generic return type of the cached dependency.
     * @return The cached dependency {@link Object}.
     */
    @SuppressWarnings("unchecked")
    public <T> T cache(final Class<T> dependencyType, final Object dependency) {
        if (dependency != null) {
            mDependencies.put(dependencyType, dependency);
            return (T) dependency;
        } else {
            throw new IllegalArgumentException("Parameter 'dependency' may not be null");
        }
    }

    /**
     * This method is meant to be implemented by each concrete implementation of {@link DependencyScope}.
     * The requested dependency instance is returned by the implementation, if it is capable of providing
     * a such instance. The implementation of this method should not delegate the request to any other
     * {@link DependencyScope}. If the implementation is not able to cache the requested instance,
     * the design contract is to return {@code null} instead.
     *
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
     *
     * @param dependencyType A {@link Class} specifying the type of the requested instance.
     * @param dependant      The requesting object. This parameter is required when the requesting dependant
     *                       is also a requested within the object graph represented by a {@link DependencyScope}.
     * @param <T>            A type parameter for casting the requested instance to expected type.
     * @param pCreateNew     A {@code boolean} parameter specifying if an instance of the requested
     *                       type should be automatically created.
     * @return The returned instance. If {@code null} is returned it indicates an error in
     * the implementation of the {@link DependencyScope}.
     */
    @SuppressWarnings("unchecked")
    protected <T> T getDependency(final Class<T> dependencyType, final Object dependant, final boolean pCreateNew) {

        final Class savedDependencyType = mDependencyType;

        mDependencyType = dependencyType;

        if (dependant != null) {
            mDependants.add(dependant);
        }

        T dependency = null;

        if (!pCreateNew) {
            dependency = (T) mDependencies.get(dependencyType);
        }

        if (dependency == null) {
            dependency = lookDependencyAmongDependants(dependencyType);

            if (dependency == null) {

                if (mMockScope != null) {
                    dependency = mMockScope.getDependency(dependencyType, dependant, pCreateNew);
                } else {
                    dependency = getDependency();
                }

                if (dependency == null) {

                    if (mParentScope != null) {
                        dependency = mParentScope.getDependency(dependencyType, null, pCreateNew);
                    }

                    if (dependency == null && !(this instanceof ApplicationScope)) {
                        dependency = ApplicationScope.getInstance().getDependency(dependencyType, dependant, pCreateNew);
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
     *
     * @param dependencyType A {@link Class} specifying the type of the requested instance.
     * @param dependant      The requesting object. This parameter is required when the requesting dependant
     *                       is also a requested within the object graph represented by a {@link DependencyScope}.
     * @param <T>            A type parameter for casting the requested instance to expected type.
     * @return The returned instance. If {@code null} is returned it indicates an error in
     * the implementation of the {@link DependencyScope}.
     */
    @SuppressWarnings("unchecked")
    protected <T> T createDependency(final Class<T> dependencyType, final Object dependant) {

        final Class savedDependencyType = mDependencyType;

        mDependencyType = dependencyType;

        if (dependant != null) {
            mDependants.add(dependant);
        }

        T dependency = createDependency();

        if (dependency == null) {

            if (mParentScope != null) {
                dependency = mParentScope.createDependency(dependencyType, dependant);
            }

            if (dependency == null && !(this instanceof ApplicationScope)) {
                dependency = ApplicationScope.getInstance().createDependency(dependencyType, null);
            }
        }

        mDependencyType = savedDependencyType;
        return dependency;
    }

    /**
     * This method is meant to be implemented by each concrete implementation of {@link DependencyScope}.
     * The requested dependency instance is created and returned by the implementation.
     *
     * @param <T> A type parameter for casting the requested instance to expected type.
     * @return The created instance of the requested dependency type. May not return {@code null}.
     */
    protected <T> T createDependency() {
        return null;
    }

    /**
     * Checks it the requested dependency is one of the cached dependent instances.
     *
     * @param dependencyType A {@link Class} specifying the type of the requested dependency.
     * @param <T>            A type parameter for casting the requested dependency to expected type.
     * @return The found requested instance or {@code null}.
     */
    private <T> T lookDependencyAmongDependants(final Class<T> dependencyType) {

        for (int i = mDependants.size() - 1; i >= 0; i--) {
            final Object dependant = mDependants.get(i);

            if (dependencyType.isAssignableFrom(dependant.getClass())) {
                mDependants.remove(dependant);
                return cache(dependencyType, dependant);
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
        mOwner = null;
        mDependencyType = null;

        if (mMockScope != null) {
            mMockScope.dispose();
            mMockScope = null;
        }
    }

    /**
     * This method is invoked by {@link Dependency} when this {@link DependencyScope} is activated
     * for the given {@link DependencyScopeOwner}.
     *
     * @param owner A {@link DependencyScopeOwner}.
     */
    public void onActivated(final DependencyScopeOwner owner) {
        // By default do nothing
    }

    /**
     * This method is invoked by {@link Dependency} when this {@link DependencyScope} is deactivated
     * for the given {@link DependencyScopeOwner}.
     *
     * @param owner A {@link DependencyScopeOwner}.
     */
    public void onDeactivated(final DependencyScopeOwner owner) {
        // By default do nothing
    }

}
