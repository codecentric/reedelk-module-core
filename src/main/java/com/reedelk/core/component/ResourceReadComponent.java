package com.reedelk.core.component;

import com.reedelk.runtime.api.commons.FileUtils;
import com.reedelk.runtime.api.message.content.MimeType;

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

    protected static MimeType mimeTypeFrom(boolean autoMimeType, String mimeType, String filePath, MimeType defaultMime) {
        if (autoMimeType) {
            String pageFileExtension = FileUtils.getExtension(filePath);
            return MimeType.fromFileExtension(pageFileExtension);
        } else {
            return MimeType.parse(mimeType, defaultMime);
        }
    }
}
