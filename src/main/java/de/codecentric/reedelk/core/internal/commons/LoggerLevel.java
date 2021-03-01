package de.codecentric.reedelk.core.internal.commons;

import de.codecentric.reedelk.runtime.api.annotation.DisplayName;
import org.slf4j.Logger;

public enum LoggerLevel implements MessageLogger {

    @DisplayName("Info")
    INFO {
        @Override
        public void log(Logger logger, Object message) {
            logger.info(asLoggableString(message));
        }
    },

    @DisplayName("Debug")
    DEBUG {
        @Override
        public void log(Logger logger, Object message) {
            logger.debug(asLoggableString(message));
        }
    },

    @DisplayName("Warn")
    WARN {
        @Override
        public void log(Logger logger, Object message) {
            logger.warn(asLoggableString(message));
        }
    },

    @DisplayName("Error")
    ERROR {
        @Override
        public void log(Logger logger, Object message) {
            logger.error(asLoggableString(message));
        }
    },

    @DisplayName("Trace")
    TRACE {
        @Override
        public void log(Logger logger, Object message) {
            logger.trace(asLoggableString(message));
        }
    };

    private static String asLoggableString(Object message) {
        return message == null ? null : message.toString();
    }
}
