package com.reedelk.core.component;

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

@ModuleComponent("Join Script")
@Description("Can only be placed after a Fork. It joins the payloads of the messages resulting " +
                "from the execution of the Fork with the provided Javascript function. " +
                "The mime type property specifies the mime type of the joined payloads. " +
                "If the result of the script is null, an empty message payload content is set.")
@Component(service = JoinWithScript.class, scope = PROTOTYPE)
public class JoinWithScript implements Join {

    @Property("Mime type")
    @MimeTypeCombo
    @InitValue(MimeType.MIME_TYPE_TEXT_PLAIN)
    @Example(MimeType.MIME_TYPE_APPLICATION_JSON)
    @Description("Sets the mime type of the joined content in the message.")
    private String mimeType;

    @Property("Script")
    @Example("joiners/joinByType.js")
    @ScriptSignature(arguments = {"context","messages"})
    @AutocompleteVariable(name = "context", type = FlowContext.TYPE)
    @AutocompleteVariable(name = "messages", type = Message.TYPE_ARRAY)
    @Description("The path of the Javascript function to be invoked when executing the component")
    private Script script;

    @Reference
    private ScriptEngineService service;

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
