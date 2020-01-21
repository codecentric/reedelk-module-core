package com.reedelk.core.component.flow;

import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.annotation.Hint;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.PropertyInfo;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("Variable Remove")
@Component(service = VariableRemove.class, scope = PROTOTYPE)
public class VariableRemove implements ProcessorSync {

    @Property("Name")
    @Hint("variable name")
    @PropertyInfo("The name of the variable to be removed from the flow context.")
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
