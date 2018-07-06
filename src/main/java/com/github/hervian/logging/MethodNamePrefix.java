package com.github.hervian.logging;


/**
 * An enum with information that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter}
 * which will format the method name of the log entering and exiting statement accordingly.
 */
public enum MethodNamePrefix {

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements
     * wherein the method name is not prepended with any class name information.
     * I.e. the method name will appear as 'myMethod(...)'.
     */
    NONE,

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements
     * wherein the method name is prepended with the simply class name.
     * I.e. the method name will appear as 'MyClass.myMethod(...)'.
     */
    SIMPLE_CLASS_NAME,

    /**
     * A configuration that can be passed to the {@link com.github.nickvl.xspring.core.log.aop.LogAdapter} which will result in log entering statements
     * wherein the method name is prepended with the FQCN.
     * I.e. the method name will appear as 'com.mycompany.MyClass.myMethod(...)'.
     */
    FULLY_QUALIFIED_CLASS_NAME

}
