package org.example.antlr.ast;

import org.example.antlr.context.EvaluationContext;

public interface ExpressionNode {
    ExpressionResult evaluate(EvaluationContext context);
}