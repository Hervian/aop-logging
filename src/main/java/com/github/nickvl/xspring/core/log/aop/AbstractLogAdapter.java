/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract log adapter.
 */
abstract class AbstractLogAdapter implements LogAdapter {

    private static final String CALLING = "calling: ";
    private static final String RETURNING = "returning: ";
    private static final String THROWING = "throwing: ";

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
        if (args.length == 0) {
            return CALLING + method + "()";
        }

        MethodParameter[] methodParameters = argumentDescriptor.getMethodParameters();
        StringBuilder buff = new StringBuilder(CALLING);

        //Construct signature and list of passed parameters, if any:
        buff.append(method).append('(');
        if (args.length < 1) {
            buff.append(")");
        } else {
            String[] arrayOfMethodParameterTypeAndNames = new String[methodParameters.length];
            for (int i = 0; i < methodParameters.length; i++) {
                arrayOfMethodParameterTypeAndNames[i] = methodParameters[i].toString();
            }
            buff.append(String.join(",", arrayOfMethodParameterTypeAndNames));
            buff.append("): ");

            //append the values of the parameters passed to the method:
            for (int i = 0; i < args.length; i++) {
                String parameterName = methodParameters[i].getParameterName();
                if (parameterName != null && parameterName != "?") {
                    buff.append(parameterName).append('=');
                }
                buff.append(argumentDescriptor.isArgumentIndex(i) ? asString(args[i]) : "?");
                buff.append(", ");
            }

            //Cut off the last ', ' which was added in last loop iteration
            if (argumentDescriptor.nextArgumentIndex(0) != -1) {
                buff.setLength(buff.length() - 2);
            }
        }

        if (argumentDescriptor.logThis() && argumentDescriptor.getInstance().isPresent()) {
            Object thiz = argumentDescriptor.getInstance().get();
            buff.append(", ").append(thiz.getClass().getSimpleName()).append(".this=").append(thiz);
        }

        return buff.toString();
    }

    @Override
    public Object toMessage(String method, int argCount, Object result, ArgumentDescriptor argumentDescriptor) {
        StringBuilder buff = new StringBuilder(RETURNING).append(method);
        if (argCount == 0) {
            return buff.append("():").append(returnValueAsString(argumentDescriptor, result)).toString();
        }
        return buff.append('(').append(argCount).append(" arguments): ").append(returnValueAsString(argumentDescriptor, result)).toString();
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
