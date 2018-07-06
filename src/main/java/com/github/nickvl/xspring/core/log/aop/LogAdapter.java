/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import org.apache.commons.logging.Log;

/**
 * Declares access to the logger and log message creation.
 */
public interface LogAdapter {
    /**
     *
     * @param clazz asdf
     * @return asdf
     */
    Log getLog(Class clazz);

    /**
     *
     * @param name asdf
     * @return sadf
     */
    Log getLog(String name);

    /**
     *
     * @param method asdf
     * @param args asdf
     * @param argumentDescriptor asdf
     * @return asdf
     */
    Object toMessage(String method, Object[] args, ArgumentDescriptor argumentDescriptor);

    /**
     *
     * @param method asdf
     * @param argCount asdf
     * @param result asdf
     * @param argumentDescriptor asdf
     * @return asdf
     */
    Object toMessage(String method, int argCount, Object result, ArgumentDescriptor argumentDescriptor);

    /**
     *
     * @param method asdf
     * @param argCount asdf
     * @param e asdf
     * @param stackTrace asdf
     * @return asdf
     */
    Object toMessage(String method, int argCount, Exception e, boolean stackTrace);

}
