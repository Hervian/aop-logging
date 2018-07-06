package com.github.hervian.logging;

import org.apache.commons.logging.Log;

import com.github.nickvl.xspring.core.log.aop.ArgumentDescriptor;
import com.github.nickvl.xspring.core.log.aop.LogAdapter;

/**
 * Provides warn strategy.
 */
public final class WarnLogStrategy extends LogStrategy {

    /**
     *
     * @param logAdapter asdf
     */
    public WarnLogStrategy(LogAdapter logAdapter) {
        super(logAdapter);
    }

    @Override
    public boolean isLogEnabled(Log logger) {
        return logger.isWarnEnabled();
    }

    @Override
    public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
        logger.warn(getLogAdapter().toMessage(method, args, argumentDescriptor));
    }

    @Override
    public void logAfter(Log logger, String method, int argCount, Object result, ArgumentDescriptor argumentDescriptor) {
        logger.warn(getLogAdapter().toMessage(method, argCount, result, argumentDescriptor));
    }

    @Override
    public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
        if (stackTrace) {
            logger.warn(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
        } else {
            logger.warn(getLogAdapter().toMessage(method, argCount, e, stackTrace));
        }
    }

}