package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Payload Set")
@Description("Sets the content of the current message payload to the given payload value. " +
                "The payload value could be a static text value or a dynamic expression. The mime type specifies " +
                "the type of the new payload value.")
@Component(service = PayloadSet.class, scope = PROTOTYPE)
public class PayloadSet implements ProcessorSync {

    @Property("Mime type")
    @MimeTypeCombo
    @Example("application/json")
    @InitValue(MimeType.MIME_TYPE_ANY)
    @DefaultValue(MimeType.MIME_TYPE_ANY)
    @Description("Sets the mime type of the new payload being set.")
    private String mimeType;

    @Property("Payload")
    @Hint("Payload text")
    @InitValue("#[]")
    @Example("<code>JSON.stringify({ data: message.payload(), id: Util.uuid() }</code>")
    @Description("The new payload to be set to the current flow message.")
    private DynamicObject payload;

    @Reference
    private ScriptEngineService scriptEngine;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        MimeType mimeType = MimeType.parse(this.mimeType, MimeType.ANY);

        Object result = scriptEngine.evaluate(payload, mimeType, flowContext, message).orElse(null);

        return MessageBuilder.get()
                .attributes(message.attributes())
                .withJavaObject(result, mimeType)
                .build();
    }

    public void setPayload(DynamicObject payload) {
        this.payload = payload;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
