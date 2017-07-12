package com.mz.json.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mz.json.validator.CheckType.*;
import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

@SuppressWarnings("ClassWithTooManyMethods")
public class CheckTypeTest {
    class TestCheckable implements ComplexCheckable {
        static final String CLASS_NAME = "com.mz.json.validator.CheckTypeTest$TestCheckable";

        private final Object prop;

        TestCheckable(Object prop) {
            this.prop = prop;
        }

        Object getProp() {
            return prop;
        }

        @Override
        public void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        }

        @Override
        public String toString() {
            return String.format("[prop: %s]", prop);
        }
    }

    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testThrowPropertyCheckException() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: string] is invalid");
        throw new PropertyCheckException(new TestCheckable("string"),
                getEmptyPropertyPath().getAppendedInstance("prop"), "is invalid");
    }

    @Test
    public void testAnyWithNull() throws Exception {
        checkCheckType(ANY, null);
    }

    @Test
    public void testAnyWithCheckable() throws Exception {
        checkCheckType(ANY, new TestCheckable("sub-prop"));
    }

    @Test
    public void testAnyWithEmptyString() throws Exception {
        checkCheckType(ANY, "");
    }

    @Test
    public void testNotNullWithNull() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: null] is null");
        checkCheckType(NOT_NULL, null);
    }

    @Test
    public void testNotNullWithCheckable() throws Exception {
        checkCheckType(NOT_NULL, new TestCheckable("sub-prop"));
    }

    @Test
    public void testNotNullWithEmptyString() throws Exception {
        checkCheckType(NOT_NULL, "");
    }

    @Test
    public void testNullOrCheckableWithNull() throws Exception {
        checkCheckType(NULL_OR_CHECKABLE, null);
    }

    @Test
    public void testNullOrCheckableWithCheckable() throws Exception {
        checkCheckType(NULL_OR_CHECKABLE, new TestCheckable("sub-prop"));
    }

    @Test
    public void testNullOrCheckableWithEmptyString() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: ] is not null and isn't inherited from 'ComplexCheckable'");
        checkCheckType(NULL_OR_CHECKABLE, "");
    }

    @Test
    public void testNotEmptyStringWithEmptyString() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: ] is an empty string");
        checkCheckType(NON_EMPTY_STRING, "");
    }

    @Test
    public void testNotEmptyStringWithNotString() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: null] is not a string");
        checkCheckType(NON_EMPTY_STRING, null);
    }

    @Test
    public void testNotEmptyString() throws Exception {
        checkCheckType(NON_EMPTY_STRING, "a");
    }

    @Test
    public void testPositiveIntegerWithNotPositiveInteger() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: 0] is lower than 1");
        checkCheckType(POSITIVE_INTEGER, 0);
    }

    @Test
    public void testPositiveIntegerWithNotInteger() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: ] is not an integer");
        checkCheckType(POSITIVE_INTEGER, "");
    }

    @Test
    public void testPositiveInteger() throws Exception {
        checkCheckType(POSITIVE_INTEGER, 1);
    }

    @Test
    public void testZeroNotZero() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: 1] is not zero");
        checkCheckType(ZERO, 1);
    }

    @Test
    public void testZeroWithNotInteger() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: ] is not an integer");
        checkCheckType(ZERO, "");
    }

    @Test
    public void testZero() throws Exception {
        checkCheckType(ZERO, 0);
    }

    @Test
    public void testNotNegativeIntegerWithNotPositiveInteger() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: -1] is lower than zero");
        checkCheckType(NON_NEGATIVE_INTEGER, -1);
    }

    @Test
    public void testNotNegativeIntegerWithNotInteger() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: ] is not an integer");
        checkCheckType(NON_NEGATIVE_INTEGER, "");
    }

    @Test
    public void testNotNegativeInteger() throws Exception {
        checkCheckType(NON_NEGATIVE_INTEGER, 1);
    }

    @Test
    public void testTrueWithFalse() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: false] isn't true");
        checkCheckType(TRUE, false);
    }

    @Test
    public void testTrueWithNotBoolean() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: null] is not a boolean");
        checkCheckType(TRUE, null);
    }

    @Test
    public void testTrue() throws Exception {
        checkCheckType(TRUE, true);
    }

    @Test
    public void testFalseWithTrue() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: true] isn't false");
        checkCheckType(FALSE, true);
    }

    @Test
    public void testFalseWithNotBoolean() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'prop' of " + TestCheckable.CLASS_NAME +
                "[prop: null] is not a boolean");
        checkCheckType(FALSE, null);
    }

    @Test
    public void testFalse() throws Exception {
        checkCheckType(FALSE, false);
    }

    private void checkCheckType(CheckType checkType, Object value) throws CheckException {
        TestCheckable testCheckable = new TestCheckable(value);
        checkType.check(testCheckable, getEmptyPropertyPath().getAppendedInstance("prop"),
                testCheckable.getProp());
    }
}