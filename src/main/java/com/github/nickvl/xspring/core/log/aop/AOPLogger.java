/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import com.github.hervian.logging.Logger;


/**
 * Logger aspect.
 */
@Aspect
public class AOPLogger implements InitializingBean {
    // private static final Log LOGGER = LogFactory.getLog(AOPLogger.class);
    private LogAdapter logAdapter;
    private Logger genericLogger;
    private final LocalVariableTableParameterNameDiscoverer localVariableNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ExceptionResolver exceptionResolver = new ExceptionResolver();
    private final ConcurrentMap<Method, MethodDescriptor> cache = new ConcurrentHashMap<Method, MethodDescriptor>();

    @Override
    public void afterPropertiesSet() throws Exception {
    }

//    /**
//     *
//     * @param logAdapter constructor
//     */
//    public AOPLogger(LogAdapter logAdapter) {
//        this.logAdapter = logAdapter;
//
//    }

    /**
     *
     * @param logAdapter asdf
     */
    public void setLogAdapter(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
        genericLogger = new Logger(logAdapter);
    }


    /**
     * Advise. Logs the advised method.
     *
     * @param joinPoint represents advised method
     * @return method execution result
     * @throws Throwable in case of exception
     */
    @Around(value = "execution(@(@com.github.nickvl.xspring.core.log.aop.annotation.Logging *) * *.* (..))" // per method
            + " || execution(* (@(@com.github.nickvl.xspring.core.log.aop.annotation.Logging *) *).*(..))"  // per class
            , argNames = "joinPoint")
    public Object logTheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Log logger = logAdapter.getLog(joinPoint.getTarget().getClass());
        Method method = extractMethod(joinPoint);

        MethodDescriptor descriptor = getMethodDescriptor(method);
        InvocationDescriptor invocationDescriptor = descriptor.getInvocationDescriptor();

        String methodName = method.getName();

        ArgumentDescriptor argumentDescriptor = getArgumentDescriptor(descriptor, method, args.length, joinPoint.getTarget().getClass(), joinPoint.getThis());
        if (beforeLoggingOn(invocationDescriptor, logger)) {
            genericLogger.logBefore(logger, methodName, args, argumentDescriptor, invocationDescriptor.getBeforeSeverity());
        }

        Object result;
        if (invocationDescriptor.getExceptionAnnotation() == null) {
            result = joinPoint.proceed(args);
        } else {
            try {
                result = joinPoint.proceed(args);
            } catch (Exception e) {
                ExceptionDescriptor exceptionDescriptor = getExceptionDescriptor(descriptor, invocationDescriptor);
                Class<? extends Exception> resolved = exceptionResolver.resolve(exceptionDescriptor, e);
                if (resolved != null) {
                    ExceptionSeverity excSeverity = exceptionDescriptor.getExceptionSeverity(resolved);
                    if (isLoggingOn(excSeverity.getSeverity(), logger)) {
                        genericLogger.logException(logger, methodName, args.length, e, excSeverity.getStackTrace(), excSeverity.getSeverity());
                    }
                }
                throw e;
            }
        }
        if (afterLoggingOn(invocationDescriptor, logger)) {
            Object loggedResult = (method.getReturnType() == Void.TYPE) ? Void.TYPE : result;
            genericLogger.logAfter(logger, methodName, args, loggedResult, argumentDescriptor, invocationDescriptor.getAfterSeverity());
        }
        return result;
    }

    private MethodDescriptor getMethodDescriptor(Method method) {
        MethodDescriptor cached = cache.get(method);
        if (cached != null) {
            return cached;
        }
        cached = new MethodDescriptor(new InvocationDescriptor.Builder(method).build());
        MethodDescriptor prev = cache.putIfAbsent(method, cached);
        return prev == null ? cached : prev;
    }

    private ArgumentDescriptor getArgumentDescriptor(MethodDescriptor descriptor, Method method, int argumentCount, Class<?> enclosingClass, Object instance) {
        if (descriptor.getArgumentDescriptor() != null) {
            return descriptor.getArgumentDescriptor();
        }
        ArgumentDescriptor argumentDescriptor = new ArgumentDescriptor.Builder(method, argumentCount, localVariableNameDiscoverer, enclosingClass, instance).build();
        descriptor.setArgumentDescriptor(argumentDescriptor);
        return argumentDescriptor;
    }

    private ExceptionDescriptor getExceptionDescriptor(MethodDescriptor descriptor, InvocationDescriptor invocationDescriptor) {
        if (descriptor.getExceptionDescriptor() != null) {
            return descriptor.getExceptionDescriptor();
        }
        ExceptionDescriptor exceptionDescriptor = new ExceptionDescriptor.Builder(invocationDescriptor.getExceptionAnnotation()).build();
        descriptor.setExceptionDescriptor(exceptionDescriptor);
        return exceptionDescriptor;
    }

    private Method extractMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // signature.getMethod() points to method declared in interface. it is not suit to discover arg names and arg annotations
        // see AopProxyUtils: org.springframework.cache.interceptor.CacheAspectSupport#execute(CacheAspectSupport.Invoker, Object, Method, Object[])
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (Modifier.isPublic(signature.getMethod().getModifiers())) {
            return targetClass.getMethod(signature.getName(), signature.getParameterTypes());
        } else {
            return ReflectionUtils.findMethod(targetClass, signature.getName(), signature.getParameterTypes());
        }
    }

    private boolean beforeLoggingOn(InvocationDescriptor descriptor, Log logger) {
        return isLoggingOn(descriptor.getBeforeSeverity(), logger);
    }

    private boolean afterLoggingOn(InvocationDescriptor descriptor, Log logger) {
        return isLoggingOn(descriptor.getAfterSeverity(), logger);
    }

    private boolean isLoggingOn(LogLevel severity, Log logger) {
        return severity != null && genericLogger.isLogEnabled(logger, severity);
    }
}
