/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop;

import com.github.hervian.logging.MethodNamePrefix;
import com.github.hervian.logging.ParameterStyle;

import lombok.Builder;

/**
 * Simple log adapter.
 */
public class SimpleLogAdapter extends AbstractLogAdapter {

    /**
     * Constructor.
     */
    public SimpleLogAdapter() {
        super(MethodNamePrefix.NONE, null, null, ParameterStyle.NAME_VALUE);
    }

    /**
     *
     * @param methodNamePrefix asdf
     * @param entryString asdf
     * @param exitString asdf
     * @param parameterStyle asdf
     */
    @Builder
    public SimpleLogAdapter(MethodNamePrefix methodNamePrefix, String entryString, String exitString, ParameterStyle parameterStyle) {
        super(MethodNamePrefix.NONE, entryString, exitString, ParameterStyle.NAME_VALUE);
    }

    @Override
    protected String asString(Object value) {
        return String.valueOf(value);
    }
}
