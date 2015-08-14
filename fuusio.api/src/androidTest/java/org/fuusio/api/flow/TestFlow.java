package org.fuusio.api.flow;

public class TestFlow extends AbstractFlow {

    public TestFlow(final FragmentContainer pHost) {
        super(pHost);
    }

    @Override
    protected FlowScope createDependencyScope() {
        return new TestDependencyScope(this);
    }
}