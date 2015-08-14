package org.fuusio.app.feature.home.presenter;

import org.fuusio.app.feature.home.view.HomeView;

import org.fuusio.api.mvp.AbstractPresenter;

public class HomePresenterImpl extends AbstractPresenter<HomeView> implements HomePresenter {

    public HomePresenterImpl(final HomeView pView) {
        super(pView);
    }
}
