package com.mz.json.helpers;

import com.google.gson.annotations.SerializedName;
import com.mz.json.validator.AbstractCheckableObject;
import com.mz.json.validator.CheckException;
import com.mz.json.validator.CheckRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mz.json.helpers.ModelCoDec.decodeModel;
import static com.mz.json.helpers.ModelCoDecTest.TestModel.CLASS_NAME;
import static com.mz.json.validator.CheckType.NON_EMPTY_STRING;
import static org.junit.Assert.assertEquals;

public class ModelCoDecTest {
    class TestModel extends AbstractCheckableObject {
        static final transient String CLASS_NAME = "com.mz.json.helpers.ModelCoDecTest$TestModel";

        @SuppressWarnings("unused")
        @CheckRule(NON_EMPTY_STRING)
        @SerializedName("prop")
        final String prop;

        TestModel(String prop) {
            this.prop = prop;
        }
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testDecodeModelFromEmptyString() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Can't decode class " + CLASS_NAME + " from JSON: [!empty!]");
        decodeModel("", TestModel.class);
    }

    @Test
    public void testDecodeModelBecauseOfEmptyString() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + CLASS_NAME + "{prop=null} is not a string");
        decodeModel("{}", TestModel.class);
    }

    @Test
    public void testDecodeModel() throws Exception {
        TestModel testModel = decodeModel("{\"prop\": \"x\"}", TestModel.class);
        assertEquals("x", testModel.prop);
    }
}