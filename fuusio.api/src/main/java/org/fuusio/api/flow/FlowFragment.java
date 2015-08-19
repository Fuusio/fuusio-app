/*
 * Copyright (C) 2014 - 2015 Marko Salmela, http://fuusio.org
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
package org.fuusio.api.flow;

import android.os.Bundle;

import org.fuusio.api.mvp.ViewFragment;
import org.fuusio.api.mvp.Presenter;

public abstract class FlowFragment<T_Presenter extends Presenter> extends ViewFragment<T_Presenter> {

    private Flow mFlow;

    protected FlowFragment() {
    }

    /**
     * Gets the {@link Flow} that controls this {@link FlowFragment}.
     * @return A {@link Flow}.
     */
    public final Flow getFlow() {
        return mFlow;
    }

    /**
     * Sets the {@link Flow} that controls this {@link FlowFragment}.
     * @param pFlow A {@link Flow}.
     */
    public void setFlow(final Flow pFlow) {
        mFlow = pFlow;
    }

    /**
     * Get a tag for this {@link FlowFragment}.
     * @return A tag as a {@link String}.
     */
    public String getFlowTag() {
        return getClass().getSimpleName();
    }

    /**
     * Gets the {@link FlowScope} owned by the {@link Flow} that controls this
     * {@link FlowFragment}.
     * @param <T> The generic return type of {@link FlowScope}.
     * @return A {@link FlowScope}.
     */
    public final <T extends Flow> FlowScope<T> getDependencyScope() {
        return mFlow.getDependencyScope();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mFlow != null) {
            mFlow.addActiveView(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mFlow != null) {
            mFlow.removeActiveView(this);
        }
    }
}
