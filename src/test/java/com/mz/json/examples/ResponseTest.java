package com.mz.json.examples;

import com.mz.json.examples.model.base.Response;
import com.mz.json.validator.CheckException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mz.json.helpers.ModelCoDec.decodeModel;

public class ResponseTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testInvalidModel() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'errors.1.reason' of " +
                "com.mz.json.examples.model.base.Response{errors=[{reason=x, code=666}, {reason=, code=null}]} " +
                "is an empty string");
        decodeModel("{errors: [{reason: \"x\", code: 666}, {reason: \"\"}]}", Response.class);
    }
}