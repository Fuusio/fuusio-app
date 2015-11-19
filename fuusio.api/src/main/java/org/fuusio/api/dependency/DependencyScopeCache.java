package org.fuusio.api.dependency;

import org.fuusio.api.app.FuusioApplication;

import java.util.HashMap;

/**
 * {@link DependencyScopeCache} is used to saving and restoring {@link DependencyScope}s for
 * their {@link DependencyScopeOwner}s.
 */
public class DependencyScopeCache {

    private final HashMap<String, DependencyScope> mSavedScopes;

    private FuusioApplication mApplication;

    public DependencyScopeCache(final FuusioApplication application) {
        mApplication = application;
        mSavedScopes = new HashMap<>();
    }

    public void saveDependencyScope(final DependencyScopeOwner owner, final DependencyScope scope) {
        mSavedScopes.put(owner.getScopeId(), scope);
    }

    public DependencyScope getDependencyScope(final DependencyScopeOwner owner) {
        return mSavedScopes.get(owner.getScopeId());
    }

    public DependencyScope removeDependencyScope(final DependencyScopeOwner owner) {
        return mSavedScopes.remove(owner.getScopeId());
    }

    public boolean containsDependencyScope(final DependencyScopeOwner owner) {
        return mSavedScopes.containsKey(owner.getScopeId());
    }
}
