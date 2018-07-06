/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.nickvl.xspring.core.log.aop.annotation.LogException;

/**
 * Method exceptions descriptor.
 */
final class ExceptionDescriptor {

    private final Map<Class<? extends Exception>, ExceptionSeverity> exceptionSeverity;

    private ExceptionDescriptor(Map<Class<? extends Exception>, ExceptionSeverity> exceptionSeverity) {
        this.exceptionSeverity = exceptionSeverity;
    }

    public Collection<Class<? extends Exception>> getDefinedExceptions() {
        return exceptionSeverity.keySet();
    }

    public ExceptionSeverity getExceptionSeverity(Class<? extends Exception> resolvedException) {
        return exceptionSeverity.get(resolvedException);
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private final LogException exceptionAnnotation;
        private final Map<Class<? extends Exception>, ExceptionSeverity> map = new HashMap<Class<? extends Exception>, ExceptionSeverity>();

        Builder(LogException exceptionAnnotation) {
            this.exceptionAnnotation = exceptionAnnotation;
        }

        public ExceptionDescriptor build() {
            setSeverity(exceptionAnnotation.fatal(), LogLevel.FATAL);
            setSeverity(exceptionAnnotation.value(), LogLevel.ERROR);
            setSeverity(exceptionAnnotation.warn(), LogLevel.WARN);
            setSeverity(exceptionAnnotation.info(), LogLevel.INFO);
            setSeverity(exceptionAnnotation.debug(), LogLevel.DEBUG);
            setSeverity(exceptionAnnotation.trace(), LogLevel.TRACE);
            return new ExceptionDescriptor(map);
        }

        private void setSeverity(LogException.Exc[] exceptionGroups, LogLevel targetSeverity) {
            for (LogException.Exc exceptions : exceptionGroups) {
                for (Class<? extends Exception> clazz : exceptions.value()) {
                    ExceptionSeverity descriptor = map.get(clazz);
                    if (descriptor == null || Utils.greater(targetSeverity, descriptor.getSeverity())) {
                        map.put(clazz, ExceptionSeverity.create(targetSeverity, exceptions.stacktrace()));
                    }
                }
            }

        }

    }


}
