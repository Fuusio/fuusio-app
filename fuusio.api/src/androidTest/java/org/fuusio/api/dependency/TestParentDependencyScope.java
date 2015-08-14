package org.fuusio.api.dependency;

public class TestParentDependencyScope extends DependencyScope {

    protected TestParentDependencyScope() {
    }

    @Override
    protected <T> T getDependency() {

        if (type(Foo.class)) {
            return dependency(new Foo());
        }

        return null;
    }


    @Override
    public boolean isDisposable() {
        return false;
    }

    public class Foo {
    }
}