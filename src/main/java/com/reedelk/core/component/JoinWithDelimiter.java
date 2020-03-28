package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.stream.Collectors;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Join With Delimiter")
@Description("Can only be placed after a Fork. It joins the payloads of the messages resulting " +
                "from the execution of the Fork with the provided delimiter. " +
                "A delimiter can be a single character or any other string. " +
                "The mime type property specifies the mime type of the joined payloads. " +
                "This component automatically converts the payload of each single input message to string " +
                "in case they are not a string type already.")
@Component(service = JoinWithDelimiter.class, scope = PROTOTYPE)
public class JoinWithDelimiter implements Join {

    @Property("Mime type")
    @MimeTypeCombo
    @Example(MimeType.MIME_TYPE_APPLICATION_JSON)
    @InitValue(MimeType.MIME_TYPE_TEXT_PLAIN)
    @Description("Sets the mime type of the joined content in the message.")
    private String mimeType;

    @Property("Delimiter")
    @Example(";")
    @InitValue(",")
    @Description("The delimiter char (or string) to be used to join the content of the messages.")
    private String delimiter;

    @Reference
    private ConverterService converterService;

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {

        // Join with delimiter supports joins of only string data types.
        String combinedPayload = messagesToJoin.stream()
                .map(message -> {
                    Object messageData = message.content().data();
                    return converterService.convert(messageData, String.class);
                })
                .collect(Collectors.joining(delimiter));

        MimeType mimeType = MimeType.parse(this.mimeType);

        return MessageBuilder.get()
                .withString(combinedPayload, mimeType)
                .build();
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
