package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Variable Remove")
@Description("Removes a variable previously set in the flow context.")
@Component(service = VariableRemove.class, scope = PROTOTYPE)
public class VariableRemove implements ProcessorSync {

    @Property("Name")
    @Hint("mySampleVariable")
    @Example("mySampleVariable")
    @Description("The name of the variable to be removed from the flow context.")
    private String name;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        if (StringUtils.isNotBlank(name)) {
            flowContext.remove(name);
        }
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }
}
