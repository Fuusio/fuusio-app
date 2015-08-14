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

import java.util.HashMap;
import java.util.Stack;

import android.content.Context;

public class ActionManager {

    private static final int MAX_UNDO_STACK_SIZE = 20;
    
    private final HashMap<ActionContext, Stack<Action>> mActionStacks;
    private final HashMap<ActionContext, Action> mRedoableActions;
    private final Context mApplicationContext;

    private ActionContext mActiveActionContext;
    private Stack<Action> mActiveStack;

    public ActionManager(final Context pApplicationContext) {
        mApplicationContext = pApplicationContext;
        mActionStacks = new HashMap<>();
        mRedoableActions = new HashMap<>();
        mActiveStack = null;
    }

    public Context getContext() {
        return mApplicationContext;
    }

    public ActionContext getActiveActionContext() {
        return mActiveActionContext;
    }

    public void setActiveActionContext(final ActionContext pActionContext) {

        mActiveActionContext = pActionContext;

        if (mActiveActionContext != null) {
            mActiveStack = mActionStacks.get(mActiveActionContext);

            if (mActiveStack == null) {
                mActiveStack = new Stack<>();
                mActionStacks.put(pActionContext, mActiveStack);
            }
        }
    }

    public void clearActions() {
        mActiveStack.clear();
    }

    public boolean executeAction(final Action pAction) {
        boolean wasExecuted = pAction.execute(mActiveActionContext);

        if (wasExecuted) {
            if (mActiveStack.size() >= MAX_UNDO_STACK_SIZE) {
                mActiveStack.remove(0);
            }

            if (pAction.isUndoable()) {
                mActiveStack.add(pAction);
            }
        }

        return wasExecuted;
    }

    public boolean redo() {
        assert (isRedoEnabled());
        final Action action = mRedoableActions.get(mApplicationContext);
        boolean wasExecuted = executeAction(action);
        mRedoableActions.remove(mApplicationContext);
        return wasExecuted;
    }

    public boolean undo() {
        assert (isUndoEnabled());
        final Action action = mActiveStack.pop();
        boolean wasExecuted = action.undo(mActiveActionContext);

        if (wasExecuted) {
            mRedoableActions.put(mActiveActionContext, action);
        }
        return wasExecuted;
    }

    public boolean isRedoEnabled() {
        return (mRedoableActions.get(mApplicationContext) != null);
    }

    public boolean isUndoEnabled() {
        return !mActiveStack.isEmpty();
    }
}
