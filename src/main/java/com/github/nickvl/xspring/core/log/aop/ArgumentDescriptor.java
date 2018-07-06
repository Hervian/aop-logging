/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Optional;

import org.springframework.core.ParameterNameDiscoverer;

import com.github.nickvl.xspring.core.log.aop.annotation.LogAround;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

/**
 * Method arguments descriptor.
 */
public final class ArgumentDescriptor {
    private final BitSet loggedValueIndexes;
    private final LogValuesDescriptor logValuesDescriptor;
    private final MethodParameter[] methodParameters;
    private final Optional<Object> instance;

    private ArgumentDescriptor(BitSet loggedValueIndexes, MethodParameter[] methodParameters, Object instance, LogValuesDescriptor logValuesDescriptor) {
        this.loggedValueIndexes = loggedValueIndexes;
        this.methodParameters = methodParameters;
        this.logValuesDescriptor = logValuesDescriptor;
        this.instance = Optional.ofNullable(instance);
    }

    /**
     *
     * @param i the index into the list of method parameters.
     * @return the next index of the method arguments to be logged.
     */
    public int nextArgumentIndex(int i) {
        return loggedValueIndexes.nextSetBit(i);
    }

    /**
     * @param i the index into the list of method parameters.
     * @return awhether or not this method argument should be logged.
     */
    public boolean isArgumentIndex(int i) {
        return loggedValueIndexes.get(i);
    }

    /**
     *
     * @return whether or not the method's return value should be logged.
     */
    public boolean logResult() {
        return logValuesDescriptor != null && logValuesDescriptor.isLogResult();
    }

    /**
     *
     * @return whether or not the instance should be logged.
     */
    public boolean logThis() {
        return logValuesDescriptor != null && logValuesDescriptor.isLogThis();
    }

    /**
     * Gets names of method parameters.
     *
     * @return all parameter names or <code>null</code> if the method has no parameters or the names can not be discovered
     */
    public MethodParameter[] getMethodParameters() {
        return methodParameters;
    }

    public Optional<Object> getInstance() {
        return instance;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private static final ArgumentDescriptor NO_ARGUMENTS_DESCRIPTOR
            = new ArgumentDescriptor(new BitSet(0), null, null, new LogValuesDescriptor());
        private final Method method;
        private final int argumentCount;
        private final ParameterNameDiscoverer parameterNameDiscoverer;
        private final Object instance;

        Builder(Method method, int argumentCount, ParameterNameDiscoverer parameterNameDiscoverer, Object instance) {
            this.method = method;
            this.argumentCount = argumentCount;
            this.parameterNameDiscoverer = parameterNameDiscoverer;
            this.instance = instance;
        }

        /**
         *
         * @return constructor
         */
        public ArgumentDescriptor build() {
            if (argumentCount == 0) {
                return NO_ARGUMENTS_DESCRIPTOR;
            }

            MethodParameter[] methodParameters = getArrayOfMethodParameters();

            BitSet lpParameters = getMethodParameters(Lp.class);

            LogValuesDescriptor logValuesDescriptor = getParamsToLog();

            return new ArgumentDescriptor(lpParameters, methodParameters, instance, logValuesDescriptor);
        }

        private MethodParameter[] getArrayOfMethodParameters() {
            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] argNames = parameterNameDiscoverer.getParameterNames(method);

            MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                methodParameters[i] = new MethodParameter(parameterTypes[i].getSimpleName(), argNames == null ? null : argNames[i]);
            }
            return methodParameters;
        }

        private <T> BitSet getMethodParameters(Class<T> annotationMarker) {
            Annotation[][] annotations = method.getParameterAnnotations();
            BitSet result = new BitSet(annotations.length);
            for (int i = 0; i < annotations.length; i++) {
                Annotation[] paramAnnotations = annotations[i];
                for (Annotation currAnnotation : paramAnnotations) {
                    if (currAnnotation.annotationType().equals(annotationMarker)) {
                        result.set(i);
                    }
                }
            }
            return result;
        }

        private LogValuesDescriptor getParamsToLog() {
            boolean logResult = false, logThis = false;
            for (Annotation annotation: method.getAnnotations()) {
                if (annotation.annotationType().equals(LogAround.class)) {
                    LogAround logAnno = ((LogAround) annotation);
                    logResult = logAnno.logResult();
                    logThis = logAnno.logThis();
                }
            }
            return new LogValuesDescriptor(logResult, logThis);
        }

    }

}
