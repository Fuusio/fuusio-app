package org.fuusio.app.feature.test.view;

import org.fuusio.api.mvp.View;
import org.fuusio.app.feature.test.presenter.TestPresenter2;

public interface TestView2 extends View<TestPresenter2> {

    void showToast(String pMessage);
}
