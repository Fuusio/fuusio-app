package org.fuusio.app.feature.home;

import org.fuusio.app.feature.home.presenter.HomePresenter;
import org.fuusio.app.feature.home.presenter.HomePresenterImpl;
import org.fuusio.app.feature.home.view.HomeActivity;
import org.fuusio.app.feature.home.view.HomeView;

import org.fuusio.api.dependency.Dependency;
import org.fuusio.api.dependency.DependencyScope;

public class HomeDependencyScope extends DependencyScope {

    private final HomeActivity mActivity;

    public HomeDependencyScope(final HomeActivity pActivity) {
        mActivity = pActivity;
    }

    @Override
    protected <T> T getDependency() {

        if (type(HomePresenter.class)) {
            return dependency(new HomePresenterImpl());
        } else if (type(HomeView.class)) {
            return dependency(mActivity);
        }

        return null;
    }
}
