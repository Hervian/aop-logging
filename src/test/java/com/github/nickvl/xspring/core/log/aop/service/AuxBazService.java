/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.service;

import com.github.nickvl.xspring.core.log.aop.annotation.LogDebug;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

/**
 * Implements {@link BazService}.
 */
@LogDebug
public class AuxBazService extends AbstractBazService {

    @Override
    public void inImpl(@Lp String xFirst, @Lp String xSecond) {
        // nothing to do
    }
}
