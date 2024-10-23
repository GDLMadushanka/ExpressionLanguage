package org.example.antlr.ast;

import org.example.antlr.context.EvaluationContext;

public class SignedExpressionNode implements ExpressionNode {
    private final ExpressionNode expression;
    private final boolean signed;
    public SignedExpressionNode(ExpressionNode expression, boolean signed) {
        this.expression = expression;
        this.signed = signed;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        ExpressionResult result = expression.evaluate(context);
        if (signed) {
            if (result.isInteger()) {
                return new ExpressionResult(-result.asInt());
            } else if (result.isDouble()) {
                return new ExpressionResult(-result.asDouble());
            } else {
                throw new RuntimeException("Invalid type for signed expression: " + result.getType());
            }
        }
        return result;
    }
}
