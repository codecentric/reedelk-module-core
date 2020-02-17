package com.reedelk.core.component.payload;

import com.reedelk.runtime.api.annotation.Default;
import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.annotation.MimeTypeCombo;
import com.reedelk.runtime.api.annotation.Property;
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

@ESBComponent("Payload Set")
@Component(service = PayloadSet.class, scope = PROTOTYPE)
public class PayloadSet implements ProcessorSync {

    @Reference
    private ScriptEngineService scriptEngine;

    @MimeTypeCombo
    @Property("Mime type")
    @Default(MimeType.MIME_TYPE_ANY)
    private String mimeType;

    @Property("Payload")
    @Default("#[]")
    private DynamicObject payload;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        MimeType mimeType = MimeType.parse(this.mimeType);

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
