package com.reedelk.core.internal;

import com.reedelk.core.internal.script.GlobalFunctions;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = ModuleActivator.class, scope = SINGLETON, immediate = true)
public class ModuleActivator {

    @Reference
    private ScriptEngineService scriptEngine;
    @Reference
    private ConfigurationService configurationService;

    @Activate
    public void start(BundleContext context) {
        long moduleId = context.getBundle().getBundleId();
        GlobalFunctions globalFunctions = new GlobalFunctions(moduleId, configurationService);
        scriptEngine.register(globalFunctions);
    }
}
