package org.fuusio.app.feature.test.view;

import org.fuusio.app.feature.test.presenter.TestPresenter1;

import org.fuusio.api.mvp.View;

public interface TestView1 extends View<TestPresenter1> {

    void showToast(String pMessage);
}
