package org.fuusio.api.util;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.fuusio.api.util.StringList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StringListTest {

    private String STRING_A1 = "A1";
    private String STRING_A2 = "A2";
    private String STRING_A3 = "A3";
    private String STRING_1_ELEMENT = "A1";
    private String STRING_2_ELEMENTS = "A1;A2";
    private String STRING_3_ELEMENTS = "A1;A2;A3";

    StringList mStringList;

    @Before
    public void beforeTests() {
        mStringList = new StringList();
        mStringList.add(STRING_A1);
        mStringList.add(STRING_A2);
        mStringList.add(STRING_A3);
    }

    @Test
    public void testReadFromString() {

        // Test null String

        mStringList.readFromString(null);

        assertEquals(0, mStringList.size());

        // Test empty String

        mStringList.readFromString("");

        assertEquals(0, mStringList.size());

        // Test String containing just one element

        mStringList.readFromString(STRING_1_ELEMENT);

        assertEquals(1, mStringList.size());
        assertEquals(STRING_A1, mStringList.get(0));

        // Test String containing two elements

        mStringList.readFromString(STRING_2_ELEMENTS);

        assertEquals(2, mStringList.size());
        assertEquals(STRING_A1, mStringList.get(0));
        assertEquals(STRING_A2, mStringList.get(1));

        // Test String containing three elements

        mStringList.readFromString(STRING_3_ELEMENTS);

        assertEquals(3, mStringList.size());
        assertEquals(STRING_A1, mStringList.get(0));
        assertEquals(STRING_A2, mStringList.get(1));
        assertEquals(STRING_A3, mStringList.get(2));
    }

    @Test
    public void testWiteToString() {

        String result = null;

        // Test null String

        mStringList.readFromString(null);

        result = mStringList.writeToString();
        assertEquals("", result);

        // Test empty String

        mStringList.readFromString("");

        result = mStringList.writeToString();
        assertEquals("", result);

        // Test String containing just one element

        mStringList.readFromString(STRING_1_ELEMENT);

        result = mStringList.writeToString();
        assertEquals(STRING_1_ELEMENT, result);

        // Test String containing two elements

        mStringList.readFromString(STRING_2_ELEMENTS);

        result = mStringList.writeToString();
        assertEquals(STRING_2_ELEMENTS, result);

        // Test String containing three elements

        mStringList.readFromString(STRING_3_ELEMENTS);

        result = mStringList.writeToString();
        assertEquals(STRING_3_ELEMENTS, result);

        int a = 0;
    }

    @After
    public void afterTests() {
        mStringList.clear();
    }
}
