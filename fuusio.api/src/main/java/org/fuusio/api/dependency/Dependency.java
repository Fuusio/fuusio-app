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

import java.util.HashMap;

/**
 * {@link Dependency} provides an API to pull and create dependencies. It also provides a static
 * API for managing {@link DependencyScope} instances, such as methods for activating and deactivating
 * {@link DependencyScope} instances.
 */
public class Dependency {

    /**
     * A {@link HashMap} containing the activated {@link DependencyScope}s using method
     * {@link Dependency#activateScope(ScopeProvider)}. Even if there can be multiple
     * activated {@link DependencyScope}s only one of them can be the active one.
     */
    private final static HashMap<ScopeProvider, DependencyScope> sDependencyScopes = new HashMap<>();

    /**
     * The currently active {@link DependencyScope}.
     */
    private static DependencyScope sActiveScope = null;

    /**
     * Adds the {@link DependencyScope} managed by the given {@link ScopeProvider} to
     * the map of current {@link DependencyScope}s.
     * @param pProvider A {@link ScopeProvider}
     * @return A {@link DependencyScope}.
     */
    public static DependencyScope addScope(final ScopeProvider pProvider) {

        final DependencyScope scope = pProvider.getDependencyScope();
        sDependencyScopes.put(pProvider, scope);
        scope.addDependant(pProvider);
        return scope;
    }

    /**
     * Gets a {@link DependencyScope} managed by the given {@link ScopeProvider}.
     * @param pProvider A {@link ScopeProvider}.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return A {@link DependencyScope}. May return {@code null}.
     */
    public static <T extends DependencyScope> T getScope(final ScopeProvider pProvider) {
        return (T)sDependencyScopes.get(pProvider);
    }

    /**
     * Gets the {@link DependencyScope} that is set to be currently the active one. Note that only
     * one {@link DependencyScope} can be active at any given.
     * @return A {@link DependencyScope}. May return {@code null} if no {@link DependencyScope} is
     * set to be active.
     */
    public static DependencyScope getActiveScope() {
        return sActiveScope;
    }

    /**
     * Activates a {@link DependencyScope} for the given {@link ScopeProvider}.
     * The active {@link DependencyScope} is used for resolving dependencies. Setting the currently
     * active {@link DependencyScope} is not thread safe. Therefore this method can be invoked
     * only from the Main UI thread.
     * @param pProvider A {@link ScopeProvider}.
     */
    public static void activateScope(final ScopeProvider pProvider) {

        DependencyScope scope = sDependencyScopes.get(pProvider);

        if (scope == null) {
            scope = addScope(pProvider);
            scope.setProvider(pProvider);
        }

        if (sActiveScope != scope) {

            if (sActiveScope != null) {
                deactivateScope(sActiveScope.getProvider());
            }

            if (scope != null) {
                sActiveScope = scope;
                sActiveScope.onActivated(pProvider);
            } else {
                sActiveScope = null;
            }
        }
    }

    /**
     * Deactivates a {@link DependencyScope} managed by the given {@link ScopeProvider}.
     * When a {@link DependencyScope} is deactivated it is also disposed if disposing is allowed
     * for the deactivated {@link DependencyScope}.
     * @param pProvider A {@link ScopeProvider}.
     */
    public static void deactivateScope(final ScopeProvider pProvider) {

        final DependencyScope scope = pProvider.getDependencyScope();

        if (scope.isDisposable()) {
            sDependencyScopes.remove(pProvider);
            scope.dispose();
        }

        scope.onDeactivated(pProvider);

        if (scope == sActiveScope) {
            sActiveScope = null;
        }
    }

    /**
     * Gets a requested dependency of the specified type. The dependency is requested from
     * the currently active {@link DependencyScope}.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The requested dependency. If {@code null} is returned, it indicates an error in
     * an {@link DependencyScope} implementation.
     */
    public static <T> T get(final Class<T> pDependencyType) {
        return sActiveScope.getDependency(pDependencyType, null, false);
    }

    /**
     * Gets a requested dependency of the specified type. The dependency is requested from
     * the currently active {@link DependencyScope}.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param pDependant The object requesting the requested. This parameter is required when the requesting object
     *                is also a requested within the object graph represented by the active {@link Dependency}.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The requested dependency. If {@code null} is returned, it indicates an error in
     * an {@link DependencyScope} implementation.
     */
    public static <T> T get(final Class<T> pDependencyType,final Object pDependant) {
        return sActiveScope.getDependency(pDependencyType, pDependant, false);
    }

    /**
     * Gets a requested dependency of the specified type. The dependency is requested from
     * the currently active {@link DependencyScope}. If no requested dependency instance exists,
     * a new instance is created.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The requested dependency. If {@code null} is returned, it indicates an error in
     * an {@link DependencyScope} implementation.
     */
    public static <T> T getOrCreate(final Class<T> pDependencyType) {
        return sActiveScope.getDependency(pDependencyType, null, true);
    }

    /**
     * Gets a requested dependency of the specified type. The dependency is requested from
     * the currently active {@link DependencyScope}. If no requested dependency instance exists,
     * a new instance is created.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param pDependant The object requesting the requested. This parameter is required when the requesting object
     *                is also a requested within the object graph represented by the active {@link Dependency}.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The requested dependency. If {@code null} is returned, it indicates an error in
     * an {@link DependencyScope} implementation.
     */
    public static <T> T getOrCreate(final Class<T> pDependencyType, final Object pDependant) {
        return sActiveScope.getDependency(pDependencyType, pDependant, true);
    }

    /**
     * Creates a new instance of the requested dependency type. The currently active
     * {@link DependencyScope} is requested to create the instance.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The requested dependency. If {@code null} is returned, it indicates an error in
     * an {@link DependencyScope} implementation.
     */
    public static <T> T create(final Class<T> pDependencyType) {
        return sActiveScope.createDependency(pDependencyType, null);
    }

    /**
     * Creates a new instance of the requested dependency type. The currently active
     * {@link DependencyScope} is requested to create the instance.
     * @param pDependencyType A {@link Class} specifying the type of the requested dependency.
     * @param pDependant The object requesting the requested. This parameter is required when the requesting object
     *                is also a requested within the object graph represented by the active {@link Dependency}.
     * @param <T> A type parameter for casting the requested dependency to expected type.
     * @return The requested dependency. If {@code null} is returned, it indicates an error in
     * an {@link DependencyScope} implementation.
     */
    public static <T> T create(final Class<T> pDependencyType, final Object pDependant) {
        return sActiveScope.createDependency(pDependencyType, pDependant);
    }
}
