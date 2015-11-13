package org.fuusio.app.feature.test.presenter;

import org.fuusio.api.dependency.D;
import org.fuusio.api.mvp.AbstractPresenter;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.app.feature.test.view.TestView1;

public class TestPresenter1Impl extends AbstractPresenter<TestView1, Presenter.Listener> implements TestPresenter1 {

    public TestPresenter1Impl() {
        mView = D.get(TestView1.class, this);
    }

    public void onButtonClicked() {
        mView.showToast("Moving to Test View 2");
        stop();
    }

}
