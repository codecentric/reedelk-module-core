package com.reedelk.core.internal.script;

import org.junit.jupiter.api.BeforeAll;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class AbstractScriptEngineTest {

    protected static ScriptEngine engine;

    @BeforeAll
    protected static void beforeAll() {
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        engine = factory.getEngineByName("JavaScript");
    }
}
