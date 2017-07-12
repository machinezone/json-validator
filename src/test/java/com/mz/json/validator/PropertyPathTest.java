package com.mz.json.validator;

import org.junit.Test;

import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;
import static org.junit.Assert.assertEquals;

public class PropertyPathTest {

    @Test
    public void testGetEmptyPath() throws Exception {
        assertEquals("Property '[!empty!]'",
                getEmptyPropertyPath().toString());
    }

    @Test
    public void testGetOneItemPath() throws Exception {
        assertEquals("Property 'a'",
                getEmptyPropertyPath().getAppendedInstance("a").toString());
    }

    @Test
    public void testGetTwoItemsPath() throws Exception {
        assertEquals("Property 'a.b'",
                getEmptyPropertyPath().getAppendedInstance("a").getAppendedInstance("b").toString());
    }
}