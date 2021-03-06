package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.core.internal.commons.LoggerLevel;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Logger")
@ComponentOutput(
        attributes = ComponentOutput.PreviousComponent.class,
        payload = ComponentOutput.PreviousComponent.class)
@ComponentInput(
        payload = Object.class,
        description = "Any payload to be logged")
@Description("This component allows to log information within a flow such as message payload, attributes, " +
                "context variables and so on. A logger component can be added anywhere in a flow and it can log a " +
                "simple text value or a dynamic expression. The Log Message input field type can be toggled " +
                "to enter a static or dynamic expression value.")
@Component(service = LoggerComponent.class, scope = PROTOTYPE)
public class LoggerComponent implements ProcessorSync {

    public static final Logger logger = LoggerFactory.getLogger(LoggerComponent.class);

    @Property("Logger Level")
    @Example("DEBUG")
    @InitValue("INFO")
    @DefaultValue("INFO")
    @Description("The logger level used to log the given message. " +
            "Log levels can be configured from the <i>{RUNTIME_HOME}/config/logback.xml</i> file.")
    private LoggerLevel level;

    @Property("Log message")
    @Hint("my log message")
    @InitValue("#[message]")
    @DefaultValue("<code>message</code>")
    @Example("<code>'Attributes:' + message.attributes()</code>")
    @Description("Sets the message to be logged. It can be a static or dynamic value.")
    private DynamicObject message;

    @Reference
    private ScriptEngineService service;

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
        } catch (ScriptException exception) {
            throw new PlatformException(exception);
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
        level.log(logger, evaluationResult);
    }
}
