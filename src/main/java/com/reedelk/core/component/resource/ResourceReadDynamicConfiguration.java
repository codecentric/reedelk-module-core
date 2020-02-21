package com.reedelk.core.component.resource;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ResourceReadDynamicConfiguration.class, scope = PROTOTYPE)
public class ResourceReadDynamicConfiguration implements Implementor {

    public static final int DEFAULT_READ_BUFFER_SIZE = 65536;

    @Property("Read buffer size")
    @Hint("65536")
    @Example("524288")
    @DefaultValue("65536")
    @Description("The buffer size used to read the files from the resources folder. " +
            "This parameter can be used to improve read performances. " +
            "If the files are big the buffer size should be bigger, otherwise for very small " +
            "files it should be kept smaller.")
    private Integer readBufferSize;

    public Integer getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(Integer readBufferSize) {
        this.readBufferSize = readBufferSize;
    }
}
