package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.core.internal.attribute.ResourceReadAttributes;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.commons.MimeTypeUtils;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.converter.ConverterService;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.resource.DynamicResource;
import de.codecentric.reedelk.runtime.api.resource.ResourceFile;
import de.codecentric.reedelk.runtime.api.resource.ResourceNotFound;
import de.codecentric.reedelk.runtime.api.resource.ResourceService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;

import java.util.Optional;

import static de.codecentric.reedelk.core.component.ResourceReadDynamicConfiguration.DEFAULT_READ_BUFFER_SIZE;
import static de.codecentric.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotNullOrBlank;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Resource Read Dynamic")
@ComponentInput(
        payload = Object.class,
        description = "The input payload is not used by this component. " +
                "The resource is read from the given Resource File path property.")
@ComponentOutput(
        attributes = ResourceReadAttributes.class,
        payload = { byte[].class, String.class },
        description = "The content of the resource file read from the project's resources folder. " +
                "The output is string if the mime type is a string based type e.g text/plain, otherwise a byte array.")
@Description("Reads a file from the project's resources folder and sets its content into the flow message. " +
        "The type of the message payload is byte array. The Mime Type property assign the mime type of the " +
        "file to the message payload. If Auto Mime Type is selected, the mime type is automatically determined " +
        "from the file extension. This component allows to specify the path and file name of the resource with " +
        "a dynamic value instead of a static one. This component might be used to load binary data " +
        "(e.g a picture file) from the project's resources folder in a dynamic fashion: for instance loading " +
        "files from a given REST Listener request path's value.")
@Component(service = ResourceReadDynamic.class, scope = PROTOTYPE)
public class ResourceReadDynamic implements ProcessorSync {

    @Mandatory
    @Property("Resource file")
    @Hint("/assets/sample.jpg")
    @InitValue("#['/assets/sample.jpg']")
    @Example("<code>message.attributes().get('pathParams').filePathParam</code>")
    @Description("The path and name of the file to be read from the project's resources folder. " +
            "The value which might be static or a dynamic expression must point to a file existing in the project's " +
            "resources directory")
    private DynamicResource resourceFile;

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

    @Group("Configuration")
    @Property("Configuration")
    private ResourceReadDynamicConfiguration configuration;

    @Reference
    ResourceService resourceService;
    @Reference
    ConverterService converterService;

    private int readBufferSize;

    @Override
    public void initialize() {
        requireNotNullOrBlank(ResourceReadDynamic.class, resourceFile, "resource file expression must not be null or empty");
        readBufferSize =
                Optional.ofNullable(configuration)
                        .flatMap(resourceReadDynamicConfiguration ->
                                Optional.ofNullable(resourceReadDynamicConfiguration.getReadBufferSize()))
                        .orElse(DEFAULT_READ_BUFFER_SIZE);
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        try {

            ResourceFile<byte[]> resourceFile = resourceService.find(this.resourceFile, readBufferSize, flowContext, message);

            String resourceFilePath = resourceFile.path();

            Publisher<byte[]> dataStream = resourceFile.data();

            MessageAttributes attributes = new ResourceReadAttributes(resourceFilePath);

            MimeType actualMimeType = MimeTypeUtils.fromFileExtensionOrParse(autoMimeType, resourceFilePath, mimeType, MimeType.APPLICATION_BINARY);

            // Convert the payload to a suitable type according to the mime type.
            if (String.class == actualMimeType.javaType()) {
                TypedPublisher<String> streamAsString =
                        converterService.convert(TypedPublisher.fromByteArray(dataStream), String.class);

                return MessageBuilder.get(ResourceReadDynamic.class)
                        .withString(streamAsString, actualMimeType)
                        .attributes(attributes)
                        .build();
            } else {
                return MessageBuilder.get(ResourceReadDynamic.class)
                        .withBinary(dataStream, actualMimeType)
                        .attributes(attributes)
                        .build();
            }

        } catch (ResourceNotFound resourceNotFound) {
            throw new PlatformException(resourceNotFound);
        }
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
