package com.mitchellbosecke.pebble;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class tests if includes with parameters work.
 *
 * @author Thomas Hunziker
 *
 */
public class IncludeWithParameterTest extends AbstractTest {

    /**
     * Test if parameters are processed correctly.
     */
    @Test
    public void testIncludeWithParameters() throws PebbleException, IOException {
        PebbleEngine pebble = new PebbleEngine.Builder().strictVariables(false).build();
        PebbleTemplate template = pebble.getTemplate("templates/template.includeWithParameter1.peb");
        Map<String, Object> context = new HashMap<>();

        context.put("contextVariable", "some-context-variable");
        context.put("level", 1);
        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        String expectedOutput = "simple:simple-value" + "contextVariable:some-context-variable" + "map.position:left"
                + "map.contextVariable:some-context-variable" + "level:2" + "level-main:1";

        assertEquals(expectedOutput, writer.toString());

    }

    @Test
    public void testIncludeWithParametersIsolated() throws PebbleException, IOException {

        PebbleEngine pebble = new PebbleEngine.Builder().strictVariables(false).build();
        PebbleTemplate template = pebble.getTemplate("templates/template.includeWithParameterNotIsolated1.peb");
        Map<String, Object> context = new HashMap<>();

        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        String expectedOutput = "bazbar";

        assertEquals(expectedOutput, writer.toString());

    }

    @Test
    public void testIncludeWithGetAttributeExpression() throws PebbleException, IOException {

        PebbleEngine pebble = new PebbleEngine.Builder().strictVariables(false).build();
        PebbleTemplate template = pebble.getTemplate("templates/template.includeWithGetAttributeExpression1.peb");
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, String> subContext = new HashMap<>();
        subContext.put("label", "foo");
        data.put("subContext", subContext);
        context.put("data", data);

        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        String expectedOutput = "foo";

        assertEquals(expectedOutput, writer.toString());
    }

    @Test(expected = PebbleException.class)
    public void testWithFailedGetAttributeExpression() throws PebbleException, IOException {

        PebbleEngine pebble = new PebbleEngine.Builder().strictVariables(false).build();
        PebbleTemplate template = pebble.getTemplate("templates/template.includeWithGetAttributeExpression1.peb");
        Map<String, Object> context = new HashMap<>();
        context.put("data", Arrays.asList("foo", "bar"));

        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        String expectedOutput = "foo";

        assertEquals(expectedOutput, writer.toString());
    }
    
    @Test
    public void testIncludeWithContextExpression() throws PebbleException, IOException {

        PebbleEngine pebble = new PebbleEngine.Builder().strictVariables(false).build();
        PebbleTemplate template = pebble.getTemplate("templates/template.includeWithContextExpression1.peb");
        Map<String, Object> context = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("label", "foo");
        context.put("data", data);

        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        String expectedOutput = "foo";

        assertEquals(expectedOutput, writer.toString());
    }
}
