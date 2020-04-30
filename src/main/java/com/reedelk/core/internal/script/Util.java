package com.reedelk.core.internal.script;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;

import java.util.UUID;

@Type(global = true,
        description = "The Util type provides a set of utility functions such " +
                "as generating UUIDs or getting temporary directory on the file system.")
public class Util {

    @TypeFunction(
            signature = "tmpdir()",
            example = "Util.tmpdir()",
            description = "Returns a temporary directory which can be used to temporarily create and store files.")
    public String tmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    @TypeFunction(
            signature = "uuid()",
            example = "Util.uuid()",
            description = "Returns a type 4 (pseudo randomly generated) uuid.")
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
