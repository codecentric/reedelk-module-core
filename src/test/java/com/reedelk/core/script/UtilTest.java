package com.reedelk.core.script;

import com.reedelk.runtime.api.commons.FileUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.net.URL;
import java.util.*;

import static java.util.Arrays.*;
import static javax.script.ScriptContext.ENGINE_SCOPE;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
class UtilTest extends AbstractScriptEngineTest {

    @BeforeEach
    void setUp() throws ScriptException {
        URL resource = UtilTest.class.getResource(ScriptModules.SCRIPT_RESOURCE_PATH);
        String script = FileUtils.ReadFromURL.asString(resource);

        Bindings bindings = engine.getBindings(ENGINE_SCOPE);
        bindings.put("util", new Util());
        engine.eval(script, bindings);
    }

    @Nested
    @DisplayName("Merge Test")
    class Merge {

        @Test
        void shouldMergeProperties() throws ScriptException {
            // Given
            String script = "Util.merge({p1:'p1 value'},{p2: 2.3, p3: { p4: 'p4 value', p5: ['one', 'two']}})";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script);

            // Then
            String[] ownKeys = result.getOwnKeys(true);
            assertThat(ownKeys).containsExactly("p1","p2", "p3");

            assertThat(result.get("p1")).isEqualTo("p1 value");
            assertThat(result.get("p2")).isEqualTo(2.3);

            ScriptObjectMirror p3 = (ScriptObjectMirror) result.get("p3");
            assertThat(p3.get("p4")).isEqualTo("p4 value");

            ScriptObjectMirror p5 = (ScriptObjectMirror) p3.get("p5");
            assertThat(p5.get("0")).isEqualTo("one");
            assertThat(p5.get("1")).isEqualTo("two");
        }

        @Test
        void shouldDeepMergeProperties() throws ScriptException {
            // Given
            String script = "Util.merge(true, {one: '1', two: '2'}, {three:'3', two: {a: 'a value'}})";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script);

            // Then
            String[] ownKeys = result.getOwnKeys(true);
            assertThat(ownKeys).containsExactly("one","two", "three");

            assertThat(result.get("one")).isEqualTo("1");
            assertThat(result.get("three")).isEqualTo("3");

            ScriptObjectMirror two = (ScriptObjectMirror) result.get("two");
            assertThat(two.get("0")).isEqualTo("2");
            assertThat(two.get("a")).isEqualTo("a value");
        }

        @Test
        void shouldNotDeepMergeProperties() throws ScriptException {
            // Given
            String script = "Util.merge(false, {one: '1', two: '2'}, {three:'3', two: {a: 'a value'}})";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script);

            // Then
            String[] ownKeys = result.getOwnKeys(true);
            assertThat(ownKeys).containsExactly("one","two", "three");

            assertThat(result.get("one")).isEqualTo("1");
            assertThat(result.get("three")).isEqualTo("3");

            ScriptObjectMirror two = (ScriptObjectMirror) result.get("two");
            assertThat(two.get("a")).isEqualTo("a value");

            String[] twoKeys = two.getOwnKeys(true);
            assertThat(twoKeys).containsExactly("a");
        }

        @Test
        void shouldNotDeepMergePropertiesByDefault() throws ScriptException {
            // Given
            String script = "Util.merge({one: '1', two: '2'}, {three:'3', two: {a: 'a value'}})";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script);

            // Then
            String[] ownKeys = result.getOwnKeys(true);
            assertThat(ownKeys).containsExactly("one","two", "three");

            assertThat(result.get("one")).isEqualTo("1");
            assertThat(result.get("three")).isEqualTo("3");

            ScriptObjectMirror two = (ScriptObjectMirror) result.get("two");
            assertThat(two.get("a")).isEqualTo("a value");

            String[] twoKeys = two.getOwnKeys(true);
            assertThat(twoKeys).containsExactly("a");
        }
    }

    @Nested
    @DisplayName("Group By Test")
    class GroupBy {

        @Test
        void shouldGroupByPropertyName() throws ScriptException {
            // Given
            String script = "Util.groupBy([{p1: 'c1', p2: 'p2 value'}, {p1: 'c2'}, {p1: 'c1', p3: 'p3 value'}], 'p1')";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script);

            // Then
            assertThat(result).hasSize(2); // two groups

            ScriptObjectMirror c1Group = (ScriptObjectMirror) result.get("c1"); // first group
            assertThat(c1Group).hasSize(2);

            ScriptObjectMirror o1 = (ScriptObjectMirror) c1Group.get("0");
            assertThat(o1.get("p1")).isEqualTo("c1");
            assertThat(o1.get("p2")).isEqualTo("p2 value");

            ScriptObjectMirror o3 = (ScriptObjectMirror) c1Group.get("1");
            assertThat(o3.get("p1")).isEqualTo("c1");
            assertThat(o3.get("p3")).isEqualTo("p3 value");


            ScriptObjectMirror c2Group = (ScriptObjectMirror) result.get("c2"); // second group
            assertThat(c2Group).hasSize(1);

            ScriptObjectMirror o2 = (ScriptObjectMirror) c2Group.get("0");
            assertThat(o2.get("p1")).isEqualTo("c2");
        }

        @Test
        void shouldGroupByWhenInputIsList() throws ScriptException {
            // Given
            Map<String,Object> object1 = new HashMap<>();
            object1.put("p1", "c1");
            object1.put("p2", "p2 value");

            Map<String,Object> object2 = new HashMap<>();
            object2.put("p1", "c2");

            Map<String,Object> object3 = new HashMap<>();
            object3.put("p1", "c1");
            object3.put("p3", "p3 value");

            Bindings collectionBindings = engine.getBindings(ENGINE_SCOPE);
            collectionBindings.put("collection", asList(object1, object2, object3));

            String script = "Util.groupBy(collection, 'p1')";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script, collectionBindings);

            // Then
            assertThat(result).hasSize(2); // two groups

            ScriptObjectMirror c1Group = (ScriptObjectMirror) result.get("c1"); // first group
            assertThat(c1Group).hasSize(2);

            HashMap<String,Object> o1 = (HashMap<String,Object>) c1Group.get("0");
            assertThat(o1.get("p1")).isEqualTo("c1");
            assertThat(o1.get("p2")).isEqualTo("p2 value");

            HashMap<String,Object> o3 = (HashMap<String,Object>) c1Group.get("1");
            assertThat(o3.get("p1")).isEqualTo("c1");
            assertThat(o3.get("p3")).isEqualTo("p3 value");


            ScriptObjectMirror c2Group = (ScriptObjectMirror) result.get("c2"); // second group
            assertThat(c2Group).hasSize(1);

            HashMap<String,Object> o2 = (HashMap<String,Object>) c2Group.get("0");
            assertThat(o2.get("p1")).isEqualTo("c2");
        }

        @Test
        void shouldGroupByWhenInputIsSet() throws ScriptException {
            // Given
            Map<String,Object> object1 = new HashMap<>();
            object1.put("p1", "c1");
            object1.put("p2", "p2 value");

            Map<String,Object> object2 = new HashMap<>();
            object2.put("p1", "c2");

            Map<String,Object> object3 = new HashMap<>();
            object3.put("p1", "c1");
            object3.put("p3", "p3 value");

            Set<Object> values = new HashSet<>();
            values.add(object1);
            values.add(object2);
            values.add(object3);

            Bindings collectionBindings = engine.getBindings(ENGINE_SCOPE);
            collectionBindings.put("collection", values);

            String script = "Util.groupBy(collection, 'p1')";

            // When
            ScriptObjectMirror result = (ScriptObjectMirror) engine.eval(script, collectionBindings);

            // Then
            assertThat(result).hasSize(2); // two groups

            ScriptObjectMirror c1Group = (ScriptObjectMirror) result.get("c1"); // first group
            assertThat(c1Group).hasSize(2);

            HashMap<String,Object> o1 = (HashMap<String,Object>) c1Group.get("0");
            assertThat(o1.get("p1")).isEqualTo("c1");
            assertThat(o1.get("p2")).isEqualTo("p2 value");

            HashMap<String,Object> o3 = (HashMap<String,Object>) c1Group.get("1");
            assertThat(o3.get("p1")).isEqualTo("c1");
            assertThat(o3.get("p3")).isEqualTo("p3 value");


            ScriptObjectMirror c2Group = (ScriptObjectMirror) result.get("c2"); // second group
            assertThat(c2Group).hasSize(1);

            HashMap<String,Object> o2 = (HashMap<String,Object>) c2Group.get("0");
            assertThat(o2.get("p1")).isEqualTo("c2");
        }
    }
}
