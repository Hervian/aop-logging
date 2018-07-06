/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.hervian.logging.MethodNamePrefix;
import com.github.hervian.logging.ParameterStyle;

import lombok.Builder;

/**
 * Universal log adapter, capable to out parameter values by reflection.
 */
public class UniversalLogAdapter extends AbstractLogAdapter {
    private Set<String> excludeFieldNames = null;
    private final int cropThreshold;
    private final boolean skipNullFields;

  private static final String CALLING = "calling:";
  private static final String RETURNING = "returning:";



    /**
     * Constructor.
     *
     * @param skipNullFields use {@code true} to exclude fields which value is {@code null} from building the string
     * @param cropThreshold threshold value of processed elements count to stop building the string, applied only for multi-element structures
     * @param excludeFieldNames field names to exclude from building the string
     * @throws IllegalArgumentException if <code>cropThreshold </code> is negative
     */
    public UniversalLogAdapter(boolean skipNullFields, int cropThreshold, Set<String> excludeFieldNames) {
        this(skipNullFields, cropThreshold, excludeFieldNames, null, null, null, null);
    }

    /**
     *
     * @param skipNullFields asdf
     * @param cropThreshold asdf
     * @param excludeFieldNames asdf
     * @param methodNamePrefix asdf
     * @param entryString asdf
     * @param exitString asdf
     * @param parameterStyle asdf
     */
    @Builder
    public UniversalLogAdapter(boolean skipNullFields, int cropThreshold, Set<String> excludeFieldNames,
            MethodNamePrefix methodNamePrefix, String entryString, String exitString,
            ParameterStyle parameterStyle) {
        super(methodNamePrefix == null ? MethodNamePrefix.SIMPLE_CLASS_NAME : methodNamePrefix, entryString,
                exitString, parameterStyle);
        this.skipNullFields = skipNullFields;
        this.cropThreshold = cropThreshold;
        this.excludeFieldNames = excludeFieldNames == null ? null : new HashSet<String>(excludeFieldNames);
    }

    /**
     * Constructor.
     *
     * @param skipNullFields use {@code true} to exclude fields which value is {@code null} from building the string
     * @param excludeFieldNames field names to exclude from building the string
     */
    public UniversalLogAdapter(boolean skipNullFields, Set<String> excludeFieldNames) {
        this(skipNullFields, -1, excludeFieldNames);
    }

    @Override
    protected String asString(Object value) {
        if (value == null) {
            return ToString.getNull();
        }
        Class<?> clazz = value.getClass();
        if (!(value instanceof Collection<?> || value instanceof Map<?, ?>) && ToStringDetector.INSTANCE.hasToString(clazz)) {
            return value.toString();
        }
        ToString builder = cropThreshold == -1 ? ToString.createDefault() : ToString.createCropInstance(cropThreshold);
        builder.addStart(value);

        if (value instanceof Collection<?>) {
            builder.addCollection((Collection<?>) value);
        } else if (value instanceof Map<?, ?>) {
            builder.addMap((Map<?, ?>) value);
        } else if (clazz.isArray()) {
            builder.addArray(value);
        } else {
            while (clazz != Object.class) {
                appendFieldsIn(builder, value, clazz.getDeclaredFields());
                clazz = clazz.getSuperclass();
            }
        }
        builder.addEnd(value);
        return builder.toString();
    }

    private boolean reject(Field field) {
        return field.getName().indexOf('$') != -1 // Reject field from inner class.
                || Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())
                || excludeFieldNames != null && excludeFieldNames.contains(field.getName());
    }

    private void appendFieldsIn(ToString builder, Object object, Field[] fields) {
        if (fields.length == 0) {
            return;
        }
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            if (!reject(field)) {
                String fieldName = field.getName();
                Object fieldValue;
                try {
                    // memo: it creates wrapper objects for primitive types.
                    fieldValue = field.get(object);
                } catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Unexpected IllegalAccessException: " + ex.getMessage());
                }
                if (!skipNullFields || fieldValue != null) {
                    builder.addField(fieldName, fieldValue);
                }
            }
        }
    }

}
