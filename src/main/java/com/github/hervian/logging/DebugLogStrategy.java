package com.github.hervian.logging;

import org.apache.commons.logging.Log;

import com.github.nickvl.xspring.core.log.aop.ArgumentDescriptor;
import com.github.nickvl.xspring.core.log.aop.LogAdapter;

/**
 * Provides debug strategy.
 */
public final class DebugLogStrategy extends LogStrategy {

    /**
     *
     * @param logAdapter asdf
     */
    public DebugLogStrategy(LogAdapter logAdapter) {
        super(logAdapter);
    }

    @Override
    public boolean isLogEnabled(Log logger) {
        return logger.isDebugEnabled();
    }

    @Override
    public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
        logger.debug(getLogAdapter().toMessage(method, args, argumentDescriptor));
    }

    @Override
    public void logAfter(Log logger, String method, int argCount, Object result, ArgumentDescriptor argumentDescriptor) {
        logger.debug(getLogAdapter().toMessage(method, argCount, result, argumentDescriptor));
    }

    @Override
    public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
        if (stackTrace) {
            logger.debug(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
        } else {
            logger.debug(getLogAdapter().toMessage(method, argCount, e, stackTrace));
        }
    }

}