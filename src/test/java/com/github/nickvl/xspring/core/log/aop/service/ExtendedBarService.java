/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.service;

import com.github.nickvl.xspring.core.log.aop.annotation.LogDebug;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

/**
 * Implements {@link BarService}. Log level is lower than in {@link AbstractBarService}, {@link BarService}.
 */
public class ExtendedBarService extends AbstractBarService {

    @Override
    public void inExtendedLogInSuperOnly(String eFirst, String eSecond) {
        // Log annotations in interface
    }

    @LogDebug
    @Override
    public void inExtended(@Lp String eFirst, @Lp String eSecond) {
        // Log annotation here only
    }

    @LogDebug
    @Override
    public void overridden(String eFirst, @Lp String eSecond) {
        // Log annotation altered
    }

    @Override
    public void overriddenLogInAbstractOnly(String eFirst, String eSecond) {
        // Log annotation in parent and not altered here
    }
}
