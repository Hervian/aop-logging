/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.service;

import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

/**
 * Implements {@link BazService}.
 */
@LogInfo
public class GeneralBazService extends AbstractBazService {

    @Override
    public void inImpl(@Lp String gFirst, @Lp String gSecond) {
        // nothing to do
    }
}
