package org.fuusio.app.feature.test.presenter;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.app.feature.test.view.TestView2;

import org.fuusio.api.dependency.D;
import org.fuusio.api.mvp.AbstractPresenter;

public class TestPresenter2Impl extends AbstractPresenter<TestView2, Presenter.Listener> implements TestPresenter2 {

    public TestPresenter2Impl() {
        mView = D.get(TestView2.class, this);
    }

    public void onButtonClicked() {
        mView.showToast("Moving to Test View 1");
        stop();
    }

}
