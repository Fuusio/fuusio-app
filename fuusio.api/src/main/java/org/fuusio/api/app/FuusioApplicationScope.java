/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.app;

import org.fuusio.api.dependency.ApplicationScope;
import org.fuusio.api.flow.FlowManager;
import org.fuusio.api.graphics.BitmapManager;
import org.fuusio.api.model.ModelObjectManager;
import org.fuusio.api.rest.RequestManager;
import org.fuusio.api.rest.volley.VolleyRequestManager;
import org.fuusio.api.ui.action.ActionManager;

public abstract class FuusioApplicationScope extends ApplicationScope {

    protected FuusioApplicationScope(final FuusioApplication pApplication) {
        super(pApplication);
    }

    @Override
    protected <T> T getDependency() {
        if (type(ActionManager.class)) {
            return dependency(new ActionManager(getApplicationContext()));
        } else if (type(BitmapManager.class)) {
            return dependency(new BitmapManager());
        } else if (type(ModelObjectManager.class)) {
            return dependency(getModelObjectManager());
        } else if (type(FlowManager.class)) {
            return dependency(FlowManager.getInstance());
        } else if (type(RequestManager.class)) {
            return dependency(new VolleyRequestManager());
        }
        return super.getDependency();
    }

    protected abstract ModelObjectManager getModelObjectManager();
}
