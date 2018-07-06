/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link com.github.nickvl.xspring.core.log.aop.ArgumentDescriptor}.
 */
public class ArgumentDescriptorTestCase {

    private LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Method getMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        for (Method method : clazz.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        throw new NoSuchMethodException(methodName);
    }

    @Test
    public void testNoArguments() throws Exception {
        Method method = getMethod(getClass(), "noArguments");
        ArgumentDescriptor descriptor = new ArgumentDescriptor.Builder(method, method.getParameterTypes().length, parameterNameDiscoverer, this).build();
        assertEquals(-1, descriptor.nextArgumentIndex(0));
        assertNull(descriptor.getMethodParameters());
    }

    private void noArguments() {
        // used in test methods viq reflection
    }

    @Test
    public void testOneArgument() throws Exception {
        Method method = getMethod(getClass(), "oneArgument");
        ArgumentDescriptor descriptor = new ArgumentDescriptor.Builder(method, method.getParameterTypes().length, parameterNameDiscoverer, this).build();
        assertEquals(0, descriptor.nextArgumentIndex(0));
        assertTrue(descriptor.isArgumentIndex(0));
        assertEquals(-1, descriptor.nextArgumentIndex(1));
        assertEquals("String foo", descriptor.getMethodParameters()[0].toString());
    }

    private void oneArgument(@Lp String foo) {
        // used in test methods viq reflection
    }

    @Test
    public void testTwoArguments() throws Exception {
        Method method = getMethod(getClass(), "twoArguments");
        ArgumentDescriptor descriptor = new ArgumentDescriptor.Builder(method, method.getParameterTypes().length, parameterNameDiscoverer, this).build();
        assertEquals(0, descriptor.nextArgumentIndex(0));
        assertTrue(descriptor.isArgumentIndex(0));
        assertEquals(-1, descriptor.nextArgumentIndex(1));
        assertEquals("String foo", descriptor.getMethodParameters()[0].toString());
    }

    private void twoArguments(@Lp String foo, String foo2) {
        // used in test methods viq reflection
    }


    @Test
    public void testVarArguments() throws Exception {
        Method method = getMethod(getClass(), "varArguments");
        ArgumentDescriptor descriptor = new ArgumentDescriptor.Builder(method, method.getParameterTypes().length, parameterNameDiscoverer, this).build();
        assertEquals(1, descriptor.nextArgumentIndex(0));
        assertFalse(descriptor.isArgumentIndex(0));
        assertTrue(descriptor.isArgumentIndex(1));
        assertEquals(1, descriptor.nextArgumentIndex(1));
        assertEquals(-1, descriptor.nextArgumentIndex(2));
        assertEquals("String[] foo2", descriptor.getMethodParameters()[1].toString());
    }

    private void varArguments(String foo, @Lp String... foo2) {
        // used in test methods viq reflection
    }

}
