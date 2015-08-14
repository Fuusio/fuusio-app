package org.fuusio.app.feature.test.flow;

import org.fuusio.app.feature.test.presenter.TestPresenter1;
import org.fuusio.app.feature.test.view.TestView1;
import org.fuusio.app.feature.test.view.TestView2;

import org.fuusio.api.dependency.D;
import org.fuusio.api.flow.AbstractFlow;
import org.fuusio.api.flow.FlowScope;
import org.fuusio.api.flow.FlowFragmentContainer;
import org.fuusio.api.mvp.Presenter;

public class TestFlowImpl extends AbstractFlow implements TestFlow {

    public TestFlowImpl(final FlowFragmentContainer pContainer) {
        super(pContainer);
    }

    @Override
    protected FlowScope createDependencyScope() {
        return new TestScope(this);
    }

    @Override
    public void onStart() {
        activateView(D.get(TestView1.class));
    }

    @Override
    public void onPresenterStopped(final Presenter pPresenter) {

        if (pPresenter instanceof TestPresenter1) {
            activateView(D.get(TestView2.class));
        } else {
            activateView(D.get(TestView1.class));
        }

    }
}
