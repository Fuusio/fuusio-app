package org.fuusio.api.util;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.gson.JsonObject;
import org.fuusio.api.util.GsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class GsonObjectTest {

    private Foo mFoo;

    @Before
    public void beforeTests() {
        mFoo = new Foo();
    }

    @Test
    public void test() {

        final JsonObject jsonObject = mFoo.toJson();
        final Foo foo2 = Foo.fromJson(jsonObject, Foo.class);

        assertTrue(foo2.booleanValue && foo2.booleanValue == mFoo.booleanValue);
        assertTrue(foo2.floatValue == mFoo.floatValue);
        assertTrue(foo2.intValue == mFoo.intValue);
        assertTrue(foo2.stringValue.contentEquals(mFoo.stringValue));

    }

    @After
    public void afterTests() {
    }


    public class Foo extends GsonObject {

        private boolean booleanValue;
        private float floatValue;
        private int intValue;
        private String stringValue;

        public Foo() {
            booleanValue = true;
            floatValue = 3.14159f;
            intValue = 123;
            stringValue = "abc";
        }

    }

}
