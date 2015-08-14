package org.fuusio.app.feature.test.presenter;

import org.fuusio.app.feature.test.view.TestView1;

import org.fuusio.api.dependency.D;
import org.fuusio.api.mvp.AbstractPresenter;

public class TestPresenter1Impl extends AbstractPresenter<TestView1> implements TestPresenter1 {

    public TestPresenter1Impl() {
        mView = D.get(TestView1.class, this);
    }

    public void onButtonClicked() {
        mView.showToast("Moving to Test View 2");
        stop();
    }

}
