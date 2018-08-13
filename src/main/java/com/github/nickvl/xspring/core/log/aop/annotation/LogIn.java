package com.github.nickvl.xspring.core.log.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.nickvl.xspring.core.log.aop.LogLevel;

/**
 * An annotation which will cause log statement for entering the method.
 * Notice that the default log level is INFO, but that the level can be set per annotation. See {@link #value()}}
 * @author m83522
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Logging
public @interface LogIn {

    /**
     * A constant used internally by the framework to indicate that a log statement should be created for both entering and exiting the annotated method.
     */
    LogPoint LOG_POINT = LogPoint.IN;

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

}
