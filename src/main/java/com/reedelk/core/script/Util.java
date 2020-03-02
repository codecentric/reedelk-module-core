package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.UUID;

@AutocompleteType(global = true, description = "Collection of utility functions")
public class Util {

    @AutocompleteItem(signature = "tmpdir()", description = "Returns the java tmp directory")
    public String tmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    @AutocompleteItem(signature = "uuid()", description = "Return a random UUID")
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
