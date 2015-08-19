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

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.test.suitebuilder.annotation.SmallTest;

import org.fuusio.api.dependency.TestFlow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FlowManagerTest {

    private FlowFragmentContainer mFragmentHost;
    private FlowManager mFlowManager;
    private FragmentManager mFragmentManager;

    public FlowManagerTest() {
    }

    @Before
    public void beforeTests() {
        mFragmentManager = Mockito.mock(FragmentManager.class);
        mFlowManager = FlowManager.getInstance();
    }

    @Test
    public void test() {

        mFragmentHost = Mockito.mock(FlowFragmentContainer.class);
        when(mFragmentHost.getSupportFragmentManager()).thenReturn(mFragmentManager);
        assertNotNull(mFlowManager);

        final TestFlow flow = mFlowManager.startFlow(TestFlow.class, mFragmentHost, null);

        assertNotNull(flow);
    }

    @After
    public void afterTests() {
    }
}
