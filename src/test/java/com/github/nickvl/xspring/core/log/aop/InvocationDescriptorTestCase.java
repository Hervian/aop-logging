/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;

import com.github.nickvl.xspring.core.log.aop.annotation.LogDebug;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.LogPoint;
import com.github.nickvl.xspring.core.log.aop.annotation.LogTrace;
import com.github.nickvl.xspring.core.log.aop.annotation.LogWarn;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests {@link InvocationDescriptor} with log annotated methods.
 */
public class InvocationDescriptorTestCase {

    private Method currMethod;

    @Rule
    public MethodRule watchman = new TestWatchman() {
        public void starting(FrameworkMethod method) {
            currMethod = method.getMethod();
        }
    };

    @After
    public void tearDown() throws Exception {
        currMethod = null;
    }

    @Test
    public void testNoAnnotations() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertNull(descriptor.getBeforeSeverity());
        assertNull(descriptor.getAfterSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogDebug(LogPoint.IN)
    public void testGetBeforeSeverity() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.DEBUG, descriptor.getBeforeSeverity());
        assertNull(descriptor.getAfterSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogInfo(LogPoint.IN)
    @LogDebug(LogPoint.IN)
    public void testGetBeforeSeverityByPriority() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.INFO, descriptor.getBeforeSeverity());
        assertNull(descriptor.getAfterSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogDebug(LogPoint.OUT)
    public void testGetAfterSeverity() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.DEBUG, descriptor.getAfterSeverity());
        assertNull(descriptor.getBeforeSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogInfo(LogPoint.OUT)
    @LogDebug(LogPoint.OUT)
    public void testGetAfterSeverityByPriority() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.INFO, descriptor.getAfterSeverity());
        assertNull(descriptor.getBeforeSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogDebug
    public void testGetSeverity() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.DEBUG, descriptor.getBeforeSeverity());
        assertSame(LogLevel.DEBUG, descriptor.getAfterSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogWarn
    @LogInfo(LogPoint.OUT)
    @LogDebug(LogPoint.IN)
    @LogTrace
    public void testGetSeverityByPriority() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.DEBUG, descriptor.getBeforeSeverity());
        assertSame(LogLevel.INFO, descriptor.getAfterSeverity());
        assertNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogException
    public void testGetExceptionAnnotation() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertNull(descriptor.getBeforeSeverity());
        assertNull(descriptor.getAfterSeverity());
        assertNotNull(descriptor.getExceptionAnnotation());
    }

    @Test
    @LogInfo
    @LogException
    public void testGetAll() throws Exception {
        InvocationDescriptor descriptor = new InvocationDescriptor.Builder(currMethod).build();
        assertSame(LogLevel.INFO, descriptor.getBeforeSeverity());
        assertSame(LogLevel.INFO, descriptor.getAfterSeverity());
        assertNotNull(descriptor.getExceptionAnnotation());
    }
}
