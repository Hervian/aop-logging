/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.github.nickvl.xspring.core.log.aop.annotation.LogAround;
import com.github.nickvl.xspring.core.log.aop.annotation.LogDebug;
import com.github.nickvl.xspring.core.log.aop.annotation.LogError;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogFatal;
import com.github.nickvl.xspring.core.log.aop.annotation.LogIn;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.LogOut;
import com.github.nickvl.xspring.core.log.aop.annotation.LogPoint;
import com.github.nickvl.xspring.core.log.aop.annotation.LogTrace;
import com.github.nickvl.xspring.core.log.aop.annotation.LogWarn;

/**
 * Method descriptor.
 */
final class InvocationDescriptor {
    private final LogLevel beforeSeverity;
    private final LogLevel afterSeverity;
    private final LogException exceptionAnnotation;

    private InvocationDescriptor(LogLevel beforeSeverity, LogLevel afterSeverity, LogException exceptionAnnotation) {
        this.beforeSeverity = beforeSeverity;
        this.afterSeverity = afterSeverity;
        this.exceptionAnnotation = exceptionAnnotation;
    }

    public LogLevel getBeforeSeverity() {
        return beforeSeverity;
    }

    public LogLevel getAfterSeverity() {
        return afterSeverity;
    }

    public LogException getExceptionAnnotation() {
        return exceptionAnnotation;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private final Method method;
        private LogLevel beforeSeverity;
        private LogLevel afterSeverity;
        private LogLevel defaultSeverity;
        private LogLevel classBeforeSeverity;
        private LogLevel classAfterSeverity;
        private LogLevel classDefaultSeverity;

        Builder(Method method) {
            this.method = method;
        }

        public InvocationDescriptor build() {
            LogException logMethodExceptionAnnotation = parseAnnotations(method.getAnnotations(), true);
            LogException logClassExceptionAnnotation = parseAnnotations(method.getDeclaringClass().getAnnotations(), false);

            if (Utils.hasNotNull(beforeSeverity, defaultSeverity, afterSeverity)) {
                return new InvocationDescriptor(
                        Utils.coalesce(beforeSeverity, defaultSeverity),
                        Utils.coalesce(afterSeverity, defaultSeverity),
                        Utils.coalesce(logMethodExceptionAnnotation, logClassExceptionAnnotation));
            }

            return new InvocationDescriptor(
                    Utils.coalesce(classBeforeSeverity, classDefaultSeverity),
                    Utils.coalesce(classAfterSeverity, classDefaultSeverity),
                    Utils.coalesce(logMethodExceptionAnnotation, logClassExceptionAnnotation));

        }

        private LogException parseAnnotations(Annotation[] annotations, boolean fromMethod) {
            LogException logExceptionAnnotation = null;
            for (Annotation currAnnotation : annotations) {
                if (currAnnotation.annotationType().equals(LogException.class)) {
                    logExceptionAnnotation = (LogException) currAnnotation;
                } else if (currAnnotation.annotationType().equals(LogFatal.class)) {
                    LogFatal logAnnotation = (LogFatal) currAnnotation;
                    setSeverity(logAnnotation.value(), LogLevel.FATAL, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogError.class)) {
                    LogError logAnnotation = (LogError) currAnnotation;
                    setSeverity(logAnnotation.value(), LogLevel.ERROR, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogWarn.class)) {
                    LogWarn logAnnotation = (LogWarn) currAnnotation;
                    setSeverity(logAnnotation.value(), LogLevel.WARN, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogInfo.class)) {
                    LogInfo logAnnotation = (LogInfo) currAnnotation;
                    setSeverity(logAnnotation.value(), LogLevel.INFO, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogDebug.class)) {
                    LogDebug logAnnotation = (LogDebug) currAnnotation;
                    setSeverity(logAnnotation.value(), LogLevel.DEBUG, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogTrace.class)) {
                    LogTrace logAnnotation = (LogTrace) currAnnotation;
                    setSeverity(logAnnotation.value(), LogLevel.TRACE, fromMethod);
                } else if (currAnnotation.annotationType().equals(LogAround.class)) {
                    LogAround logAnnotation = (LogAround) currAnnotation;
                    setSeverity(LogAround.LOG_POINT, logAnnotation.value(), fromMethod);
                } else if (currAnnotation.annotationType().equals(LogIn.class)) {
                    LogIn logAnnotation = (LogIn) currAnnotation;
                    setSeverity(LogIn.LOG_POINT, logAnnotation.value(), fromMethod);
                } else if (currAnnotation.annotationType().equals(LogOut.class)) {
                    LogOut logAnnotation = (LogOut) currAnnotation;
                    setSeverity(LogOut.LOG_POINT, logAnnotation.value(), fromMethod);
                }
            }
            return logExceptionAnnotation;
        }

        private void setSeverity(LogPoint logPoint, LogLevel targetSeverity, boolean fromMethod) {
            if (fromMethod) {
                if (logPoint == LogPoint.IN) {
                    beforeSeverity = Utils.max(targetSeverity, beforeSeverity);
                } else if (logPoint == LogPoint.OUT) {
                    afterSeverity = Utils.max(targetSeverity, afterSeverity);
                } else if (logPoint == LogPoint.BOTH) {
                    defaultSeverity = Utils.max(targetSeverity, defaultSeverity);
                }
            } else {
                if (logPoint == LogPoint.IN) {
                    classBeforeSeverity = Utils.max(targetSeverity, classBeforeSeverity);
                } else if (logPoint == LogPoint.OUT) {
                    classAfterSeverity = Utils.max(targetSeverity, classAfterSeverity);
                } else if (logPoint == LogPoint.BOTH) {
                    classDefaultSeverity = Utils.max(targetSeverity, classDefaultSeverity);
                }
            }
        }

    }
}
