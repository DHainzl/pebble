/*******************************************************************************
 * This file is part of Pebble.
 *
 * Copyright (c) 2014 by Mitchell Bösecke
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.mitchellbosecke.pebble.node;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

public class IncludeNode extends AbstractRenderableNode {

    private final Expression<?> includeExpression;

    private final Expression<?> mapExpression;

    public IncludeNode(int lineNumber, Expression<?> includeExpression, Expression<?> mapExpression) {
        super(lineNumber);
        this.includeExpression = includeExpression;
        this.mapExpression = mapExpression;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException,
            IOException {
        String templateName = (String) includeExpression.evaluate(self, context);

        Map<?, ?> map = Collections.emptyMap();
        if (this.mapExpression != null) {
            Object mapExpressionResult = this.mapExpression.evaluate(self, context);
            if (mapExpressionResult instanceof Map) {
                map = (Map<?, ?>) mapExpressionResult;
            } else {
                throw new PebbleException(null, String.format("Expression %1s could not be evaluated to a map",
                    this.mapExpression.getClass().getCanonicalName()), getLineNumber(), self.getName());
            }
        }

        if (templateName == null) {
            throw new PebbleException(
                    null,
                    "The template name in an include tag evaluated to NULL. If the template name is static, make sure to wrap it in quotes.",
                    getLineNumber(), self.getName());
        }
        self.includeTemplate(writer, context, templateName, map);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public Expression<?> getIncludeExpression() {
        return includeExpression;
    }

}
