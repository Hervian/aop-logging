package com.github.hervian.logging;

/**
 * An enum with information that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter},
 * which will format the way method parameters are displayed.
 */
public enum ParameterStyle {

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements,
     * wherein the method parameters are presented as a comma separated list of: 'MyObject myObject=someValue'.
     */
    TYPE_NAME_VALUE,

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements,
     * wherein the method parameters are presented as a comma separated list of: 'MyObject myObject=someValue'.
     */
    NAME_VALUE,

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements,
     * wherein the method parameters are presented as a comma separated list of: 'MyObject'.
     */
    TYPE,

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements,
     * wherein the method parameters are presented as 'value1, value2' etc.
     */
    VALUE

}
