package com.reedelk.core.script;

import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.ScriptSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class ScriptModules implements ScriptSource {

    public static String SCRIPT_RESOURCE_PATH = "/function/core-javascript-functions.js";

    private final long moduleId;

    private final Log log;
    private final Util util;
    private final Config config;

    public ScriptModules(long moduleId, ConfigurationService configurationService) {
        this.moduleId = moduleId;
        this.log = new Log();
        this.util = new Util();
        this.config = new Config(configurationService);
    }

    @Override
    public Map<String, Object> bindings() {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("Log", log);
        bindings.put("util", util);
        bindings.put("Config", config);
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
        return SCRIPT_RESOURCE_PATH;
    }
}
