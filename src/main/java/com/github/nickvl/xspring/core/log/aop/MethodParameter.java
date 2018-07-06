package com.github.nickvl.xspring.core.log.aop;

/**
 * A bean to hold information about a method parameter type and the name as defined in the source code.
 */
public class MethodParameter {

    private final String parameterType;
    private final String parameterName;

    MethodParameter(String parameterType, String parameterName) {
        this.parameterType = parameterType;
        this.parameterName = parameterName == null ? "?" : parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(parameterType).append(" ").append(parameterName).toString();
    }

}
