package com.github.hervian.logging;

import org.apache.commons.logging.Log;

import com.github.nickvl.xspring.core.log.aop.ArgumentDescriptor;
import com.github.nickvl.xspring.core.log.aop.LogAdapter;

/**
 * Provides fatal strategy.
 */
public final class FatalLogStrategy extends LogStrategy {

    /**
     *
     * @param logAdapter asdf
     */
    public FatalLogStrategy(LogAdapter logAdapter) {
        super(logAdapter);
    }

    @Override
    public boolean isLogEnabled(Log logger) {
        return logger.isFatalEnabled();
    }

    @Override
    public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor) {
        logger.fatal(getLogAdapter().toMessage(method, args, argumentDescriptor));
    }

    @Override
    public void logAfter(Log logger, String method, int argCount, Object result, ArgumentDescriptor argumentDescriptor) {
        logger.fatal(getLogAdapter().toMessage(method, argCount, result, argumentDescriptor));
    }

    @Override
    public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace) {
        if (stackTrace) {
            logger.fatal(getLogAdapter().toMessage(method, argCount, e, stackTrace), e);
        } else {
            logger.fatal(getLogAdapter().toMessage(method, argCount, e, stackTrace));
        }
    }
}