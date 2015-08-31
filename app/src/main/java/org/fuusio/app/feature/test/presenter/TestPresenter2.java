package org.fuusio.app.feature.test.presenter;

import org.fuusio.app.feature.test.view.TestView1;
import org.fuusio.app.feature.test.view.TestView2;

import org.fuusio.api.mvp.Presenter;

public interface TestPresenter2 extends Presenter<TestView2, Presenter.Listener> {

    void onButtonClicked();
}
