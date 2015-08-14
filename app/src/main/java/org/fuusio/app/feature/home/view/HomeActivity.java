package org.fuusio.app.feature.home.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.DependencyScope;
import org.fuusio.api.dependency.ScopeManager;
import org.fuusio.api.flow.FlowManager;
import org.fuusio.app.FuusioBaseActivity;
import org.fuusio.app.R;
import org.fuusio.app.feature.home.HomeDependencyScope;
import org.fuusio.app.feature.home.presenter.HomePresenter;
import org.fuusio.app.feature.test.flow.TestFlow;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FuusioBaseActivity<HomePresenter> implements HomeView, ScopeManager {

    private HomeDependencyScope mScope;

    public HomeActivity() {
        D.activateScope(this);
    }

    @Override
    protected void onCreate(final Bundle pInState) {
        super.onCreate(pInState);

        setContentView(R.layout.activity_home);

        mToolbar = getToolbar();

        mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = getView(R.id.drawer_layout);
        mNavigationView = getView(R.id.navigation_view);

        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }

        /*
        final ViewPager viewPager = getView(R.id.view_pager); // TODO

        if (viewPager != null) {
            setupViewPager(viewPager);
        }*/

        final FloatingActionButton fab = getView(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        // tabLayout.setupWithViewPager(viewPager);

    /* TODO

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });

        // Inflate a menu to be displayed in the toolbar
        // TODO mToolbar.inflateMenu(R.menu.your_toolbar_menu);

        mNavigationDrawerLayout.setDrawerListener(mDrawerToggle);

        // REMOVE mToolbar.setDisplayHomeAsUpEnabled(true);
        // REMOVE mToolbar.setHomeButtonEnabled(true);
*/
        mPresenter = D.get(HomePresenter.class, this);
    }

    @Override
    public DependencyScope getDependencyScope() {
        if (mScope == null) {
            mScope = new HomeDependencyScope(this);
        }
        return mScope;
    }


    @Override
    protected void onStart() {
        super.onStart();

        FlowManager.startFlow(TestFlow.class, this);
        // OPTION FlowManager.startFlow(D.get(TestFlow.class, this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        D.deactivateScope(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, pMenu);
        return super.onCreateOptionsMenu(pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(final NavigationView pNavigationView) {
        pNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem pItem) {
                        pItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        final int itemId = pItem.getItemId();

                        switch (itemId) {
                            default: {
                                break;
                            }
                        }

                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
