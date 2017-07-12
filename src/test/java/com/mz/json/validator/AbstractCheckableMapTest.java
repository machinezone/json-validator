package com.mz.json.validator;

import com.mz.json.validator.model.StringProp;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class AbstractCheckableMapTest {
    private static final String CLASS_NAME = "com.mz.json.validator.AbstractCheckableMapTest";

    private class TestCheckableMap extends AbstractCheckableMap<String,StringProp> {
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testEmptyMap() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        TestCheckableMap o = new TestCheckableMap();
        o.check();
        assertEquals(o.toString(), "{}");
    }

    @Test
    public void testGoodMap() throws Exception {
        TestCheckableMap o = new TestCheckableMap();
        o.put("k", new StringProp("prop"));
        o.check();
        assertEquals(o.toString(), "{k={prop=prop}}");
    }

    @Test
    public void testWrongMap() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        TestCheckableMap o = new TestCheckableMap();
        o.put("k", new StringProp(""));

        e.expect(CheckException.class);
        e.expectMessage("Property 'k.prop' of " + CLASS_NAME +
                "$TestCheckableMap{k={prop=}} is an empty string");
        o.check();
    }

    @Test
    public void testWrongItem() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        TestCheckableMap o = new TestCheckableMap();
        o.put("k", null);

        e.expect(CheckException.class);
        e.expectMessage("Property 'k' of " + CLASS_NAME +
                "$TestCheckableMap{k=null} is null");
        o.check();
    }
}