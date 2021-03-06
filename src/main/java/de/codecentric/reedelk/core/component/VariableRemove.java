package de.codecentric.reedelk.core.component;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import org.osgi.service.component.annotations.Component;

import static de.codecentric.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotBlank;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Variable Remove")
@ComponentOutput(
        attributes = ComponentOutput.PreviousComponent.class,
        payload = ComponentOutput.PreviousComponent.class)
@ComponentInput(
        payload = Object.class,
        description = "Any input. The input message is not used by this component.")
@Description("Removes a variable previously set in the flow context.")
@Component(service = VariableRemove.class, scope = PROTOTYPE)
public class VariableRemove implements ProcessorSync {

    @Property("Name")
    @Hint("mySampleVariable")
    @Example("mySampleVariable")
    @Description("The name of the variable to be removed from the flow context.")
    private String name;

    @Override
    public void initialize() {
        requireNotBlank(VariableRemove.class, name, "Variable name to remove must not be empty");
    }

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        flowContext.remove(name);

        return message;
    }

    public void setName(String name) {
        this.name = name;
    }
}
