package org.fuusio.api.dependency;

import org.fuusio.api.flow.AbstractFlow;
import org.fuusio.api.flow.FlowScope;
import org.fuusio.api.flow.FlowFragmentContainer;

public class TestFlow extends AbstractFlow {

    public TestFlow(final FlowFragmentContainer pHost) {
        super(pHost);
    }

    @Override
    protected FlowScope createDependencyScope() {
        return new TestDependencyScope(this);
    }
}