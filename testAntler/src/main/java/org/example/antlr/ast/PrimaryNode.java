package org.example.antlr.ast;

import com.google.gson.JsonElement;
import org.example.antlr.context.EvaluationContext;

public class PrimaryNode implements ExpressionNode {
    private final Object value;

    public PrimaryNode(Object value) {
        this.value = value;
    }


    @Override
    public JsonElement evaluate(EvaluationContext context) {
        return null;
    }
}
