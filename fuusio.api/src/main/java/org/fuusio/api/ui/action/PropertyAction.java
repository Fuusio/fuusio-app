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
package org.fuusio.api.ui.action;

import org.fuusio.api.dependency.D;
import org.fuusio.api.model.ModelObject;
import org.fuusio.api.model.ModelObjectContext;
import org.fuusio.api.model.Property;

public class PropertyAction extends UndoableAction {

    private final Property mProperty;
    private final ModelObjectContext mObjectContext;

    private Object mNewValue;
    private Object mOldValue;
    private Class<? extends ModelObject> mObjectClass;
    private long mObjectId;


    public PropertyAction(final Property property, final ModelObjectContext objectContext) {
        super(-1, -1);
        mProperty = property;
        mObjectContext = objectContext;
    }

    public boolean set(final ModelObject modelObject, final Object newValue) {
        mObjectClass = modelObject.getClass();
        mObjectId = modelObject.getId();
        mNewValue = newValue;

        final ActionManager actionManager = D.get(ActionManager.class);
        return actionManager.executeAction(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.fuusio.api.ui.action.Action#execute(android.app.Activity)
     */
    @Override
    public boolean execute(final ActionContext actionContext) {
        final ModelObject modelObject = mObjectContext.getObject(mObjectClass, mObjectId);
        // return mProperty.set(modelObject, mNewValue);
        return mProperty.set(modelObject, mNewValue.toString());
    }

    @Override
    public boolean undo(final ActionContext actionContext) {
        final ModelObject modelObject = mObjectContext.getObject(mObjectClass, mObjectId);
        return mProperty.set(modelObject, mOldValue.toString());
    }

    @Override
    public boolean isUndoable() {
        return mObjectContext.exists(mObjectClass, mObjectId);
    }
}
