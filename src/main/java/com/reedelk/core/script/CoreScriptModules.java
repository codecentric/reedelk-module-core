package com.reedelk.core.script;

import com.reedelk.runtime.api.annotation.AutoCompleteContributor;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.ScriptSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

@AutoCompleteContributor(contributions = {

        "Util[VARIABLE:Util]",
        "Util.tmpdir()[FUNCTION:String]",
        "Util.uuid()[FUNCTION:String]",

        "Log[VARIABLE:Log]",
        "Log.info('')[FUNCTION:void:2]",
        "Log.debug('')[FUNCTION:void:2]",
        "Log.warn('')[FUNCTION:void:2]",
        "Log.error('')[FUNCTION:void:2]",
        "Log.trace('')[FUNCTION:void:2]",

        "Config[VARIABLE:Config]",
        "Config.getString('')[FUNCTION:String:2]",
        "Config.getStringWithDefault('','')[FUNCTION:String:5]",
        "Config.getInt('')[FUNCTION:String:2]",
        "Config.getIntWithDefault('',5)[FUNCTION:String:3]",
        "Config.getLong('')[FUNCTION:String:2]",
        "Config.getLongWithDefault('',5)[FUNCTION:String:3]",
        "Config.getDouble('')[FUNCTION:String:2]",
        "Config.getDoubleWithDefault('',5)[FUNCTION:String:5]",
        "Config.getFloat('')[FUNCTION:String:2]",
        "Config.getFloatWithDefault('',5)[FUNCTION:String:5]",
        "Config.getBoolean('')[FUNCTION:String:2]",
        "Config.getBooleanWithDefault('',true)[FUNCTION:String:7]",

})
public class CoreScriptModules implements ScriptSource {

    private final long moduleId;
    private final Config config;

    public CoreScriptModules(long moduleId, ConfigurationService configurationService) {
        this.moduleId = moduleId;
        this.config = new Config(configurationService);
    }

    @Override
    public Map<String, Object> bindings() {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("logger", new ScriptLogger());
        bindings.put("config", config);
        return bindings;
    }

    @Override
    public Collection<String> scriptModuleNames() {
        return unmodifiableList(asList("Util", "Log", "Config"));
    }

    @Override
    public long moduleId() {
        return moduleId;
    }

    @Override
    public String resource() {
        return "/function/core-javascript-functions.js";
    }
}
