package org.fuusio.api.app;

import org.fuusio.api.dependency.ApplicationScope;
import org.fuusio.api.flow.FlowManager;
import org.fuusio.api.model.ModelObjectManager;
import org.fuusio.api.ui.action.ActionManager;

public abstract class FuusioApplicationScope extends ApplicationScope {

    protected FuusioApplicationScope(final FuusioApplication pApplication) {
        super(pApplication);
    }

    @Override
    protected <T> T getDependency() {
        if (type(ActionManager.class)) {
            return dependency(new ActionManager(getApplicationContext()));
        } else if (type(ModelObjectManager.class)) {
            return dependency(getModelObjectManager());
        } else if (type(FlowManager.class)) {
            return dependency(FlowManager.getInstance());
        }
        return super.getDependency();
    }

    protected abstract ModelObjectManager getModelObjectManager();
}
