package org.fuusio.api.flow;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.test.suitebuilder.annotation.SmallTest;

import org.fuusio.api.dependency.D;
import org.fuusio.api.dependency.DependencyScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AbstractFlowTest {

    private Context mApplicationContext;
    private FlowFragmentContainer mFragmentHost;
    private FlowManager mFlowManager;
    private FragmentManager mFragmentManager;
    private TestFlow mTestFlow;
    private TestView mTestView;
    private TestPresenter mTestPresenter;

    @Before
    public void beforeTests() {

        mApplicationContext = InstrumentationRegistry.getContext();
        mFragmentManager = Mockito.mock(FragmentManager.class);
        mFlowManager = FlowManager.getInstance();
        mFragmentHost = Mockito.mock(FlowFragmentContainer.class);

        when(mFragmentHost.getSupportFragmentManager()).thenReturn(mFragmentManager);
    }

    @Test
    public void test() {

        assertNotNull(mFlowManager);

        mTestFlow = mFlowManager.startFlow(TestFlow.class, mFragmentHost);
        assertNotNull(mTestFlow);

        final DependencyScope scope = mTestFlow.getDependencyScope();
        assertNotNull(scope);

        mTestFlow.onStart();

        verify(mFragmentManager, atLeastOnce()).addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) any());

        assertEquals(D.getActiveScope(), scope);


        mTestView = Mockito.mock(TestView.class);

        final TestPresenter presenter = D.get(TestPresenter.class, mTestView);
        
        when(mTestView.getPresenter()).thenReturn(presenter);
        mTestFlow.activateView(mTestView);
        assertNotNull(D.get(TestView.class));

        mTestFlow.stop();

        verify(mFragmentManager, atLeastOnce()).removeOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)any());
    }

    @After
    public void afterTests() {
    }
}
