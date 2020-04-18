package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Arrays;
import java.util.Collections;
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

    @Reference
    private ConverterService converterService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        TypedContent<?, ?> content = message.content();

        if (content == null) {
            // Empty.
            return MessageBuilder.get(SplitText.class)
                    .withList(Collections.emptyList(), String.class)
                    .build();
        }

        String payloadAsString = converterService.convert(content.data(), String.class);

        List<String> segments;
        if (StringUtils.isBlank(payloadAsString)) {
            segments = Collections.emptyList();
        } else {
            String[] split = payloadAsString.split(delimiter);
            segments = Arrays.asList(split);
        }

        return MessageBuilder.get(SplitText.class)
                .withList(segments, String.class)
                .build();
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
