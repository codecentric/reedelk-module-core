package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.UUID;

@AutocompleteType(global = true,
        description = "The Util type provides a set of utility functions such " +
                "as generating uuids or getting temporary directory on the file system.")
public class Util {

    @AutocompleteItem(
            signature = "tmpdir()",
            example = "Util.tmpdir()",
            description = "Returns a temporary directory which can be used to temporarily create and store files.")
    public String tmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    @AutocompleteItem(
            signature = "uuid()",
            example = "Util.uuid()",
            description = "Returns a type 4 (pseudo randomly generated) uuid.")
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
