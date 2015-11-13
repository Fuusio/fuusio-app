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

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

import org.fuusio.api.mvp.View;

/**
 * {@link FlowFragmentContainer} defines an interface for objects that can be used to show {@link FlowFragment}s.
 * Typically a such object is an {@link android.app.Activity}.
 */
public interface FlowFragmentContainer {

    /**
     * Gets the {@link Context} available for  {@link FlowFragment}s
     *
     * @return A {@link Context}.
     */
    Context getContext();

    /**
     * Tests if the given {@link View} can be shown.
     *
     * @param view A {@link View}.
     * @return A {@code boolean}.
     */
    boolean canShowView(View view);

    /**
     * Gets the {@link FragmentManager} that manages {@link android.app.Fragment}s.
     *
     * @return A {@link FragmentManager}.
     */
    FragmentManager getSupportFragmentManager();

    /**
     * Gets the {@link Resources} available for {@link FlowFragment}s
     *
     * @return A {@link Resources}.
     */
    Resources getResources();

    /**
     * Shows the given {@link FlowFragment} which is managed by the given {@link Flow}. Each shown
     * {@link FlowFragment} instance has to be supplied with a tag that can be used to retrieve
     * the instance later on.
     *
     * @param flow        A {@link Flow}. May not be {@code null}.
     * @param fragment    A {@link FlowFragment}. May not be {@code null}.
     * @param fragmentTag A {@link String} used as a tag to identify the {@link FlowFragment}.
     *                    In most cases tag can be simply the class name of the  {@link FlowFragment}.
     */
    void showFlowFragment(Flow flow, FlowFragment fragment, String fragmentTag);
}
