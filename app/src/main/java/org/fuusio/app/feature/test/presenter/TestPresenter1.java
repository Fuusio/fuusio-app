package org.fuusio.app.feature.test.presenter;

import org.fuusio.app.feature.test.view.TestView1;

import org.fuusio.api.mvp.Presenter;

public interface TestPresenter1 extends Presenter<TestView1> {

    void onButtonClicked();
}
