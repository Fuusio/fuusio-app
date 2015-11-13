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
package org.fuusio.api.dependency;

import android.os.Bundle;

import org.fuusio.api.flow.AbstractFlow;
import org.fuusio.api.flow.FlowFragmentContainer;
import org.fuusio.api.flow.FlowScope;

public class TestFlow extends AbstractFlow {

    public TestFlow(final FlowFragmentContainer pHost, final Bundle pParams) {
        super(pHost, pParams);
    }

    @Override
    protected FlowScope createDependencyScope() {
        return new TestDependencyScope(this);
    }

}