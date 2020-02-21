package com.reedelk.core.component;

import com.reedelk.core.component.ResourceReadBinary;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.resource.ResourceBinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceReadBinaryTest {

    @Mock
    private FlowContext context;
    @Mock
    private Message message;
    @Mock
    private ConverterService converterService;

    private ResourceReadBinary component;

    @BeforeEach
    void setUp() {
        component = new ResourceReadBinary();
        component.converterService = converterService;
    }

    @Test
    void shouldNotConvertContentWhenBinaryMimeType() {
        // Given
        Flux<byte[]> byteStream = Flux.just("one".getBytes(), "two".getBytes());
        ResourceBinary resourceBinary = new TestResourceBinary("assets/img/donkey.jpg", byteStream);
        component.setMimeType(MimeType.MIME_TYPE_APPLICATION_BINARY);
        component.setAutoMimeType(true);
        component.setResourceFile(resourceBinary);

        // When
        Message result = component.apply(context, message);

        // Then
        verifyZeroInteractions(converterService);

        TypedContent<byte[], byte[]> content = result.content();
        Publisher<byte[]> dataStream = content.stream();
        StepVerifier.create(dataStream)
                .expectNextMatches(bytes -> Arrays.equals(bytes, "one".getBytes()))
                .expectNextMatches(bytes -> Arrays.equals(bytes, "two".getBytes()))
                .verifyComplete();

        assertThat(result.attributes()).containsEntry("componentName", "ResourceReadText");
        assertThat(result.attributes()).containsEntry("resourcePath", "assets/img/donkey.jpg");
        assertThat(result.attributes()).containsKey("timestamp");
    }

    @Test
    void shouldConvertContentWhenTextPlainMimeType() {
        // Given
        Flux<byte[]> byteStream = Flux.just("one".getBytes(), "two".getBytes());
        ResourceBinary resourceBinary = new TestResourceBinary("assets/css/style.css", byteStream);
        component.setMimeType(MimeType.MIME_TYPE_TEXT_PLAIN);
        component.setAutoMimeType(true);
        component.setResourceFile(resourceBinary);

        TypedPublisher<String> convertedStream = TypedPublisher.fromString(Flux.just("one", "two"));
        doReturn(convertedStream)
                .when(converterService)
                .convert(any(TypedPublisher.class), eq(String.class));

        // When
        Message result = component.apply(context, message);

        // Then
        StepVerifier.create(result.content().stream())
                .expectNext("one")
                .expectNext("two")
                .verifyComplete();

        assertThat(result.attributes()).containsEntry("componentName", "ResourceReadText");
        assertThat(result.attributes()).containsEntry("resourcePath", "assets/css/style.css");
        assertThat(result.attributes()).containsKey("timestamp");
    }

    static class TestResourceBinary extends ResourceBinary {

        private final Publisher<byte[]> data;

        public TestResourceBinary(String resourcePath, Publisher<byte[]> data) {
            super(resourcePath);
            this.data = data;
        }

        @Override
        public Publisher<byte[]> data() {
            return data;
        }
    }
}
