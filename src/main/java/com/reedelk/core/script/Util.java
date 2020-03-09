package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.UUID;

@AutocompleteType(global = true,
        description = "The Util type provides a set of utility functions such " +
                "as generating UUIDs or getting temporary directory on the file system.")
@AutocompleteItem(
        cursorOffset = 1,
        token = "merge",
        signature = "merge([deep], object1 [, objectN])",
        example = "Util.merge(obj1, obj2, obj3)",
        description = "Merge the contents of two or more objects together into a new object. " +
                "If the optional argument deep is true, the merge becomes recursive (aka. deep copy).")
@AutocompleteItem(
        cursorOffset = 1,
        token = "groupBy",
        signature = "groupBy(array,function)",
        example = "Util.groupBy([1,2,3], function(a,b) { return a; })",
        description = "Groups the object’s values by a criterion. " +
                "Pass either a string attribute to group by, or a function that returns the criterion.")
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
