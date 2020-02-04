package com.reedelk.core.component.join;

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

@ESBComponent("Join Delimiter")
@Component(service = JoinWithDelimiter.class, scope = PROTOTYPE)
public class JoinWithDelimiter implements Join {

    @Reference
    private ConverterService converterService;

    @Property("Mime type")
    @Default(MimeType.MIME_TYPE_TEXT_PLAIN)
    @MimeTypeCombo
    @PropertyInfo("Sets the mime type of the joined content in the message.")
    private String mimeType;

    @Property("Delimiter")
    @PropertyInfo("The delimiter char (or string) to be used to join the content of the messages.")
    @Default(",")
    private String delimiter;

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