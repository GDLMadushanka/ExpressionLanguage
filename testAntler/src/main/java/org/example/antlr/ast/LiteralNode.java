package org.example.antlr.ast;

import org.example.antlr.context.EvaluationContext;

public class LiteralNode implements ExpressionNode {
    private final String value;

    /*
        0: number
        1: string
        2: boolean
        3: null
     */
    private final int type;

    public LiteralNode(String value, int type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        switch (type) {
            case 0:
                try {
                    return new ExpressionResult(Integer.parseInt(value));
                } catch (NumberFormatException e1) {
                    try {
                        return new ExpressionResult(Float.parseFloat(value));
                    } catch (NumberFormatException e2) {
                        try {
                            return new ExpressionResult(Double.parseDouble(value));
                        } catch (NumberFormatException e3) {
                            throw new IllegalArgumentException("Value " + value + " is not a number");
                        }
                    }
                }
            case 1:
                return new ExpressionResult(value);
            case 2:
                return new ExpressionResult(Boolean.parseBoolean(value));
            case 3:
                return new ExpressionResult();
        }
        return null;
    }
}
