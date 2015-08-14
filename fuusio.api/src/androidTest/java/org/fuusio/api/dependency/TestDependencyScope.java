package org.fuusio.api.dependency;

import org.fuusio.api.flow.FlowScope;

import org.mockito.Mockito;

public class TestDependencyScope extends FlowScope<TestFlow> {

    private boolean mWasDisposed;

    protected TestDependencyScope(final TestFlow pFlow) {
        super(pFlow);
    }

    public final boolean wasDisposed() {
        return mWasDisposed;
    }

    public void setWasDisposed(final boolean pDisposed) {
        mWasDisposed = pDisposed;
    }

    @Override
    protected void dispose() {
        super.dispose();
        mWasDisposed = true;
    }

    @Override
    protected <T> T getDependency() {

        if (type(TestFlow.class)) {
            return dependency(mFlow);
        } else if (type(TestView.class)) {
            return dependency(Mockito.mock(TestView.class));
        } else if (type(TestPresenter.class)) {
            return dependency(Mockito.mock(TestPresenter.class));
        }

        return null;
    }

    public boolean isCleared() {
        return mDependants.isEmpty() && mDependencies.isEmpty();
    }
}