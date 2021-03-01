package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Payload Set")
@ComponentOutput(
        attributes = ComponentOutput.PreviousComponent.class,
        payload = ComponentOutput.InferFromDynamicProperty.class,
        dynamicPropertyName = "payload",
        description = "Sets the new message payload by evaluating the expression. The attributes are not changed.")
@ComponentInput(
        payload = Object.class,
        description = "Any payload input to be evaluated")
@Description("Sets the content of the current message payload to the given payload value. " +
                "The payload value could be a static text value or a dynamic expression. The mime type specifies " +
                "the type of the new payload value.")
@Component(service = PayloadSet.class, scope = PROTOTYPE)
public class PayloadSet implements ProcessorSync {

    @Property("Mime type")
    @MimeTypeCombo
    @Example("application/json")
    @DefaultValue(MimeType.AsString.ANY)
    @Description("Sets the mime type of the new payload being set.")
    private String mimeType;

    @Property("Payload")
    @Hint("Payload text")
    @InitValue("#[message.payload()]")
    @Example("<code>Json.stringify([ data: message.attributes().pathParams.name, id: Util.uuid() ])</code>")
    @Description("The new payload to be set to the current flow message.")
    private DynamicObject payload;

    @Reference
    private ScriptEngineService scriptEngine;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        MimeType parsedMimeType = MimeType.parse(mimeType, MimeType.ANY);

        Object result = scriptEngine.evaluate(payload, parsedMimeType, flowContext, message).orElse(null);

        return MessageBuilder.get(PayloadSet.class)
                .withJavaObject(result, parsedMimeType)
                .attributes(message.attributes())
                .build();
    }

    public void setPayload(DynamicObject payload) {
        this.payload = payload;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
