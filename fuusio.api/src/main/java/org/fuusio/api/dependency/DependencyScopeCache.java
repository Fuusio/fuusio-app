package org.fuusio.api.dependency;

import org.fuusio.api.app.FuusioApplication;

import java.util.HashMap;

/**
 * {@link DependencyScopeCache} is used to saving and restoring {@link DependencyScope}s for
 * their {@link DependencyScopeOwner}s.
 */
public class DependencyScopeCache {

    private final HashMap<Class<? extends DependencyScopeOwner>, DependencyScope> mSavedScopes;

    private FuusioApplication mApplication;

    public DependencyScopeCache(final FuusioApplication application) {
        mApplication = application;
        mSavedScopes = new HashMap<>();
    }

    public void saveDependencyScope(final DependencyScopeOwner owner, final DependencyScope scope) {
        final Class<? extends DependencyScopeOwner> ownerClass = owner.getClass();
        mSavedScopes.put(ownerClass, scope);
    }

    public DependencyScope getDependencyScope(final DependencyScopeOwner owner) {
        final Class<? extends DependencyScopeOwner> ownerClass = owner.getClass();
        return mSavedScopes.get(ownerClass);
    }

    public DependencyScope removeDependencyScope(final DependencyScopeOwner owner) {
        final Class<? extends DependencyScopeOwner> ownerClass = owner.getClass();
        return mSavedScopes.remove(ownerClass);
    }
}
