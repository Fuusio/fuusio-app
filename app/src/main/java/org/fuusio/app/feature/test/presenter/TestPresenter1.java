package org.fuusio.app.feature.test.presenter;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.app.feature.test.view.TestView1;

public interface TestPresenter1 extends Presenter<TestView1, Presenter.Listener> {

    void onButtonClicked();
}
