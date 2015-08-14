package org.fuusio.api.util;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TupleTest {

    private static final double DOUBLE_VALUE_1 = 1.2345678;
    private static final double DOUBLE_VALUE_2 = 2.3456789;

    private Tuple<Double> mTuple;

    @Before
    public void beforeTests() {
        mTuple = new Tuple<>(DOUBLE_VALUE_1,DOUBLE_VALUE_2);
    }

    @After
    public void afterTests() {

    }

    @Test
    public void testGetValue1() {
        mTuple.setValue1(DOUBLE_VALUE_1);
        assertEquals(DOUBLE_VALUE_1, mTuple.getValue1(), 0);
    }

    @Test
    public void testSetValue1() {
        mTuple.setValue1(DOUBLE_VALUE_2);
        assertEquals(DOUBLE_VALUE_2, mTuple.getValue1(), 0);
    }

    @Test
    public void testGetValue2() {
        mTuple.setValue2(DOUBLE_VALUE_1);
        assertEquals(DOUBLE_VALUE_1, mTuple.getValue2(), 0);
    }

    @Test
    public void testSetValue2() {
        mTuple.setValue2(DOUBLE_VALUE_2);
        assertEquals(DOUBLE_VALUE_2, mTuple.getValue2(), 0);
    }
}