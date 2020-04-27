package com.reedelk.core.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.DynamicValueUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Script")
@Description("Executes the given script function and sets the result of its evaluation in the payload content. " +
        "The script function must be defined in a file with .groovy extension in " +
        "the project's <i>resources/scripts</i> folder. The function must have the following signature:" +
        "<br>" +
        "<br>" +
        "<pre><code> fun myFunctionName(context, message) {<br>   // Function code<br>   'my result'<br> }<br></code></pre>" +
        "<br>" +
        "The <i>context</i> variable can be used to access data stored in the flow context and the <i>message</i> variable is the current flow message object. " +
        "To access the message content from the script use <code>message.payload()</code>.")
@Component(service = ScriptEvaluator.class, scope = PROTOTYPE)
public class ScriptEvaluator implements ProcessorSync {

    @Property("Mime type")
    @MimeTypeCombo
    @Example(MimeType.AsString.TEXT_XML)
    @DefaultValue(MimeType.AsString.TEXT_PLAIN)
    @Description("Sets the mime type of the script result in the message payload; " +
            "e.g: if the result of the script is JSON, then <i>application/json</i> should be selected." +
            "This is useful to let the following components in the flow know how to process the message payload set " +
            "by this script. For instance, the REST listener would use this information to set the correct content type " +
            "in the request's response body.")
    private String mimeType;

    @Property("Script")
    @Example("mapJsonModel.js")
    @Description("Sets the script file to be executed by this component. " +
            "Must be a file path and name starting from the project's resources/scripts directory.")
    private Script script;

    @Property("Target Variable")
    @Hint("myScriptResult")
    @Example("myScriptResult")
    @Group("Advanced")
    @Description("If the property is not empty, the result of the script evaluation is assigned to the given context" +
            " variable instead of the message payload.")
    private DynamicString target;

    @Reference
    private ScriptEngineService service;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        MimeType mimeType = MimeType.parse(this.mimeType, MimeType.TEXT_PLAIN);

        Object evaluated = service.evaluate(script, Object.class, flowContext, message).orElse(null);

        // If the target variable has been set, we assign to a context variable
        // the result of the Script evaluation and we return the original message.
        if (DynamicValueUtils.isNotNullOrBlank(target)) {
            service.evaluate(target, flowContext, message)
                    .ifPresent(contextVariableName ->
                            flowContext.put(contextVariableName, evaluated));
            return message;

        } else {
            return MessageBuilder.get(ScriptEvaluator.class)
                    .withJavaObject(evaluated, mimeType)
                    .build();
        }
    }

    public void setTarget(DynamicString target) {
        this.target = target;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public DynamicString getTarget() {
        return target;
    }
}
