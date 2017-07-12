package com.mz.json.validator;

import com.mz.json.validator.model.StringProp;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class AbstractCheckableKeyValuePairTest {
    private static final String CLASS_NAME = "com.mz.json.validator.AbstractCheckableKeyValuePairTest";

    private class StringCheckableKeyValuePair extends AbstractCheckableKeyValuePair<String> {
        StringCheckableKeyValuePair(String key, String value) {
            super(key, value);
        }
    }

    private class ObjectCheckableKeyValuePair extends AbstractCheckableKeyValuePair<Object> {
        ObjectCheckableKeyValuePair(String key, Object value) {
            super(key, value);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private class CheckableKeyValuePair<T extends ComplexCheckable> extends AbstractCheckableKeyValuePair<T> {
        CheckableKeyValuePair(String key, T value) {
            super(key, value);
        }
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testGoodPair() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        StringCheckableKeyValuePair pair = new StringCheckableKeyValuePair("k", "v");
        pair.check();
        assertEquals(pair.toString(), "{k=v}");
    }

    @Test
    public void testGoodNonStringPair() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        ObjectCheckableKeyValuePair pair = new ObjectCheckableKeyValuePair("k", true);
        pair.check();
        assertEquals(pair.toString(), "{k=true}");
    }

    @Test
    public void testPairWithEmptyKey() throws Exception {
        StringCheckableKeyValuePair pair = new StringCheckableKeyValuePair("", "v");

        e.expect(CheckException.class);
        e.expectMessage("Property '$key' of " + CLASS_NAME +
                "$StringCheckableKeyValuePair{=v} is an empty string");
        pair.check();
    }

    @Test
    public void testPairWithNullValue() throws Exception {
        StringCheckableKeyValuePair pair = new StringCheckableKeyValuePair("k", null);

        e.expect(CheckException.class);
        e.expectMessage("Property 'k' of " + CLASS_NAME +
                "$StringCheckableKeyValuePair{k=null} is null");
        pair.check();
    }

    @Test
    public void testPairWithCheckableValue() throws Exception {
        class NoFields extends AbstractCheckableObject {
        }

        CheckableKeyValuePair<NoFields> pair = new CheckableKeyValuePair<>("k", new NoFields());

        e.expect(CheckException.class);
        e.expectMessage("Class " + CLASS_NAME +
                "$1NoFields{k={}} has no fields to check");
        pair.check();
    }

    @Test
    public void testPairWithEmptyStringInCheckableValue() throws Exception {
        CheckableKeyValuePair<StringProp> pair = new CheckableKeyValuePair<>("k", new StringProp(""));

        e.expect(CheckException.class);
        e.expectMessage("Property 'k.prop' of " + CLASS_NAME +
                "$CheckableKeyValuePair{k={prop=}} is an empty string");
        pair.check();
    }

    @Test
    public void testPairWithGoodStringProp() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        CheckableKeyValuePair<StringProp> pair = new CheckableKeyValuePair<>("k", new StringProp("v"));

        pair.check();
        assertEquals(pair.toString(), "{k={prop=v}}");
    }
}