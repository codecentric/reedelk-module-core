package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.core.internal.type.ListOfMessages;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.commons.AttributesUtils;
import de.codecentric.reedelk.runtime.api.component.Join;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.script.Script;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Join With Script")
@ComponentInput(
        payload = Message[].class,
        description = "The messages to join using the given script")
@ComponentOutput(
        attributes = ComponentOutput.PreviousComponent.class,
        payload = Object.class,
        description = "The joined content of the input messages payloads using by evaluating the given script.")
@Description("Can only be placed after a Fork. It joins the payloads of the messages resulting " +
        "from the execution of the Fork with the provided script function. " +
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
    @Example("joiners/joinByType.groovy")
    @ScriptSignature(arguments = {"context", "messages"}, types = {FlowContext.class, ListOfMessages.class})
    @Description("The path of the script function to be invoked when executing the component")
    private Script script;

    @Reference
    private ScriptEngineService service;

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
        MessageAttributes mergedAttributes = AttributesUtils.merge(messagesToJoin);

        return service
                .evaluate(script, Object.class, flowContext, messagesToJoin)
                .map(result -> {
                    MimeType parsedMimeType = MimeType.parse(mimeType, MimeType.TEXT_PLAIN);
                    return MessageBuilder.get(JoinWithScript.class)
                            .withJavaObject(result, parsedMimeType)
                            .attributes(mergedAttributes)
                            .build();

                }).orElseGet(() ->
                        MessageBuilder.get(JoinWithScript.class)
                                .attributes(mergedAttributes)
                                .empty()
                                .build());
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
