package com.reedelk.core.component;

import java.io.Serializable;
import java.util.Map;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;

abstract class ResourceReadComponent {

    private final String attributeResourcePath =  "resourcePath";
    private final String attributeTimestamp = "timestamp";

    protected Map<String, Serializable> createAttributes(String resourceFilePath) {
        return of(attributeResourcePath, resourceFilePath,
                                attributeTimestamp, System.currentTimeMillis());
    }
}
