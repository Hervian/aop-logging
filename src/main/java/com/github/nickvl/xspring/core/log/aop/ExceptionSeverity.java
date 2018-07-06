/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

/**
 * A holder for a pair.
 */
final class ExceptionSeverity {
    private final LogLevel severity;
    private final boolean stackTrace;

    private ExceptionSeverity(LogLevel severity, boolean stackTrace) {
        this.severity = severity;
        this.stackTrace = stackTrace;
    }

    public static ExceptionSeverity create(LogLevel severity, boolean stackTrace) {
        return new ExceptionSeverity(severity, stackTrace);
    }

    public LogLevel getSeverity() {
        return severity;
    }

    public boolean getStackTrace() {
        return stackTrace;
    }
}
