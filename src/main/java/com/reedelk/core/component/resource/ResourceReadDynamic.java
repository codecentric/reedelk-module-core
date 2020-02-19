package com.reedelk.core.component.resource;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.JavaType;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.resource.ResourceService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;

import java.util.Optional;

import static com.reedelk.core.component.resource.ResourceReadDynamicConfiguration.DEFAULT_READ_BUFFER_SIZE;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent(
        name = "Resource Read Dynamic",
        description = "Reads a file from the project's resources folder and sets its content into the flow message. " +
                "The type of the message payload is byte array. The Mime Type property assign the mime type of the " +
                "file to the message payload. If Auto Mime Type is selected, the mime type is automatically determined " +
                "from the file extension. This component allows to specify the path and file name of the resource with " +
                "a dynamic value instead of a static one. This component might be used to load binary data " +
                "(e.g a picture file) from the project's resources folder in a dynamic fashion: for instance loading " +
                "files from a given REST Listener request path's value.")
@Component(service = ResourceReadDynamic.class, scope = PROTOTYPE)
public class ResourceReadDynamic extends ResourceReadComponent implements ProcessorSync {

    @Reference
    ResourceService resourceService;
    @Reference
    ConverterService converterService;

    @InitValue("#['/assets/sample.jpg']")
    @Example("<code>message.attributes().get('pathParams').filePathParam</code>")
    @Property("Resource file")
    @PropertyDescription("The path and name of the file to be read from the project's resources folder. " +
            "The value which might be static or a dynamic expression must point to a file existing in the project's " +
            "resources directory")
    private DynamicResource resourceFile;

    @Example("true")
    @InitValue("true")
    @DefaultRenameMe("false")
    @Property("Auto mime type")
    @PropertyDescription("If true, the mime type of the payload is determined from the extension of the resource read.")
    private boolean autoMimeType;

    @MimeTypeCombo
    @Example(MimeType.MIME_TYPE_IMAGE_JPEG)
    @InitValue(MimeType.MIME_TYPE_APPLICATION_BINARY)
    @DefaultRenameMe(MimeType.MIME_TYPE_APPLICATION_BINARY)
    @When(propertyName = "autoMimeType", propertyValue = "false")
    @When(propertyName = "autoMimeType", propertyValue = When.BLANK)
    @Property("Mime type")
    @PropertyDescription("The mime type of the resource read from local project's resources directory.")
    private String mimeType;

    @Property("Configuration")
    private ResourceReadDynamicConfiguration configuration;

    private int readBufferSize;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        try {

            ResourceFile<byte[]> resourceFile = resourceService.find(this.resourceFile, readBufferSize, flowContext, message);

            String resourceFilePath = resourceFile.path();

            Publisher<byte[]> dataStream = resourceFile.data();

            MessageAttributes attributes = createAttributes(ResourceReadDynamic.class, resourceFilePath);

            MimeType actualMimeType = mimeTypeFrom(autoMimeType, mimeType, resourceFilePath, MimeType.APPLICATION_BINARY);

            // Convert the payload to a suitable type according to the mime type.
            if (String.class == JavaType.from(actualMimeType)) {
                TypedPublisher<String> streamAsString =
                        converterService.convert(TypedPublisher.fromByteArray(dataStream), String.class);

                return MessageBuilder.get()
                        .withString(streamAsString, actualMimeType)
                        .attributes(attributes)
                        .build();
            } else {
                return MessageBuilder.get()
                        .withBinary(dataStream, actualMimeType)
                        .attributes(attributes)
                        .build();
            }

        } catch (ResourceNotFound resourceNotFound) {
            throw new ESBException(resourceNotFound);
        }
    }

    @Override
    public void initialize() {
        readBufferSize =
                Optional.ofNullable(configuration)
                .flatMap(resourceReadDynamicConfiguration ->
                        Optional.ofNullable(resourceReadDynamicConfiguration.getReadBufferSize()))
                        .orElse(DEFAULT_READ_BUFFER_SIZE);
    }

    public void setResourceFile(DynamicResource resourceFile) {
        this.resourceFile = resourceFile;
    }

    public void setAutoMimeType(boolean autoMimeType) {
        this.autoMimeType = autoMimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setConfiguration(ResourceReadDynamicConfiguration configuration) {
        this.configuration = configuration;
    }
}
