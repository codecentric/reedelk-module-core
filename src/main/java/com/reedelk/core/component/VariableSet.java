package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static com.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotBlank;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Variable Set")
@ComponentOutput(
        attributes = ComponentOutput.PreviousComponent.class,
        payload = ComponentOutput.PreviousComponent.class)
@ComponentInput(
        payload = Object.class,
        description = "Any input. The input message is used to evaluate the dynamic expression whose result is assigned " +
                "to the context variable with the given name.")
@Description("Sets a variable in the flow context with the provided value.")
@Component(service = VariableSet.class, scope = PROTOTYPE)
public class VariableSet implements ProcessorSync {

    @Property("Name")
    @Hint("mySampleVariable")
    @Example("mySampleVariable")
    @Description("The name of the variable to be set in the flow context.")
    private String name;

    @Property("Mime type")
    @MimeTypeCombo
    @DefaultValue(MimeType.AsString.ANY)
    @Example(MimeType.AsString.APPLICATION_JSON)
    @Description("The mime type of the value this context variable will be bound to.")
    private String mimeType;

    @Property("Value")
    @Hint("my variable content")
    @InitValue("#[message.payload()]")
    @Example("<code>message.attributes().pathParams</code>")
    @Description("The value to assign to the context variable being set. It might be a static or dynamic value.")
    private DynamicObject value;

    @Reference
    private ScriptEngineService scriptEngine;

    @Override
    public void initialize() {
        requireNotBlank(VariableSet.class, name, "Variable name to set must not be empty");
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        MimeType mimeType = MimeType.parse(this.mimeType, MimeType.ANY);

        Object result = scriptEngine.evaluate(value, mimeType, flowContext, message).orElse(null);

        flowContext.put(name, result);

        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(DynamicObject value) {
        this.value = value;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
