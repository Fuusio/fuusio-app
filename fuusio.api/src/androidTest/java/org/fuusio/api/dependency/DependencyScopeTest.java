package org.fuusio.api.dependency;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.fuusio.api.flow.FlowFragmentContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import android.test.suitebuilder.annotation.SmallTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DependencyScopeTest {

    private Context mApplicationContext;
    private FlowFragmentContainer mFragmentHost;
    private TestView mTestView;
    private TestPresenter mTestPresenter;
    private TestParentDependencyScope mParentScope;


    @Before
    public void beforeTests() {
        mApplicationContext = InstrumentationRegistry.getContext();
        mFragmentHost = Mockito.mock(FlowFragmentContainer.class);
        mParentScope = new TestParentDependencyScope();
    }

    @Test
    public void test() {

        final TestFlow testFlow = new TestFlow(mFragmentHost);
        final TestDependencyScope scope = (TestDependencyScope)testFlow.getDependencyScope();
        scope.setParentScope(mParentScope);

        assertNotNull(scope);
        assertTrue(Dependency.getActiveScope() != scope);
        assertNotNull(scope.getParentScope());

        Dependency.activateScope(testFlow);
        assertNotNull(Dependency.getActiveScope());

        final TestView view = Mockito.mock(TestView.class);
        final TestPresenter presenter = D.get(TestPresenter.class, view);

        assertNotNull(presenter);
        assertNotNull(D.get(TestView.class));

        final TestParentDependencyScope.Foo foo = D.get(TestParentDependencyScope.Foo.class);
        assertNotNull(foo);

        scope.setWasDisposed(false);

        Dependency.deactivateScope(testFlow);
        assertTrue(Dependency.getActiveScope() != scope);
        assertTrue(scope.wasDisposed());
        assertTrue(scope.isCleared());

        boolean npeCatched = false;

        try {
            D.get(TestFlow.class);
        } catch (NullPointerException e) {
            npeCatched = true;
        }

        assertTrue(npeCatched);
    }

    @After
    public void afterTests() {
    }
}
