/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.service;

import com.github.nickvl.xspring.core.log.aop.annotation.LogWarn;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

/**
 * Bar service interface.
 */
public interface BarService {

    @LogWarn
    void inExtendedLogInSuperOnly(String iFirst, String iSecond);

    void inAbstract(@Lp String iFirst, @Lp String iSecond);

    void inExtended(@Lp String iFirst, @Lp String iSecond);

    void overridden(String iFirst, String iSecond);

    void overriddenLogInAbstractOnly(String iFirst, String iSecond);
}
