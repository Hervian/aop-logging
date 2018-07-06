package com.github.nickvl.xspring.core.log.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nickvl.xspring.core.log.aop.LogLevel;

/**
 * An annotation which will cause log statements for both entering and exiting the method.
 * Notice that the default log level is INFO, but that the level can be set per annotation. See {@link #value()}}
 * @author m83522
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Logging
public @interface LogAround {

    /**
     * An enum value indicating that we a log statement should be created for both entering and exiting the method.
     */
    LogPoint LOG_POINT = LogPoint.BOTH;

    /**
     *
     * The log level.
     * @return The log level.
     */
    LogLevel value() default LogLevel.INFO;

    /**
     * @return whether or not to log the instance, on which we are calling this method.
     * Notice that this annotation type will have no effect if the method annotated is static.
     */
    boolean logThis() default false;

    /**
     *
     * Whether or not to include the content of the return value,
     * i.e. if the value of the method's return variable should be included in the log.
     * @return Whether or not to include the content of the return value,
     * i.e. if the value of the method's return variable should be included in the log.
     */
    boolean logResult() default false;

    /**
     * @return true if all method parameters should be logged.
     * Intended as a quicker way to log all method parameters than annotating each of them with {@link Lp}.
     */
    boolean logAllParams() default false;
}
