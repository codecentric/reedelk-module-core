package com.reedelk.core.internal.attribute;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeProperty;
import com.reedelk.runtime.api.message.MessageAttributes;

import static com.reedelk.core.internal.attribute.ResourceReadAttributes.RESOURCE_PATH;
import static com.reedelk.core.internal.attribute.ResourceReadAttributes.TIMESTAMP;

@Type
@TypeProperty(name = RESOURCE_PATH, type = String.class)
@TypeProperty(name = TIMESTAMP, type = long.class)
public class ResourceReadAttributes extends MessageAttributes {

    static final String RESOURCE_PATH =  "resourcePath";
    static final String TIMESTAMP = "timestamp";

    public ResourceReadAttributes(String resourceFilePath) {
        put(RESOURCE_PATH, resourceFilePath);
        put(TIMESTAMP, System.currentTimeMillis());
    }
}
