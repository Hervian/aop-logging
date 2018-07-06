package com.github.nickvl.xspring.core.log.aop;

/**
 * A bean holding information about what should be logged.
 */
public class LogValuesDescriptor {


    private final boolean logResult;
    private final boolean logThis;

    /**
     * Default constructor. In current program flow this method is only called when there are no arguments to work on.
     */
    public LogValuesDescriptor() {
        this(false, false);
    }

    /**
     * Constructor setting information about what should be logged.
     * @param logResult a boolean indicating whether or not the method's return value should be logged.
     * @param logThis a boolean indicating whether or not the instance method should be logged.
     */
    public LogValuesDescriptor(boolean logResult, boolean logThis) {
        this.logResult = logResult;
        this.logThis = logThis;
    }


    public boolean isLogResult() {
        return logResult;
    }

    public boolean isLogThis() {
        return logThis;
    }


}
