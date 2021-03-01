package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.runtime.api.converter.ConverterService;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;
import de.codecentric.reedelk.runtime.api.resource.ResourceBinary;
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
import static org.mockito.Mockito.verifyZeroInteractions;

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
        component.setMimeType(MimeType.AsString.APPLICATION_BINARY);
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

        assertThat(result.attributes()).containsEntry("component", "de.codecentric.reedelk.core.component.ResourceReadBinary");
        assertThat(result.attributes()).containsEntry("resourcePath", "assets/img/donkey.jpg");
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
