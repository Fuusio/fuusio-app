package org.fuusio.app.feature.test.presenter;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.app.feature.test.view.TestView2;

public interface TestPresenter2 extends Presenter<TestView2, Presenter.Listener> {

    void onButtonClicked();
}
