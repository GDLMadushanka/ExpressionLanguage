package org.example.antlr.ast;

import com.google.gson.JsonElement;
import org.example.antlr.context.EvaluationContext;

public interface ExpressionNode {
    JsonElement evaluate(EvaluationContext context);
}
