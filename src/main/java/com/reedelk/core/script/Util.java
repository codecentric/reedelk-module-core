package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;

import java.util.UUID;

@AutocompleteType
@AutocompleteItem(token = "Util", itemType = AutocompleteItemType.VARIABLE, replaceValue = "Util", returnType = "Util", description = "Collection of utility functions")
public class Util {

    @AutocompleteItem(replaceValue = "tmpdir()", description = "Returns the java tmp directory")
    public String tmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    @AutocompleteItem(replaceValue = "uuid()", description = "Return a random UUID")
    public String uuid() {
        return UUID.randomUUID().toString();
    }
}
