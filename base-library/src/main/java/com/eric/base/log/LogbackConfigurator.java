package com.eric.base.log;

import android.content.Context;
import android.util.Log;

import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.File;
import java.io.InputStream;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackConfigurator {

    private static final String TAG = "LogbackConfigurator";
    private static final String LOGBACK_FILE_NAME = "logback.xml";

    /**
     * 配置 Logback 日志系统
     *
     * @param context Android 应用的 Context
     */
    public static void configure(Context context) {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

            // 重置当前的 LoggerContext
            loggerContext.reset();

            // 使用 JoranConfigurator 加载 logback.xml 配置文件
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);

            // 加载 assets/logback.xml 文件
            InputStream logbackConfig = context.getAssets().open(LOGBACK_FILE_NAME);
            configurator.doConfigure(logbackConfig);

            // 打印 Logback 的内部状态
            StatusPrinter.print(loggerContext);
        } catch (Exception e) {
            Log.e(TAG, "Logback configuration failed", e);
        }
    }
}
