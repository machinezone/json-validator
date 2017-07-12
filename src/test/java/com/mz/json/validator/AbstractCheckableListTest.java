package com.mz.json.validator;

import com.mz.json.validator.model.StringProp;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class AbstractCheckableListTest {
    private static final String CLASS_NAME = "com.mz.json.validator.AbstractCheckableListTest";

    private class TestCheckableList extends AbstractCheckableList<StringProp> {
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyMap() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        TestCheckableList o = new TestCheckableList();
        o.check();
        assertEquals(o.toString(), "[]");
    }

    @Test
    public void testGoodMap() throws Exception {
        TestCheckableList o = new TestCheckableList();
        o.add(new StringProp("prop"));
        o.check();
        assertEquals(o.toString(), "[{prop=prop}]");
    }

    @Test
    public void testWrongMap() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        TestCheckableList o = new TestCheckableList();
        o.add(new StringProp(""));

        e.expect(CheckException.class);
        e.expectMessage("Property '0.prop' of " + CLASS_NAME +
                "$TestCheckableList[{prop=}] is an empty string");
        o.check();
    }

    @Test
    public void testWrongItem() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        TestCheckableList o = new TestCheckableList();
        o.add(null);

        e.expect(CheckException.class);
        e.expectMessage("Property '0' of " + CLASS_NAME +
                "$TestCheckableList[null] is null");
        o.check();
    }
}