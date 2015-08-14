package org.fuusio.app;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import org.fuusio.api.flow.Flow;
import org.fuusio.api.flow.FlowFragment;
import org.fuusio.api.flow.FlowFragmentContainer;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.ViewActivity;

public abstract class FuusioBaseActivity<T_Presenter extends Presenter> extends ViewActivity<T_Presenter> implements FlowFragmentContainer {

    protected ActionBar mActionBar;
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected CharSequence mTitle;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(final Bundle pInState) {
        super.onCreate(pInState);
        mTitle = getTitle();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTitle(final CharSequence pTitle) {
        mTitle = pTitle;
        mToolbar.setTitle(mTitle);
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = getView(R.id.toolbar);
            setSupportActionBar(mToolbar);
        }
        return mToolbar;
    }

    @Override
    public void showFlowFragment(final Flow pFlow, final FlowFragment pFragment, final String pFragmentTag) {
        final FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_container, pFragment, pFragment.getFlowTag())
                .commit();
    }

}
