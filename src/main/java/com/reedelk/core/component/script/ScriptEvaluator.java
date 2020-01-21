package com.reedelk.core.component.script;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.factory.TypedContentFactory;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("Script")
@Component(service = ScriptEvaluator.class, scope = PROTOTYPE)
public class ScriptEvaluator implements ProcessorSync {

    @Reference
    private ScriptEngineService service;

    @Property("Mime type")
    @Default(MimeType.MIME_TYPE_TEXT_PLAIN)
    @MimeTypeCombo
    @PropertyInfo("Sets the mime type of the script result in the message payload. " +
            "E.g: if the result of the script is JSON, then <i>application/json</i> should be selected." +
            "This is useful to let the following components know how to process the message payload set by this script. " +
            "The rest listener for instance uses this information to set the correct content type in the request's response body.")
    private String mimeType;

    @Property("Script")
    @PropertyInfo("Sets the script to be executed by this component.")
    private Script script;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        MimeType mimeType = MimeType.parse(this.mimeType);

        Object evaluated = service.evaluate(script, Object.class, flowContext, message).orElse(null);

        TypedContent<?> content = TypedContentFactory.from(evaluated, mimeType);

        return MessageBuilder.get().typedContent(content).build();
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
