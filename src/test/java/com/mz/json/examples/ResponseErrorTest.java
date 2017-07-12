package com.mz.json.examples;

import com.mz.json.examples.model.base.ResponseError;
import com.mz.json.validator.CheckException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mz.json.helpers.ModelCoDec.decodeModel;
import static com.mz.json.helpers.ModelCoDec.encodeModel;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class ResponseErrorTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testValidModel() throws Exception {
        ResponseError error = decodeModel("{code=201, reason: \"Created\"}", ResponseError.class);
        assertEquals("{reason=Created, code=201}", error.toString());
        assertEquals("{\"reason\":\"Created\",\"code\":201}", encodeModel(error));
    }

    @Test
    public void testInvalidModel() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'reason' of " +
                "com.mz.json.examples.model.base.ResponseError{reason=null, code=null}" +
                " is not a string");
        decodeModel("{}", ResponseError.class);
    }
}