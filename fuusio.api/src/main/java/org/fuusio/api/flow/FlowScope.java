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

import org.fuusio.api.dependency.DependencyScope;

public abstract class FlowScope<T_Flow extends Flow> extends DependencyScope {

    protected final T_Flow mFlow;

    protected FlowFragmentContainer mFragmentContainer;

    protected FlowScope(final T_Flow flow) {
        this(flow, null);
    }

    protected FlowScope(final T_Flow flow, final FlowFragmentContainer container) {
        mFlow = flow;
        mFragmentContainer = container;
    }

    public T_Flow getFlow() {
        return mFlow;
    }
}
