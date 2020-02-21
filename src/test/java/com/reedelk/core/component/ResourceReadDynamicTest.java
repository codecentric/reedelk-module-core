package com.reedelk.core.component;

import com.reedelk.core.component.ResourceReadDynamic;
import com.reedelk.core.component.ResourceReadDynamicConfiguration;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceReadDynamicTest {

    private ModuleContext moduleContext = new ModuleContext(10L);

    @Mock
    private FlowContext context;
    @Mock
    private Message message;
    @Mock
    private ResourceService resourceService;
    @Mock
    private ConverterService converterService;
    @Mock
    private DynamicResource dynamicResource;

    private ResourceReadDynamic component;

    @BeforeEach
    void setUp() {
        component = new ResourceReadDynamic();
        component.resourceService = resourceService;
        component.converterService = converterService;
    }

    @Test
    void shouldNotConvertContentWhenBinaryMimeType() {
        // Given
        component.setAutoMimeType(true);
        component.setMimeType(MimeType.MIME_TYPE_APPLICATION_BINARY);
        component.setResourceFile(dynamicResource);
        component.initialize();

        byte[] payloadAsBytes = "my image".getBytes();
        doReturn(new TestResourceFile("/assets/image.jpg", payloadAsBytes))
                .when(resourceService)
                .find(dynamicResource, ResourceReadDynamicConfiguration.DEFAULT_READ_BUFFER_SIZE, context, message);
        // When
        Message result = component.apply(context, message);

        // Then
        byte[] payload = result.payload();
        assertThat(payload).isEqualTo(payloadAsBytes);
        assertThat(result.attributes()).containsEntry("componentName", "ResourceReadDynamic");
        assertThat(result.attributes()).containsEntry("resourcePath", "/assets/image.jpg");
        assertThat(result.attributes()).containsKey("timestamp");
    }

    @Test
    void shouldConvertContentWhenTextPlainMimeType() {
        // Given
        component.setAutoMimeType(true);
        component.setMimeType(MimeType.MIME_TYPE_TEXT_PLAIN);
        component.setResourceFile(dynamicResource);
        component.initialize();

        String sampleText = "my text";
        byte[] payloadAsBytes = sampleText.getBytes();
        doReturn(new TestResourceFile("/assets/mytext.txt", payloadAsBytes))
                .when(resourceService)
                .find(dynamicResource, ResourceReadDynamicConfiguration.DEFAULT_READ_BUFFER_SIZE, context, message);

        doReturn(TypedPublisher.fromString(Flux.just(sampleText)))
                .when(converterService)
                .convert(any(TypedPublisher.class), eq(String.class));

        // When
        Message result = component.apply(context, message);

        // Then
        String payload = result.payload();
        assertThat(payload).isEqualTo(sampleText);
        assertThat(result.attributes()).containsEntry("componentName", "ResourceReadDynamic");
        assertThat(result.attributes()).containsEntry("resourcePath", "/assets/mytext.txt");
        assertThat(result.attributes()).containsKey("timestamp");
    }

    static class TestResourceFile implements ResourceFile<byte[]> {

        private final String path;
        private final byte[] data;

        TestResourceFile(String path, byte[] data) {
            this.path = path;
            this.data = data;
        }

        @Override
        public String path() {
            return path;
        }

        @Override
        public Publisher<byte[]> data() {
            return Flux.just(data);
        }
    }
}
