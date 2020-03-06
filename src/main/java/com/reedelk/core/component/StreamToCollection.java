package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.osgi.service.component.annotations.Component;

import java.util.List;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static com.reedelk.runtime.api.commons.StreamUtils.FromObject;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Stream To Collection")
@Description("Converts a stream to a collection. " +
        "Each element of the stream is a single item in the collection. " +
        "This component consumes the stream by loading all the elements in memory.")
@Component(service = StreamToCollection.class, scope = PROTOTYPE)
public class StreamToCollection implements ProcessorSync {

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        TypedContent<Object, Object> content = message.content();
        checkArgument(content.isStream(), "Expected payload containing a stream but it was not.");
        checkArgument(!content.isConsumed(), "Expected payload containing a stream not consumed but it was not.");

        List<Object> streamAsList = FromObject.consume(content.stream());

        return MessageBuilder.get()
                .attributes(message.getAttributes())
                .withJavaCollection(streamAsList, Object.class)
                .build();
    }
}
