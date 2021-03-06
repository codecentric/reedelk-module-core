package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.converter.ConverterService;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;
import de.codecentric.reedelk.runtime.api.type.ListOfString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static de.codecentric.reedelk.runtime.api.commons.StringUtils.isBlank;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Split Text")
@ComponentOutput(
        attributes = ComponentOutput.PreviousComponent.class,
        payload = ListOfString.class,
        description = "A list containing the segments of the input payload split using the delimiter.")
@ComponentInput(
        payload = String.class,
        description = "Any payload input string to be split using the delimiter. " +
                "If the input is not a string, it is first converted to string using the default system charset.")
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

        // Empty List.
        if (content == null) {
            return MessageBuilder.get(SplitText.class)
                    .withList(Collections.emptyList(), String.class)
                    .attributes(message.attributes())
                    .build();
        }

        String payloadAsString = converterService.convert(content.data(), String.class);

        List<String> segments;
        if (isBlank(payloadAsString)) {
            segments = new ListOfString();
        } else {
            String[] split = payloadAsString.split(delimiter);
            segments = Arrays.asList(split);
        }

        return MessageBuilder.get(SplitText.class)
                .withList(segments, String.class)
                .attributes(message.attributes())
                .build();
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
