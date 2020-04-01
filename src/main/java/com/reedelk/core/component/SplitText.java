package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.StringContent;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Split Text")
@Description("The Split Text component splits the text from the " +
        "Message payload using the provided delimiter or regular expression. " +
        "A collection containing all the strings computed by splitting the message payload with the delimiter.")
@Component(service = SplitText.class, scope = PROTOTYPE)
public class SplitText implements ProcessorSync {

    @Property("Delimiter")
    @Hint(",")
    @Example(";")
    @InitValue(",")
    @Description("The delimiter to be used to split the message payload. A regular expression can be used.")
    private String delimiter;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        TypedContent<?, ?> content = message.content();

        if (content instanceof StringContent) {
            String payloadAsString = message.payload();

            List<String> segments;
            if (StringUtils.isNull(payloadAsString)) {
                segments = new ArrayList<>();
            } else {
                String[] split = payloadAsString.split(delimiter);
                segments = Arrays.asList(split);
            }

            return MessageBuilder.get()
                    .withJavaCollection(segments, String.class)
                    .build();
        }

        throw new ESBException(String.format("Message payload must be of type=['%s']", StringContent.class.getSimpleName()));
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
