package com.eric.base.log;

import android.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timber.log.Timber;

public class LogbackTree extends Timber.Tree {

    private final Logger logger;

    public LogbackTree(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        switch (priority) {
            case Log.VERBOSE:
                logger.trace(message, t);
                break;
            case Log.DEBUG:
                logger.debug(message, t);
                break;
            case Log.INFO:
                logger.info(message, t);
                break;
            case Log.WARN:
                logger.warn(message, t);
                break;
            case Log.ERROR:
                logger.error(message, t);
                break;
            default:
                logger.debug(message, t);
                break;
        }
    }
}
