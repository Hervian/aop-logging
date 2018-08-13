package com.github.hervian.logging;

import org.apache.commons.logging.Log;

import com.github.nickvl.xspring.core.log.aop.ArgumentDescriptor;
import com.github.nickvl.xspring.core.log.aop.LogAdapter;
import com.github.nickvl.xspring.core.log.aop.LogLevel;

/**
 * Logger class which given a log level delegates to the proper log method.
 */
public class Logger {

    private final LogAdapter logAdapter;

    /**
     *
     * @param logAdapter constructor
     */
    public Logger(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
    }

    /**
     *
     * @param logger asdf
     * @param logLevel asdf
     * @return asdf
     */
   public boolean isLogEnabled(Log logger, LogLevel logLevel) {
       switch (logLevel) {
       case DEBUG:
           return logger.isDebugEnabled();
       case ERROR:
           return logger.isErrorEnabled();
       case FATAL:
           return logger.isFatalEnabled();
       case INFO:
           return logger.isInfoEnabled();
       case TRACE:
           return logger.isTraceEnabled();
       case WARN:
           return logger.isWarnEnabled();
       default:
           throw new UnsupportedOperationException(
                   "A LogLevel has been added to the source code, but no mapping has been created in this switch statement.");
       }
   }

   /**
    *
    * @param logger asdf
    * @param method asdf
    * @param args asdf
    * @param argumentDescriptor asdf
    * @param logLevel asdf
    */
    public void logBefore(Log logger, String method, Object[] args, ArgumentDescriptor argumentDescriptor, LogLevel logLevel) {
        switch (logLevel) {
        case DEBUG:
            logger.debug(logAdapter.toMessage(method, args, argumentDescriptor));
            break;
        case ERROR:
            logger.error(logAdapter.toMessage(method, args, argumentDescriptor));
            break;
        case FATAL:
            logger.fatal(logAdapter.toMessage(method, args, argumentDescriptor));
            break;
        case INFO:
            logger.info(logAdapter.toMessage(method, args, argumentDescriptor));
            break;
        case TRACE:
            logger.trace(logAdapter.toMessage(method, args, argumentDescriptor));
            break;
        case WARN:
            logger.warn(logAdapter.toMessage(method, args, argumentDescriptor));
            break;
        default:
            throw new UnsupportedOperationException(
                    "A LogLevel has been added to the source code, but no mapping has been created in this switch statement.");
        }
    }


    /**
     *
     * @param logger asdf
     * @param method asdf
     * @param args asdf
     * @param result asdf
     * @param argumentDescriptor asdf
     * @param logLevel asdf
     */
   public void logAfter(Log logger, String method, Object[] args, Object result, ArgumentDescriptor argumentDescriptor, LogLevel logLevel) {
       switch (logLevel) {
       case DEBUG:
           logger.debug(logAdapter.toMessage(method, args, result, argumentDescriptor));
           break;
       case ERROR:
           logger.error(logAdapter.toMessage(method, args, result, argumentDescriptor));
           break;
       case FATAL:
           logger.fatal(logAdapter.toMessage(method, args, result, argumentDescriptor));
           break;
       case INFO:
           logger.info(logAdapter.toMessage(method, args, result, argumentDescriptor));
           break;
       case TRACE:
           logger.trace(logAdapter.toMessage(method, args, result, argumentDescriptor));
           break;
       case WARN:
           logger.warn(logAdapter.toMessage(method, args, result, argumentDescriptor));
           break;
       default:
           throw new UnsupportedOperationException(
                   "A LogLevel has been added to the source code, but no mapping has been created in this switch statement.");
       }
   }


   /**
    *
    * @param logger asdf
    * @param method asdf
    * @param argCount asdf
    * @param e asdf
    * @param stackTrace asdf
    * @param logLevel asdf
    */
   public void logException(Log logger, String method, int argCount, Exception e, boolean stackTrace, LogLevel logLevel) {
       switch (logLevel) {
       case DEBUG:
           if (stackTrace) {
               logger.debug(logAdapter.toMessage(method, argCount, e, stackTrace), e);
           } else {
               logger.debug(logAdapter.toMessage(method, argCount, e, stackTrace));
           }
           break;
       case ERROR:
           if (stackTrace) {
               logger.error(logAdapter.toMessage(method, argCount, e, stackTrace), e);
           } else {
               logger.error(logAdapter.toMessage(method, argCount, e, stackTrace));
           }
           break;
       case FATAL:
           if (stackTrace) {
               logger.fatal(logAdapter.toMessage(method, argCount, e, stackTrace), e);
           } else {
               logger.fatal(logAdapter.toMessage(method, argCount, e, stackTrace));
           }
           break;
       case INFO:
           if (stackTrace) {
               logger.info(logAdapter.toMessage(method, argCount, e, stackTrace), e);
           } else {
               logger.info(logAdapter.toMessage(method, argCount, e, stackTrace));
           }
           break;
       case TRACE:
           if (stackTrace) {
               logger.trace(logAdapter.toMessage(method, argCount, e, stackTrace), e);
           } else {
               logger.trace(logAdapter.toMessage(method, argCount, e, stackTrace));
           }
           break;
       case WARN:
           if (stackTrace) {
               logger.warn(logAdapter.toMessage(method, argCount, e, stackTrace), e);
           } else {
               logger.warn(logAdapter.toMessage(method, argCount, e, stackTrace));
           }
           break;
       default:
           throw new UnsupportedOperationException(
                   "A LogLevel has been added to the source code, but no mapping has been created in this switch statement.");
       }
   }

}
