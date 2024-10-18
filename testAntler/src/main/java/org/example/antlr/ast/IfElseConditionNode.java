package org.example.antlr.ast;

import com.google.gson.JsonElement;
import org.example.antlr.context.EvaluationContext;

public class IfElseConditionNode implements ExpressionNode {

    private final ExpressionNode condition;
    private final ExpressionNode trueExpression;
    private final ExpressionNode falseExpression;

    public IfElseConditionNode(ExpressionNode condition, ExpressionNode trueExpression, ExpressionNode falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
//        if (condition.evaluate(context).isJsonPrimitive() && condition.evaluate(context).getAsBoolean()) {
//            return trueExpression.evaluate(context);
//        } else {
//            return falseExpression.evaluate(context);
//        }
        return null;
    }
}
