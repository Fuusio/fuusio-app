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
import android.support.v4.app.FragmentManager;

import org.fuusio.api.component.Component;
import org.fuusio.api.dependency.ScopeManager;
import org.fuusio.api.mvp.View;

import java.util.List;

/**
 * {@link Flow} defines an interface for components that implement UI flow logic controlling Views
 * and Presenter according to received user interaction events. A concrete {@link Flow} implementation
 * also provides a {@link FlowScope} for dependency injection.
 */
public interface Flow extends Component, ScopeManager, FragmentManager.OnBackStackChangedListener {

    /**
     * Gets the currently active Views.
     * @return A {@link List} containing the currently active Views as {@link View}s.
     */
    List<View> getActiveViews();

    /**
     * Adds the given {@link View} to the set of active Views.
     * @param pView A {@link View} to be added.
     * @return The given {@link View} if it was not already in the set of active Views.
     */
    View addActiveView(View pView);

    /**
     * Removes the given {@link View} from the set of active Views.
     * @param pView A {@link View} to be removed.
     * @return The given {@link View} if it was included in the set of active Views.
     */
    View removeActiveView(View pView);

    /**
     * A {@link Flow} implementation can use this method to activate i.e. to make visible the given
     * {@link View}.
     * @param pView A {@link View} to be activated. May not be {@link null}.
     */
    void activateView(View pView);

    /**
     * Tests if the given {@link View} is currently active one.
     * @param pView A {@link View}.
     * @return A {@code boolean} value.
     */
    boolean isActiveView(View pView);

    /**
     * Gets the {@link FlowManager} that started this {@link Flow}.
     * @return A {@link FlowManager}.
     */
    FlowManager getFlowManager();

    /**
     * Sets the {@link FlowManager} that started this {@link Flow}.
     * @param pManager A {@link FlowManager}.
     */
    void setFlowManager(FlowManager pManager);

    /**
     * This method is invoked to pause this {@link Flow}.
     */
    void pause();

    /**
     * This method is invoked to resume this {@link Flow}.
     */
    void resume();

    /**
     * This method is invoked to restart this {@link Flow}.
     */
    void restart();

    /**
     * This method is invoked to start this {@link Flow}. The method is not intended to be invoked
     * directly by a developer, but via invoking {@link FlowManager#startFlow(Flow, Bundle)}.
     * @param pParams A {@link Bundle} containing parameters for starting the {@link Flow}.
     */
    void start(final Bundle pParams);

    /**
     * This method is invoked to stop this {@link Flow}.
     */
    void stop();

    /**
     * This method is invoked to destroy this {@link Flow}.
     */
    void destroy();

    /**
     * This method is invoked by {@link Flow#pause()} when this {@link Flow} is paused.
     */
    void onPause();

    /**
     * This method is invoked by {@link Flow#resume()} when this {@link Flow} is resumed.
     */
    void onResume();

    /**
     * This method is invoked by {@link Flow#restart()} when this {@link Flow} is restarted.
     */
    void onRestart();

    /**
     * This method is invoked by {@link Flow#start(Bundle)} when this {@link Flow} is started.
     * @param pParams A {@link Bundle} containing parameters for starting the {@link Flow}.
     */
    void onStart(Bundle pParams);

    /**
     * This method is invoked by {@link Flow#stop()} when this {@link Flow} is  stopped.
     */
    void onStop();

    /**
     * This method is invoked by {@link Flow#destroy()} when this {@link Flow} is destroyed.
     */
    void onDestroy();

    /**
     * Tests if this {@link Flow} handles the back pressed event.
     * @return A {@code boolean} value.
     */
    boolean isBackPressedEventHandler();

    /**
     * Clears the back stack managed by {@link FragmentManager}.
     */
    void clearBackStack();

    /**
     * Tests if the previous {@link View} can be navigated back to.
     * @return A {@code boolean} value.
     */
    boolean canGoBack();

    /**
     * Goes back to previous {@link View}.
     */
    void goBack();

    /**
     * Invoked when the given {@link View} has been brought back to foreground and resumed.
     * @param pView A {@link View}.
     */
    void onNavigatedBackTo(View pView);
}
