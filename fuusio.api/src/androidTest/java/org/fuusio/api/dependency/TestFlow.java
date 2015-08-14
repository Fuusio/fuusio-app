package org.fuusio.api.dependency;

import org.fuusio.api.flow.AbstractFlow;
import org.fuusio.api.flow.FlowScope;
import org.fuusio.api.flow.FragmentContainer;

public class TestFlow extends AbstractFlow {

    public TestFlow(final FragmentContainer pHost) {
        super(pHost);
    }

    @Override
    protected FlowScope createDependencyScope() {
        return new TestDependencyScope(this);
    }
}