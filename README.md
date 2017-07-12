# JSON validator

A Java library that puts together a basic implementation of JSON codec and a generic declarative approach of validating properties of Java objects and their subitems via annotations.

## Features
- You will never forget to explicitly set a serialized name for each property
- You will never get a sudden silent null after decoding in any of defined properties without explicit permission
- The whole classes tree will be checked recursively, including keys and values of any map or list
- You will get a verbose human-readable error, even if a property of sub-sub-sub...sub class of a map is invalid
- You will declare only rules which need to be checked, without any excessive code related to codec or Java
- Encoding and decoding obey common approaches like transient fields and has local-threaded optimization

## Example
    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
    public class ResponseError extends AbstractCheckableObject {
        // Allowed rules: ANY, NULL_OR_CHECKABLE, NOT_NULL, ZERO,
        // NON_NEGATIVE_INTEGER, POSITIVE_INTEGER, NON_EMPTY_STRING, TRUE, FALSE
        @CheckRule(NON_EMPTY_STRING)
        @SerializedName("reason")
        private String reason;

        @CheckRule(POSITIVE_INTEGER)
        @SerializedName("code")
        private Integer code;
    }

    // Allowed parents: AbstractCheckableMap, AbstractCheckableList, AbstractCheckableKeyValuePair
    class ResponseErrors extends AbstractCheckableList<ResponseError> {}

    public class Response extends AbstractCheckableObject {
        @CheckRule(NULL_OR_CHECKABLE)
        @SerializedName("errors")
        private ResponseErrors errors;
    }

    public class ResponseTest {
        @Rule
        public final ExpectedException e = ExpectedException.none();

        @Test
        public void testInvalidModel() throws Exception {
            e.expect(CheckException.class);
            e.expectMessage("Property 'errors.1.reason' of " +
                    "com.mz.json.examples.model.Response{errors=[{reason=x, code=666}, {reason=, code=null}]} " +
                    "is an empty string");
            decodeModel("{errors: [{reason: \"x\", code: 666}, {reason: \"\"}]}", Response.class);
        }
    }

## More examples
* [Complex Model](src/test/java/com/mz/json/examples/ComplexResponseTest.java)
* [All the unit-tests](src/test/java/com/mz/json/validator/)

## How to add the library to a project
1. Add the repo to GIT submodules:

    `git submodule add -b master <repo>`

2. Add to `dependencies` section of the `build.gradle` file:

    `compile project(":json-validator")`

3. Add to the `settings.gradle` file:

    `include 'json-validator'`

## See also
* [TODO list](TODO.md)
