/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.hervian.logging;

import org.apache.commons.logging.Log;

import com.github.nickvl.xspring.core.log.aop.ArgumentDescriptor;
import com.github.nickvl.xspring.core.log.aop.LogAdapter;

/**
 * Defines log strategies.
 */
public abstract class LogStrategy {

    private final LogAdapter logAdapter;

    LogStrategy(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
    }

    protected LogAdapter getLogAdapter() {
        return logAdapter;
    }

    /**
     * If current strategy logging enabled.
     *
     * @param logger current logger
     * @return <code>true</code> if logging enabled, otherwise <code>false</code>
     */
    public abstract boolean isLogEnabled(Log logger);

    /**
     * Logs calling of the method.
     *
     * @param logger current logger
     * @param method method name
     * @param args arguments of the method
     * @param argumentDescriptor argument descriptor
     */
    public abstract void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor);

    /**
     * Logs returning from the method.
     *
     * @param logger current logger
     * @param method method name
     * @param argCount parameter count number of the method
     * @param result returned result of the method
     * @param argumentDescriptor argument descriptor
     */
    public abstract void logAfter(Log logger, String method, int argCount, Object result, ArgumentDescriptor argumentDescriptor);

    /**
     * Logs throwing exception from the method.
     *
     * @param logger current logger
     * @param method method name
     * @param argCount parameter count number of the method
     * @param e exception thrown from the method
     * @param stackTrace if stack trace should be logged
     */
    public abstract void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace);

}
