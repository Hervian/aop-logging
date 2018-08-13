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
import com.github.nickvl.xspring.core.log.aop.annotation.LogIn;
import com.github.nickvl.xspring.core.log.aop.annotation.LogOut;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

/**
 * Method arguments descriptor.
 */
public final class ArgumentDescriptor {
    private final BitSet loggedValueIndexes;
    private final LogValuesDescriptor logValuesDescriptor;
    private final MethodParameter[] methodParameters;
    private final Class<?> enclosingClass;
    private final Optional<Object> instance;

    private ArgumentDescriptor(BitSet loggedValueIndexes, MethodParameter[] methodParameters, Class<?> enclosingClass, Object instance, LogValuesDescriptor logValuesDescriptor) {
        this.loggedValueIndexes = loggedValueIndexes;
        this.methodParameters = methodParameters;
        this.logValuesDescriptor = logValuesDescriptor;
        this.enclosingClass = enclosingClass;
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

    public Class<?> getEnclosingClass() {
        return enclosingClass;
    }

    public Optional<Object> getInstance() {
        return instance;
    }

    /**
     * Builder.
     */
    public static final class Builder {
        private static final ArgumentDescriptor NO_ARGUMENTS_DESCRIPTOR
            = new ArgumentDescriptor(new BitSet(0), null, null, null, new LogValuesDescriptor());
        private final Method method;
        private final int argumentCount;
        private final ParameterNameDiscoverer parameterNameDiscoverer;
        private final Object instance;
        private final Class<?> enclosingClass;

        Builder(Method method, int argumentCount, ParameterNameDiscoverer parameterNameDiscoverer, Class<?> enclosingClass, Object instance) {
            this.method = method;
            this.argumentCount = argumentCount;
            this.parameterNameDiscoverer = parameterNameDiscoverer;
            this.enclosingClass = enclosingClass;
            this.instance = instance;
        }

        /**
         *
         * @return constructor
         */
        public ArgumentDescriptor build() {
            MethodParameter[] methodParameters = getArrayOfMethodParameters();

            BitSet lpParameters = getMethodParameters(Lp.class);

            LogValuesDescriptor logValuesDescriptor = getParamsToLog();

            return new ArgumentDescriptor(lpParameters, methodParameters, enclosingClass, instance, logValuesDescriptor);
        }

        private MethodParameter[] getArrayOfMethodParameters() {
            Class<?>[] parameterTypes = method.getParameterTypes();
            String[] argNames = parameterNameDiscoverer.getParameterNames(method);

            MethodParameter[] methodParameters = parameterTypes == null || parameterTypes.length == 0 ? null : new MethodParameter[parameterTypes.length];
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
                if (annotation.annotationType().equals(LogIn.class)) {
                    LogIn logAnno = ((LogIn) annotation);
                    logThis = logAnno.logThis();
                }
                if (annotation.annotationType().equals(LogOut.class)) {
                    LogOut logAnno = ((LogOut) annotation);
                    logResult = logAnno.logResult();
                    logThis = logAnno.logThis();
                }
            }
            return new LogValuesDescriptor(logResult, logThis);
        }

    }

}
