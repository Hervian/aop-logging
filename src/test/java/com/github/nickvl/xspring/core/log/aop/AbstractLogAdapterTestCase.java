/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.io.IOException;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

import com.github.nickvl.xspring.core.log.aop.TestSupportUtility;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link AbstractLogAdapter}.
 */
public class AbstractLogAdapterTestCase {

    private LogAdapter logAdapter;

    @Before
    public void setUp() throws Exception {
        logAdapter = new AbstractLogAdapter() {
            @Override
            protected String asString(Object value) {
                return String.valueOf(value);
            }
        };
    }

    @Test
    public void testGetLogByClass() throws Exception {
        assertNotNull(logAdapter.getLog(this.getClass()));
    }

    @Test
    public void testGetLogByName() throws Exception {
        assertNotNull(logAdapter.getLog("custom-logger"));
    }

    @Test
    public void testToMessageBeforeNoArgs() throws Exception {
        BitSet indexesToLog = new BitSet();
        indexesToLog.set(0);
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(indexesToLog, null, false, false, this);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{}, descriptor);
        assertEquals("calling: fooMethod()", message);
    }

    @Test
    public void testToMessageBeforeOneArg() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "first")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, this, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1"}, descriptor);
        assertEquals("calling: fooMethod(String first): first=v1", message);
    }

    @Test
    public void testToMessageBeforeOneArgNoName() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "v1")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, this, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1"}, descriptor);
        assertEquals("calling: fooMethod(String v1): v1=v1", message);
    }

    @Test
    public void testToMessageBeforeSomeArgs() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "first"), new MethodParameter("String", "second")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, this, false, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
//        assertEquals("calling: fooMethod(2 arguments: first=?, second=v2)", message);
        assertEquals("calling: fooMethod(String first,String second): first=?, second=v2", message);
    }

    @Test
    public void testToMessageBeforeSomeArgsNoNames() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "v1"), new MethodParameter("String", "v2")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, this, false, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
//        assertEquals("calling: fooMethod(2 arguments: ?, v2)", message);
        assertEquals("calling: fooMethod(String v1,String v2): v1=?, v2=v2", message);
    }

    @Test
    public void testToMessageBeforeAllArgs() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "first"), new MethodParameter("String", "second")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, this, true, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
//        assertEquals("calling: fooMethod(2 arguments: first=v1, second=v2)", message);
        assertEquals("calling: fooMethod(String first,String second): first=v1, second=v2", message);
    }

    @Test
    public void testToMessageBeforeAllArgsNoNames() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "v1"), new MethodParameter("String", "v2")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, this, true, true);
        Object message = logAdapter.toMessage("fooMethod", new Object[]{"v1", "v2"}, descriptor);
//        assertEquals("calling: fooMethod(2 arguments: v1, v2)", message);
        assertEquals("calling: fooMethod(String v1,String v2): v1=v1, v2=v2", message);
    }

    @Test
    public void testToMessageAfterNoArgs() throws Exception {
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(null, true, false, this, true, true);
        Object message = logAdapter.toMessage("fooMethod", 0, "res", descriptor);
        assertEquals("returning: fooMethod():res", message);
    }

    @Test
    public void testToMessageAfterNoArgsVoid() throws Exception {
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(null, true, false, this, true, true);
        Object message = logAdapter.toMessage("fooMethod", 0, Void.TYPE, descriptor);
        assertEquals("returning: fooMethod():void", message);
    }

    @Test
    public void testToMessageAfter() throws Exception {
        MethodParameter[] arNames = {new MethodParameter("String", "res")};
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(arNames, true, true, this, true);
        Object message = logAdapter.toMessage("fooMethod", 2, "res", descriptor);
        assertEquals("returning: fooMethod(2 arguments): res", message);
    }

    @Test
    public void testToMessageAfterVoid() throws Exception {
        ArgumentDescriptor descriptor = TestSupportUtility.createArgumentDescriptor(null, true, false, this, true, true);
        Object message = logAdapter.toMessage("fooMethod", 2, Void.TYPE, descriptor);
        assertEquals("returning: fooMethod(2 arguments): void", message);
    }

    @Test
    public void testToMessageExceptionNoArgs() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 0, new IOException("storage disconnected"), false);
        assertEquals("throwing: fooMethod():class java.io.IOException=storage disconnected", message);
    }

    @Test
    public void testToMessageException() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 2, new IllegalArgumentException("second is negative"), false);
        assertEquals("throwing: fooMethod(2 arguments):class java.lang.IllegalArgumentException=second is negative", message);
    }

    @Test
    public void testToMessageExceptionNullMessage() throws Exception {
        Object message = logAdapter.toMessage("fooMethod", 2, new Exception(), false);
        assertEquals("throwing: fooMethod(2 arguments):class java.lang.Exception", message);
    }
}