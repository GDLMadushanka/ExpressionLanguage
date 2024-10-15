package org.example.antlr.ast;

import com.google.gson.JsonElement;
import org.example.antlr.context.EvaluationContext;

public class PropertyAccessNode implements ExpressionNode {
    private ExpressionNode target;
    private String propertyName;

    public PropertyAccessNode(ExpressionNode target, String propertyName) {
        this.target = target;
        this.propertyName = propertyName;
    }

    @Override
    public JsonElement evaluate(EvaluationContext context) {
        return null;
    }

}