package com.reedelk.core.component.payload;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static com.reedelk.runtime.api.commons.ConfigurationPreconditions.requireNotBlank;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Payload To String")
@Description("Transforms the message payload to string type. This component can be used when the payload " +
                "is a byte array or a byte array stream and we want to convert it to a string for further processing. " +
                "This might be necessary for instance when the result of a REST Call does not have a mime type assigned. " +
                "In this case the result will be a byte array and in order to further process the content with a script " +
                "we must convert it to a string type.")
@Component(service = PayloadToString.class, scope = PROTOTYPE)
public class PayloadToString implements ProcessorSync {

    @Property("Mime Type")
    @MimeTypeCombo
    @InitValue(MimeType.MIME_TYPE_TEXT_PLAIN)
    @Example(MimeType.MIME_TYPE_APPLICATION_JSON)
    @Description("Sets the new mime type of the payload content.")
    private String mimeType;

    @Reference
    private ConverterService converterService;

    private MimeType wantedMimeType;

    @Override
    public void initialize() {
        requireNotBlank(PayloadToString.class, mimeType, "MimeType must not be empty");
        this.wantedMimeType = MimeType.parse(mimeType);
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        TypedContent<?, ?> content = message.content();
        if (content.isStream()) {
            // Content is not consumed.
            TypedPublisher<?> stream = content.stream();
            TypedPublisher<String> output = converterService.convert(stream, String.class);
            return MessageBuilder.get()
                    .withTypedPublisher(output, wantedMimeType)
                    .attributes(message.attributes())
                    .build();
        } else {
            // Content is consumed.
            Object data = content.data();
            String converted = converterService.convert(data, String.class);
            return MessageBuilder.get()
                    .withString(converted, wantedMimeType)
                    .attributes(message.attributes())
                    .build();
        }
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
