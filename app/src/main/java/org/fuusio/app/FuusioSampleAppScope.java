package org.fuusio.app;

import org.fuusio.app.feature.test.flow.TestFlow;
import org.fuusio.app.feature.test.flow.TestFlowImpl;

import org.fuusio.api.app.FuusioApplicationScope;
import org.fuusio.api.dependency.D;
import org.fuusio.api.flow.FragmentContainer;
import org.fuusio.api.model.ModelObjectManager;

public class FuusioSampleAppScope extends FuusioApplicationScope {

    public FuusioSampleAppScope(final FuusioSampleApp pApp) {
        super(pApp);
    }

    @Override
    protected <T> T getDependency() {

        if (type(TestFlow.class)) {
            return dependency(new TestFlowImpl(D.get(FragmentContainer.class)));
        }
        return super.getDependency();
    }

    @Override
    protected ModelObjectManager getModelObjectManager() {
        return null; // TODO
    }
}
