package com.reedelk.core.component.join;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("Join Script")
@Component(service = JoinWithScript.class, scope = PROTOTYPE)
public class JoinWithScript implements Join {

    @Reference
    private ScriptEngineService service;

    @Property("Mime type")
    @Default(MimeType.MIME_TYPE_TEXT_PLAIN)
    @MimeTypeCombo
    @PropertyInfo("Sets the mime type of the joined content in the message.")
    private String mimeType;


    @Property("Script")
    @ScriptSignature(arguments = {"context","messages"})
    @AutoCompleteContributor(message = false, contributions = {
            "messages[VARIABLE:Message[]]",
            "messages.size()[FUNCTION:int]"})
    private Script script;

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
        Optional<Object> result = service.evaluate(script, Object.class, flowContext, messagesToJoin);
        if (result.isPresent()) {
            MimeType mimeType = MimeType.parse(this.mimeType);
            return MessageBuilder.get()
                    .withJavaObject(result.get(), mimeType)
                    .build();
        } else {
            return MessageBuilder.get()
                    .empty()
                    .build();
        }
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}