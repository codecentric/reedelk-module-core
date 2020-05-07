package com.reedelk.core.component;

import com.reedelk.core.internal.attribute.ResourceReadAttributes;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.MimeTypeUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.ResourceBinary;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Resource Read Binary")
@ComponentInput(payload = {Void.class}, description = "The resource content") // TODO: Any input is accepted, create a class for that.
@ComponentOutput(attributes = ResourceReadAttributes.class, payload = byte[].class, description = "The content of the resource file read from the project's resources folder.")
@Description("Reads a file from the project's resources folder and sets its content into the flow message. " +
                "The type of the message payload is byte array. This component might be used to load binary " +
                "data (e.g a picture file) from the project's resources folder. The Mime Type property assign the " +
                "mime type of the file to the message payload. If Auto Mime Type is selected, the mime type is " +
                "automatically determined from the file extension.")
@Component(service = ResourceReadBinary.class, scope = PROTOTYPE)
public class ResourceReadBinary implements ProcessorSync {

    @Property("Resource file")
    @Example("assets/my_image.jpg")
    @HintBrowseFile("Select Resource Binary File ...")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceBinary resourceFile;

    @Property("Auto mime type")
    @Example("true")
    @InitValue("true")
    @DefaultValue("false")
    @Description("If true, the mime type of the payload is determined from the extension of the resource read.")
    private boolean autoMimeType;

    @Property("Mime type")
    @MimeTypeCombo
    @Example(MimeType.AsString.IMAGE_JPEG)
    @DefaultValue(MimeType.AsString.APPLICATION_BINARY)
    @When(propertyName = "autoMimeType", propertyValue = "false")
    @When(propertyName = "autoMimeType", propertyValue = When.BLANK)
    @Description("The mime type of the resource read from local project's resources directory.")
    private String mimeType;

    @Reference
    ConverterService converterService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        Publisher<byte[]> data = resourceFile.data();

        String resourceFilePath = resourceFile.path();

        MessageAttributes attributes = new ResourceReadAttributes(resourceFilePath);

        MimeType mimeType = MimeTypeUtils.fromFileExtensionOrParse(autoMimeType, resourceFilePath, this.mimeType, MimeType.APPLICATION_BINARY);

        return MessageBuilder.get(ResourceReadBinary.class)
                .withBinary(data, mimeType)
                .attributes(attributes)
                .build();
    }

    public void setResourceFile(ResourceBinary resourceFile) {
        this.resourceFile = resourceFile;
    }

    public void setAutoMimeType(boolean autoMimeType) {
        this.autoMimeType = autoMimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
