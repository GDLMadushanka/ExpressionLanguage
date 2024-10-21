package org.example.antlr.ast;

import org.example.antlr.context.EvaluationContext;

public class SignedExpressionNode implements ExpressionNode {
    private ExpressionNode expression;
    private boolean signed = false;
    public SignedExpressionNode(ExpressionNode expression, boolean signed) {
        this.expression = expression;
        this.signed = signed;
    }


    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        ExpressionResult result = expression.evaluate(context);
        if (signed) {
            if (result.getType().equals(Integer.class)) {
                return new ExpressionResult(-result.asInt());
            } else if (result.getType().equals(Double.class)) {
                return new ExpressionResult(-result.asDouble());
            } else {
                throw new RuntimeException("Invalid type for signed expression: " + result.getType());
            }
        }
        return result;
    }
}
