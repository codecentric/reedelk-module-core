package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@AutocompleteType(global = true, description = "Functions to log from script functions")
public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    // Protected
    Log() {
    }

    @AutocompleteItem(replaceValue = "info('')", cursorOffset = 2, description = "Logs a message with INFO level")
    public void info(String message) {
        logger.info(message);
    }

    @AutocompleteItem(replaceValue = "warn('')", cursorOffset = 2, description = "Logs a message with WARN level")
    public void warn(String message) {
        logger.warn(message);
    }

    @AutocompleteItem(replaceValue = "error('')", cursorOffset = 2, description = "Logs a message with ERROR level")
    public void error(String message) {
        logger.error(message);
    }

    @AutocompleteItem(replaceValue = "trace('')", cursorOffset = 2, description = "Logs a message with TRACE level")
    public void trace(String message) {
        logger.trace(message);
    }

    @AutocompleteItem(replaceValue = "debug('')", cursorOffset = 2, description = "Logs a message with DEBUG level")
    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
