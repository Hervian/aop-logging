/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.hervian.logging.MethodNamePrefix;
import com.github.hervian.logging.ParameterStyle;

/**
 * Abstract log adapter.
 */
abstract class AbstractLogAdapter implements LogAdapter {

    private static final String THROWING = "throwing: ";

    /**
     * an enum to hold configured information about the format of the log entering string.
     */
    private final MethodNamePrefix methodNamePrefix;

    /**
     *
     * an enum to hold configured information about the format of the log entering string.
     */
    private final ParameterStyle parameterStyle;

    private final String entryString;
    private final String exitString;


    /**
     *
     * @param methodNamePrefix asdf
     * @param entryString asdf
     * @param exitString asdf
     * @param parameterStyle asdf
     */
    AbstractLogAdapter(MethodNamePrefix methodNamePrefix, String entryString, String exitString, ParameterStyle parameterStyle) {
        this.methodNamePrefix = methodNamePrefix == null ? MethodNamePrefix.NONE : methodNamePrefix;
        this.entryString = entryString == null ? "calling: " : entryString;
        this.exitString = exitString == null ? "returning: " : exitString;
        this.parameterStyle = parameterStyle == null ? ParameterStyle.TYPE_NAME_VALUE : parameterStyle;
    }

    @Override
    public Log getLog(Class clazz) {
        return LogFactory.getLog(clazz);
    }

    @Override
    public Log getLog(String name) {
        return LogFactory.getLog(name);
    }

    @Override
    public Object toMessage(String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
        StringBuilder buff = new StringBuilder(entryString).append(' ');
        logMethodNameAndParameters(method, args, argumentDescriptor, buff);

        logThis(argumentDescriptor, buff);

        return buff.toString();
    }

    private void logMethodNameAndParameters(String method, Object[] args, ArgumentDescriptor argumentDescriptor,
            StringBuilder buff) {
        addMethodName(argumentDescriptor, buff);
        buff.append(method).append('(');

        MethodParameter[] methodParameters = argumentDescriptor.getMethodParameters();

        //Construct signature and list of passed parameters, if any:
        if (args.length > 0) {
            String[] arrayOfMethodParameterTypeAndNames = new String[methodParameters.length];
            appendParametersToEntryMethodString(args, argumentDescriptor, buff, methodParameters,
                    arrayOfMethodParameterTypeAndNames);
        }
        if (args.length > 0) {
          //Cut off the last ', ' which was added in last loop iteration
            buff.setLength(buff.length() - 2);
        }
        buff.append(")");
    }

    private void logThis(ArgumentDescriptor argumentDescriptor, StringBuilder buff) {
        if (argumentDescriptor.logThis() && argumentDescriptor.getInstance().isPresent()) {
            Object thiz = argumentDescriptor.getInstance().get();
            buff.append(", ").append(thiz.getClass().getSimpleName()).append(".this=").append(thiz);
        }
    }

    private void addMethodName(ArgumentDescriptor argumentDescriptor, StringBuilder buff) {
        switch (methodNamePrefix) {
        case FULLY_QUALIFIED_CLASS_NAME:
            buff.append(argumentDescriptor.getEnclosingClass().getName()).append('.');
            break;
        case NONE:
            break;
        case SIMPLE_CLASS_NAME:
            buff.append(argumentDescriptor.getEnclosingClass().getSimpleName()).append('.');
            break;
        default:
            break;

        }
    }

    private void appendParametersToEntryMethodString(Object[] args, ArgumentDescriptor argumentDescriptor,
            StringBuilder buff, MethodParameter[] methodParameters, String[] arrayOfMethodParameterTypeAndNames) {
        appendParametersToEntryMethodString(args, argumentDescriptor, buff, methodParameters,
                arrayOfMethodParameterTypeAndNames, parameterStyle);
    }

    private void appendParametersToEntryMethodString(Object[] args, ArgumentDescriptor argumentDescriptor,
            StringBuilder buff, MethodParameter[] methodParameters, String[] arrayOfMethodParameterTypeAndNames, ParameterStyle passedParameterStyle) {
        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter methodParameter = methodParameters[i];
            ParameterStyle localParameterStyle = passedParameterStyle;
            do { // http://clav.cz/java-switch-with-goto-case/
                switch (localParameterStyle) {
                case TYPE_NAME_VALUE:
                    buff.append(methodParameter.getParameterType()).append(' ');
                    localParameterStyle = ParameterStyle.NAME_VALUE;
                    continue;
                case NAME_VALUE:
                    buff.append(methodParameter.getParameterName()).append("=");
                    localParameterStyle = ParameterStyle.VALUE;
                    continue;
                case TYPE:
                    buff.append(methodParameter.getParameterType());
                    break;
                case VALUE:
                    buff.append(argumentDescriptor.isArgumentIndex(i) ? asString(args[i]) : "?");
                    break;
                default:
                    throw new UnsupportedOperationException("Unmapped enum");
                }
                break;
            } while (true);
            buff.append(", ");
        }
    }

    @Override
    public Object toMessage(String method, Object[] args, Object result, ArgumentDescriptor argumentDescriptor) {
//        StringBuilder buff = new StringBuilder(exitString).append(' ').append(method);
        StringBuilder buff = new StringBuilder(exitString).append(' ');
        logMethodNameAndParameters(method, args, argumentDescriptor, buff);

        buff.append(": ").append(returnValueAsString(argumentDescriptor, result));

        logThis(argumentDescriptor, buff);

        return buff.toString();
//        return buff.append('(').append(argCount).append(" arguments): ").append(returnValueAsString(argumentDescriptor, result)).toString();
    }

    private String returnValueAsString(ArgumentDescriptor argumentDescriptor, Object result) {
        return argumentDescriptor.logResult() ? asString(result) : "?";
    }

    @Override
    public Object toMessage(String method, int argCount, Exception e, boolean stackTrace) {
        String message;
        if (argCount == 0) {
            message = THROWING + method + "():" + e.getClass();
        } else {
            message = THROWING + method + '(' + argCount + " arguments):" + e.getClass();
        }
        if (e.getMessage() != null) {
            message += '=' + e.getMessage();
        }
        return message;
    }

    protected abstract String asString(Object value);

}
