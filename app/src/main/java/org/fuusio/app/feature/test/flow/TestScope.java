package org.fuusio.app.feature.test.flow;

import org.fuusio.api.flow.FlowScope;
import org.fuusio.app.feature.test.presenter.TestPresenter1;
import org.fuusio.app.feature.test.presenter.TestPresenter1Impl;
import org.fuusio.app.feature.test.presenter.TestPresenter2;
import org.fuusio.app.feature.test.presenter.TestPresenter2Impl;
import org.fuusio.app.feature.test.view.TestFragment1;
import org.fuusio.app.feature.test.view.TestFragment2;
import org.fuusio.app.feature.test.view.TestView1;
import org.fuusio.app.feature.test.view.TestView2;

public class TestScope extends FlowScope<TestFlow> {

    protected TestScope(final TestFlow pFlow) {
        super(pFlow);
    }

    @Override
    protected <T> T getDependency() {

        if (type(TestPresenter1.class)) {
            return dependency(new TestPresenter1Impl());
        } else if (type(TestPresenter2.class)) {
            return dependency(new TestPresenter2Impl());
        } else if (type(TestFlow.class)) {
            return dependency(getFlow());
        } else if (type(TestView1.class)) {
            return dependency(new TestFragment1());
        } else if (type(TestView2.class)) {
            return dependency(new TestFragment2());
        }
        return null;
    }
}
