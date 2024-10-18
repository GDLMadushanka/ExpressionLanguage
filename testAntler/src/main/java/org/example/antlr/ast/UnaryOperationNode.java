package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.example.antlr.context.EvaluationContext;

public class UnaryOperationNode implements ExpressionNode {
    private final ExpressionNode expressionNode;
    private final Boolean negate;

    public UnaryOperationNode(ExpressionNode node, Boolean negate) {
        this.expressionNode = node;
        this.negate = negate;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
//        JsonElement value = expressionNode.evaluate(context);
//        boolean valid = value.isJsonPrimitive() && value.getAsJsonPrimitive().isBoolean()
//                && value.isJsonPrimitive() && value.getAsJsonPrimitive().isBoolean();
//        if (valid) {
//            if (negate) {
//                return new JsonPrimitive(!value.getAsBoolean());
//            } else {
//                return value;
//            }
//        } else {
//            throw new IllegalArgumentException("Invalid argument type for unary operation");
//        }
        return null;
    }
}
