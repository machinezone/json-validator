package com.mz.json.examples;

import com.mz.json.examples.model.complex.ComplexResponse;
import com.mz.json.validator.CheckException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.mz.json.helpers.ModelCoDec.decodeModel;
import static com.mz.json.helpers.ModelCoDec.encodeModel;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class ComplexResponseTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testValidModel() throws Exception {
        ComplexResponse response = decodeModel("{code=200, reason: OK, " +
                "shards: {s1: {commit-date: today, replicated: true}}, custom: {data: -1}}", ComplexResponse.class);
        assertEquals('{' +
                "shards={s1={commit-date=today, records-updated=0, mixed=null, failed=false, replicated=true}}, " +
                "custom={data=-1.0}, errors=null}", response.toString());
        assertEquals('{' +
                "\"shards\":{\"s1\":{\"records-updated\":0,\"commit-date\":\"today\",\"replicated\":true,\"failed\":false}}," +
                "\"custom\":{\"data\":-1.0}}", encodeModel(response));
    }

    @Test
    public void testInvalidModel() throws Exception {
        e.expect(CheckException.class);
        e.expectMessage("Property 'shards.s1.commit-date' of " +
                "com.mz.json.examples.model.complex.ComplexResponse{" +
                "shards={s1={commit-date=null, records-updated=0, mixed=null, failed=false, replicated=false}}, " +
                "custom=null, errors=null} " +
                "is null");
        decodeModel("{code=200, reason: OK, " +
                "shards: {s1: {}}}", ComplexResponse.class);
    }
}