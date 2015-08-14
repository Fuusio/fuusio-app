package org.fuusio.app.feature.test.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.fuusio.app.R;
import org.fuusio.app.feature.test.presenter.TestPresenter2;

import org.fuusio.api.binding.Binding;
import org.fuusio.api.dependency.D;
import org.fuusio.api.flow.FlowFragment;

public class TestFragment2 extends FlowFragment<TestPresenter2> implements TestView2 {

    private Binding mButtonBinding;

    public TestFragment2() {
        mPresenter = D.get(TestPresenter2.class, this);
    }

    @Override
    public View onCreateView(final LayoutInflater pInflater, final ViewGroup pContainer, final Bundle pInState) {
        return pInflater.inflate(R.layout.fragment_test_view_2, pContainer, false);
    }

    @Override
    protected void createBindings() {

        mButtonBinding = new Binding(this, R.id.button) {
            @Override
            protected void clicked() {
                mPresenter.onButtonClicked();
            }
        };
    }

    @Override
    public void showToast(final String pMessage) {
        final Toast toast = Toast.makeText(getActivity(), pMessage, Toast.LENGTH_LONG);
        toast.show();
    }
}
