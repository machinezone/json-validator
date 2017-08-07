package com.mz.json.validator;

import com.google.gson.annotations.SerializedName;
import com.mz.json.validator.model.ArrayOfStringProp;
import com.mz.json.validator.model.PrintableCheckableObject;
import com.mz.json.validator.model.StringProp;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mz.json.validator.CheckType.*;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ClassWithTooManyMethods")
public class CheckableObjectTest {
    private static final String CLASS_NAME = "com.mz.json.validator.CheckableObjectTest";

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testNoFields() throws Exception {
        class NoFields extends PrintableCheckableObject {
        }

        e.expect(CheckException.class);
        e.expectMessage("Class " + CLASS_NAME +
                "$1NoFields{} has no fields to check");
        new NoFields().check();
    }

    @Test
    public void testNoSerializedName() throws Exception {
        @SuppressWarnings("InstanceVariableMayNotBeInitialized")
        class NoSerializedName extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            private String prop;
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + CLASS_NAME +
                "$1NoSerializedName{prop=null} has no explicit serialized name");
        new NoSerializedName().check();
    }

    @Test
    public void testNotCheckableProp() throws Exception {
        @SuppressWarnings("InstanceVariableMayNotBeInitialized")
        class NotCheckableProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @SerializedName("prop_title")
            private String prop;
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop_title' of " + CLASS_NAME +
                "$1NotCheckableProp{prop_title=null} isn't inherited from 'ComplexCheckable' interface " +
                "and has no 'CheckRule' annotation");
        new NotCheckableProp().check();
    }

    @Test
    public void testEmptyStringProp() throws Exception {
        final class EmptyStringProp extends StringProp {
            private EmptyStringProp() {
                super("");
            }
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + CLASS_NAME +
                "$1EmptyStringProp{prop=} is an empty string");
        new EmptyStringProp().check();
    }

    @Test
    public void testGoodStringProp() throws Exception {
        final class GoodStringProp extends StringProp {
            private GoodStringProp() {
                super("string");
            }
        }

        GoodStringProp o = new GoodStringProp();
        o.check();
        assertEquals(o.toString(), "{prop=string}");
    }

    @Test
    public void testGoodStringPropWithWrongAdditionalField() throws Exception {
        final class GoodStringPropWithWrongAdditionalField extends StringProp {
            @SuppressWarnings({"unused"})
            private String prop2 = "";

            private GoodStringPropWithWrongAdditionalField() {
                super("string");
            }
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop2' of " + CLASS_NAME +
                "$1GoodStringPropWithWrongAdditionalField{prop2=, prop=string} " +
                "has no explicit serialized name");
        new GoodStringPropWithWrongAdditionalField().check();
    }

    @Test
    public void testGoodStringPropWithGoodAdditionalField() throws Exception {
        final class GoodStringPropWithGoodAdditionalField extends StringProp {
            @SuppressWarnings({"unused"})
            @CheckRule
            @SerializedName("prop2")
            private String prop2 = "";

            private GoodStringPropWithGoodAdditionalField() {
                super("string");
            }
        }

        GoodStringPropWithGoodAdditionalField o = new GoodStringPropWithGoodAdditionalField();
        o.check();
        assertEquals(o.toString(), "{prop2=, prop=string}");
    }

    @Test
    public void testGoodStringPropWithTransientField() throws Exception {
        final class GoodStringPropWithAdditionalField extends StringProp {
            @SuppressWarnings({"unused"})
            private transient String prop2 = "";

            private GoodStringPropWithAdditionalField() {
                super("string");
            }
        }

        GoodStringPropWithAdditionalField o = new GoodStringPropWithAdditionalField();
        o.check();
        assertEquals(o.toString(), "{prop=string}");
    }

    @Test
    public void testEmptyStringPropInProp() throws Exception {
        class EmptyStringPropInProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @SerializedName("prop")
            private final StringProp prop = new StringProp("");
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop.prop' of " + CLASS_NAME +
                "$1EmptyStringPropInProp{prop={prop=}} is an empty string");
        new EmptyStringPropInProp().check();
    }

    @Test
    public void testNullStringPropInProp() throws Exception {
        class NullStringPropInProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @SerializedName("prop")
            private final StringProp prop = null;
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + CLASS_NAME +
                "$1NullStringPropInProp{prop=null} isn't inherited from 'ComplexCheckable' interface " +
                "and has no 'CheckRule' annotation");
        new NullStringPropInProp().check();
    }

    @Test
    public void testGoodStringPropInProp() throws Exception {
        class GoodStringPropInProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @SerializedName("prop")
            private final StringProp prop = new StringProp("string");
        }

        GoodStringPropInProp o = new GoodStringPropInProp();
        o.check();
        assertEquals(o.toString(), "{prop={prop=string}}");
    }

    @Test
    public void testGoodStringPropInPropWithCheckRule() throws Exception {
        class GoodStringPropInPropWithCheckRule extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(NON_EMPTY_STRING)
            @SerializedName("prop")
            private final StringProp prop = new StringProp("string");
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + CLASS_NAME +
                "$1GoodStringPropInPropWithCheckRule{prop={prop=string}} " +
                "is inherited from 'ComplexCheckable' interface and also has 'CheckRule' annotation");
        new GoodStringPropInPropWithCheckRule().check();
    }

    @Test
    public void testEmptyArrayOfString() throws Exception {
        //noinspection MismatchedQueryAndUpdateOfCollection
        ArrayOfStringProp o = new ArrayOfStringProp();
        o.check();
        assertEquals(o.toString(), "[]");
    }

    @Test
    public void testEmptyArrayOfStringPropInProp() throws Exception {
        class EmptyArrayOfStringPropInProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @SerializedName("prop")
            private final ArrayOfStringProp prop = new ArrayOfStringProp();
        }

        EmptyArrayOfStringPropInProp o = new EmptyArrayOfStringPropInProp();
        o.check();
        assertEquals(o.toString(), "{prop=[]}");
    }

    @Test
    public void testArrayOfEmptyStringPropInProp() throws Exception {
        final class ArrayOfEmptyStringPropInProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
            @SerializedName("prop")
            private final ArrayOfStringProp prop = new ArrayOfStringProp();

            private ArrayOfEmptyStringPropInProp() {
                prop.add(new StringProp(""));
            }
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop.prop' of " + CLASS_NAME +
                "$1ArrayOfEmptyStringPropInProp{prop=[{prop=}]} is an empty string");
        new ArrayOfEmptyStringPropInProp().check();
    }

    @Test
    public void testArrayOfGoodStringPropInProp() throws Exception {
        final class ArrayOfGoodStringPropInProp extends PrintableCheckableObject {
            @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
            @SerializedName("prop")
            private final ArrayOfStringProp prop = new ArrayOfStringProp();

            private ArrayOfGoodStringPropInProp() {
                prop.add(new StringProp("string"));
            }
        }

        ArrayOfGoodStringPropInProp o = new ArrayOfGoodStringPropInProp();
        o.check();
        assertEquals(o.toString(), "{prop=[{prop=string}]}");
    }

    @Test
    public void testNullOrCheckableWithNull() throws Exception {
        final class NullOrCheckableWithNull extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(NULL_OR_CHECKABLE)
            @SerializedName("prop2")
            private StringProp prop2 = null;
        }

        NullOrCheckableWithNull o = new NullOrCheckableWithNull();
        o.check();
        assertEquals(o.toString(), "{prop2=null}");
    }

    @Test
    public void testNullOrCheckableWithNotCheckable() throws Exception {
        final class NullOrCheckableWithNotCheckable extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(NULL_OR_CHECKABLE)
            @SerializedName("prop2")
            private String prop2 = "";
        }

        e.expect(CheckException.class);
        e.expectMessage("Property 'prop2' of " + CLASS_NAME +
                "$1NullOrCheckableWithNotCheckable{prop2=} is not null " +
                "and isn't inherited from 'ComplexCheckable'");
        new NullOrCheckableWithNotCheckable().check();
    }

    @Test
    public void testNullOrCheckableWithCheckable() throws Exception {
        final class NullOrCheckableWithCheckable extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(NULL_OR_CHECKABLE)
            @SerializedName("prop2")
            private StringProp prop2 = new StringProp("string");
        }

        NullOrCheckableWithCheckable o = new NullOrCheckableWithCheckable();
        o.check();
        assertEquals(o.toString(), "{prop2={prop=string}}");
    }

    @Test
    public void testAnyWithNull() throws Exception {
        final class AnyWithNull extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(ANY)
            @SerializedName("prop2")
            private StringProp prop2 = null;
        }

        AnyWithNull o = new AnyWithNull();
        o.check();
        assertEquals(o.toString(), "{prop2=null}");
    }

    @Test
    public void testAnyWithNotCheckable() throws Exception {
        final class AnyWithNotCheckable extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(ANY)
            @SerializedName("prop2")
            private String prop2 = "";
        }

        AnyWithNotCheckable o = new AnyWithNotCheckable();
        o.check();
        assertEquals(o.toString(), "{prop2=}");
    }

    @Test
    public void testAnyWithCheckable() throws Exception {
        final class AnyWithCheckable extends PrintableCheckableObject {
            @SuppressWarnings({"unused"})
            @CheckRule(ANY)
            @SerializedName("prop2")
            private StringProp prop2 = new StringProp("string");
        }

        AnyWithCheckable o = new AnyWithCheckable();
        o.check();
        assertEquals(o.toString(), "{prop2={prop=string}}");
    }
}