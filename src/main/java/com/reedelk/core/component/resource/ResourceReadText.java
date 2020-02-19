package com.reedelk.core.component.resource;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.Publisher;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent(
        name = "Resource Read Text",
        description = "Reads a file from the project's resources folder and sets its content into the flow message. " +
                "The type of the message payload is string. This component might be used to load text files (e.g .txt, .json, .xml) " +
                "from the project's resources folder. The Mime Type property assign the mime type of the file to the " +
                "message payload. If Auto Mime Type is selected, the mime type is automatically determined from " +
                "the file extension.")
@Component(service = ResourceReadText.class, scope = PROTOTYPE)
public class ResourceReadText extends ResourceReadComponent implements ProcessorSync {

    @Hint("assets/sample.txt")
    @Example("assets/data_model.json")
    @Property("Resource file")
    @PropertyDescription("The path and name of the file to be read from the project's resources folder.")
    private ResourceText resourceFile;

    @InitValue("true")
    @Example("true")
    @DefaultValue("false")
    @Property("Auto mime type")
    @PropertyDescription("If true, the mime type of the payload is determined from the extension of the resource read.")
    private boolean autoMimeType;

    @MimeTypeCombo
    @Example(MimeType.MIME_TYPE_APPLICATION_JSON)
    @DefaultValue(MimeType.MIME_TYPE_TEXT_PLAIN)
    @InitValue(MimeType.MIME_TYPE_TEXT_PLAIN)
    @When(propertyName = "autoMimeType", propertyValue = "false")
    @When(propertyName = "autoMimeType", propertyValue = When.BLANK)
    @Property("Mime type")
    @PropertyDescription("The mime type of the resource read from local project's resources directory.")
    private String mimeType;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        Publisher<String> data = resourceFile.data();

        String resourceFilePath = resourceFile.path();

        MimeType mimeType = mimeTypeFrom(autoMimeType, this.mimeType, resourceFilePath, MimeType.TEXT);

        MessageAttributes attributes = createAttributes(ResourceReadText.class, resourceFilePath);

        return MessageBuilder.get()
                .attributes(attributes)
                .withString(data, mimeType)
                .build();
    }

    public void setResourceFile(ResourceText resourceFile) {
        this.resourceFile = resourceFile;
    }

    public void setAutoMimeType(boolean autoMimeType) {
        this.autoMimeType = autoMimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
