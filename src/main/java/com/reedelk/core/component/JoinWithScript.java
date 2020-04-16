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

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Join With Script")
@Description("Can only be placed after a Fork. It joins the payloads of the messages resulting " +
        "from the execution of the Fork with the provided Javascript function. " +
        "The mime type property specifies the mime type of the joined payloads. " +
        "If the result of the script is null, an empty message payload content is set.")
@Component(service = JoinWithScript.class, scope = PROTOTYPE)
public class JoinWithScript implements Join {

    @Property("Mime type")
    @MimeTypeCombo
    @DefaultValue(MimeType.AsString.TEXT_PLAIN)
    @Example(MimeType.AsString.APPLICATION_JSON)
    @Description("Sets the mime type of the joined content in the message.")
    private String mimeType;

    @Property("Script")
    @Example("joiners/joinByType.js")
    @ScriptSignature(arguments = {"context", "messages"})
    @AutocompleteVariable(name = "context", type = FlowContext.class)
    @AutocompleteVariable(name = "messages", type = Message[].class)
    @Description("The path of the Javascript function to be invoked when executing the component")
    private Script script;

    @Reference
    private ScriptEngineService service;

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
        return service
                .evaluate(script, Object.class, flowContext, messagesToJoin)
                .map(result -> {
                    MimeType parsedMimeType = MimeType.parse(mimeType, MimeType.TEXT_PLAIN);
                    return MessageBuilder.get(JoinWithScript.class)
                            .withJavaObject(result, parsedMimeType)
                            .build();

                }).orElseGet(() -> MessageBuilder.get(JoinWithScript.class).empty().build());
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

}
