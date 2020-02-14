package com.reedelk.core.component.resource;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.ResourceBinary;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.Publisher;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("Resource Read Binary")
@Component(service = ResourceReadBinary.class, scope = PROTOTYPE)
public class ResourceReadBinary extends ResourceReadComponent implements ProcessorSync {

    @Property("Resource file")
    @PropertyInfo("The path and name of the file to be read from the project's resources folder.")
    private ResourceBinary resourceFile;

    @Property("Auto mime type")
    @Default("true")
    @PropertyInfo("If true, the mime type of the payload is determined from the extension of the resource read.")
    private boolean autoMimeType;

    @Property("Mime type")
    @MimeTypeCombo
    @Default(MimeType.MIME_TYPE_TEXT_PLAIN)
    @When(propertyName = "autoMimeType", propertyValue = "false")
    @When(propertyName = "autoMimeType", propertyValue = When.BLANK)
    @PropertyInfo("The mime type of the resource read from local project's resources directory.")
    private String mimeType;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        Publisher<byte[]> data = resourceFile.data();

        String resourceFilePath = resourceFile.path();

        MessageAttributes attributes = createAttributes(ResourceReadText.class, resourceFilePath);

        // TODO: Need to convert the payload form the mime type!
        MimeType mimeType = mimeTypeFrom(autoMimeType, this.mimeType, resourceFilePath);

        return MessageBuilder.get()
                .attributes(attributes)
                .withBinary(data, mimeType)
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
