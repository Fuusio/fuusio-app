package org.fuusio.app.feature.test.view;

import org.fuusio.app.feature.test.presenter.TestPresenter1;
import org.fuusio.app.feature.test.presenter.TestPresenter2;

import org.fuusio.api.mvp.View;

public interface TestView2 extends View<TestPresenter2> {

    void showToast(String pMessage);
}
