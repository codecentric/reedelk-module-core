package com.reedelk.core.component.logger;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent(
        name = "Logger",
        description = "This component allows to log information within a flow such as message payload, attributes, " +
                "context variables and so on. A logger component can be added anywhere in a flow and it can log a " +
                "simple text value or a dynamic Javascript expression. The Log Message input field type can be toggled " +
                "to enter a static or dynamic Javascript value.")
@Component(service = LoggerComponent.class, scope = PROTOTYPE)
public class LoggerComponent implements ProcessorSync {

    static final Logger logger = LoggerFactory.getLogger(LoggerComponent.class);

    @Reference
    private ScriptEngineService service;

    @Example("DEBUG")
    @InitValue("INFO")
    @DefaultValue("INFO")
    @Property("Logger Level")
    @PropertyDescription("The logger level used to log the given message. " +
            "Log levels can be configured from the <i>{RUNTIME_HOME}/config/logback.xml</i> file.")
    private LoggerLevel level;

    @Example("<code>'Attributes:' + message.attributes()</code>")
    @InitValue("#[message]")
    @DefaultValue("<code>message</code>")
    @Hint("my log message")
    @Property("Log message")
    @PropertyDescription("Sets the message to be logged. It can be a static or dynamic value.")
    private DynamicObject message;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        try {
            if (LoggerLevel.DEBUG.equals(level)) {
                // When level is DEBUG, we only debug if the debug is enabled.
                if (logger.isDebugEnabled()) {
                    debug(message, flowContext);
                }
            } else {
                debug(message, flowContext);
            }
        } catch (ScriptException e) {
            throw new ESBException(e);
        }
        return message;
    }

    public void setLevel(LoggerLevel level) {
        this.level = level;
    }

    public void setMessage(DynamicObject message) {
        this.message = message;
    }

    private void debug(Message message, FlowContext flowContext) throws ScriptException {
        // The logger should just print the Stream object if it is a stream, otherwise if
        // the stream was resolved (hence loaded into memory) it should print the value.
        Object evaluationResult = service.evaluate(this.message, flowContext, message).orElse(null);
        level.log(evaluationResult);
    }
}
