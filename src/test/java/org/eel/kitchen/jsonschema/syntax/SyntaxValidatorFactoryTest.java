/*
 * Copyright (c) 2012, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.eel.kitchen.jsonschema.syntax;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eel.kitchen.jsonschema.main.JsonValidator;
import org.eel.kitchen.jsonschema.main.ValidationConfig;
import org.eel.kitchen.jsonschema.main.ValidationReport;
import org.eel.kitchen.util.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.*;

//TODO: test reflection issues

public final class SyntaxValidatorFactoryTest
{
    private static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    private JsonNode allTests;
    private ValidationReport report;
    private JsonValidator validator;
    private final ValidationConfig cfg = new ValidationConfig();

    @BeforeClass
    public void setUp()
        throws IOException
    {
        allTests = JsonLoader.fromResource("/syntax/syntax.json");
    }

    @Test
    public void testNullSchema()
    {
        try {
            validator = new JsonValidator(cfg, null);
            fail("No exception thrown");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "schema is null");
        }
    }

    @Test
    public void testEmptySchema()
    {
        validator = new JsonValidator(cfg, nodeFactory.objectNode());
        report = validator.validateSchema();

        assertTrue(report.isSuccess());

        assertTrue(report.getMessages().isEmpty());
    }

    @Test
    public void testNonObjectSchema()
    {
        try {
            validator = new JsonValidator(cfg, nodeFactory.textNode("hello"));
            fail("No exception thrown");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "not a schema (not an object)");
        }
    }

    @Test
    public void testUnknownKeyword()
    {
        final ObjectNode schema = nodeFactory.objectNode();
        schema.put("toto", 2);

        validator = new JsonValidator(cfg, schema);

        report = validator.validateSchema();

        assertTrue(report.isSuccess());
    }

    @Test
    public void testAdditionalItems()
    {
        testKeyword("additionalItems");
    }

    @Test
    public void testAdditionalProperties()
    {
        testKeyword("additionalProperties");
    }

    @Test
    public void testDependencies()
    {
        testKeyword("dependencies");
    }

    @Test
    public void testDescription()
    {
        testKeyword("description");
    }

    @Test
    public void testDisallow()
    {
        testKeyword("disallow");
    }

    @Test
    public void testDivisibleBy()
    {
        testKeyword("divisibleBy");
    }

    @Test
    public void testDollarRef()
    {
        testKeyword("$ref");
    }

    @Test
    public void testEnum()
    {
        testKeyword("enum");
    }

    @Test
    public void testExclusiveMaximum()
    {
        testKeyword("exclusiveMaximum");
    }

    @Test
    public void testExclusiveMinimum()
    {
        testKeyword("exclusiveMinimum");
    }

    @Test
    public void testExtends()
    {
        testKeyword("extends");
    }

    @Test
    public void testId()
    {
        testKeyword("id");
    }

    @Test
    public void testItems()
    {
        testKeyword("items");
    }

    @Test
    public void testMaximum()
    {
        testKeyword("maximum");
    }

    @Test
    public void testMaxItems()
    {
        testKeyword("maxItems");
    }

    @Test
    public void testMaxLength()
    {
        testKeyword("maxLength");
    }

    @Test
    public void testMinimum()
    {
        testKeyword("minimum");
    }

    @Test
    public void testMinItems()
    {
        testKeyword("minItems");
    }

    @Test
    public void testMinLength()
    {
        testKeyword("minLength");
    }

    @Test
    public void testPatternProperties()
    {
        testKeyword("patternProperties");
    }

    @Test
    public void testPattern()
    {
        testKeyword("pattern");
    }

    @Test
    public void testProperties()
    {
        testKeyword("properties");
    }

    @Test
    public void testTitle()
    {
        testKeyword("title");
    }

    @Test
    public void testType()
    {
        testKeyword("type");
    }

    @Test
    public void testUniqueItems()
    {
        testKeyword("uniqueItems");
    }

    private void testKeyword(final String keyword)
    {
        final JsonNode node = allTests.get(keyword);

        for (final JsonNode element: node)
            testEntry(element);
    }

    private void testEntry(final JsonNode element)
    {
        final JsonNode schema = element.get("schema");
        final boolean valid = element.get("valid").booleanValue();

        validator = new JsonValidator(cfg, schema);

        report = validator.validateSchema();

        assertEquals(report.isSuccess(), valid, schema.toString());
    }
}
