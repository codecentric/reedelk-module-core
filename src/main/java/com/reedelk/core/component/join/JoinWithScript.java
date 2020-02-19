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

@ModuleComponent(
        name = "Join Script",
        description = "Can only be placed after a Fork. It joins the payloads of the messages resulting " +
                "from the execution of the Fork with the provided Javascript function. " +
                "The mime type property specifies the mime type of the joined payloads. " +
                "If the result of the script is null, an empty message payload content is set.")
@Component(service = JoinWithScript.class, scope = PROTOTYPE)
public class JoinWithScript implements Join {

    @Reference
    private ScriptEngineService service;

    @MimeTypeCombo
    @Example(MimeType.MIME_TYPE_APPLICATION_JSON)
    @InitValue(MimeType.MIME_TYPE_TEXT_PLAIN)
    @Property("Mime type")
    @PropertyDescription("Sets the mime type of the joined content in the message.")
    private String mimeType;

    @ScriptSignature(arguments = {"context","messages"})
    @AutoCompleteContributor(message = false, contributions = {
            "messages[VARIABLE:Message[]]",
            "messages.size()[FUNCTION:int]"})
    @Example("joiners/joinByType.js")
    @Property("Script")
    @PropertyDescription("The path of the Javascript function to be invoked when executing the component")
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
