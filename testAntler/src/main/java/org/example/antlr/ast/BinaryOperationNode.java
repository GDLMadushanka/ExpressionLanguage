package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryOperationNode implements ExpressionNode {

    private static final Logger logger = Logger.getLogger(BinaryOperationNode.class.getName());

    private final ExpressionNode left;
    private final ExpressionNode right;
    private final String operator;

    public BinaryOperationNode(ExpressionNode left, String operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator.trim().toLowerCase();
        this.right = right;
        logger.fine("Created BinaryOperationNode with operator: " + operator);
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        ExpressionResult leftValue = left.evaluate(context);
        ExpressionResult rightValue = right.evaluate(context);

        try {
            switch (operator) {
//                case "+":
//                    return handleAddition(leftValue, rightValue);
//                case "-":
//                    return handleArithmetic(leftValue, rightValue, (a, b) -> a - b);
//                case "*":
//                    return handleArithmetic(leftValue, rightValue, (a, b) -> a * b);
//                case "/":
//                    return handleArithmetic(leftValue, rightValue, (a, b) -> a / b);
//                case "%":
//                    return handleArithmetic(leftValue, rightValue, (a, b) -> a % b);
                case "==":
                    return handleComparison(leftValue, rightValue, (a, b) -> a == b);
                case "!=":
                    return handleComparison(leftValue, rightValue, (a, b) -> a != b);
                case "<":
                    return handleComparison(leftValue, rightValue, (a, b) -> a < b);
                case "<=":
                    return handleComparison(leftValue, rightValue, (a, b) -> a <= b);
                case ">":
                    return handleComparison(leftValue, rightValue, (a, b) -> a > b);
                case ">=":
                    return handleComparison(leftValue, rightValue, (a, b) -> a >= b);
//                case "and":
//                    return handleLogical(leftValue, rightValue, (a, b) -> a && b);
//                case "or":
//                    return handleLogical(leftValue, rightValue, (a, b) -> a || b);
                default:
                    throw new EvaluationException("Unsupported operator: " + operator);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error evaluating binary operation", e);
            throw new EvaluationException("Error evaluating binary operation: " + e.getMessage(), e);
        }
    }

    private ExpressionResult handleComparison(ExpressionResult leftValue, ExpressionResult rightValue,
                                              BiFunction<Double, Double, Boolean> comparison) {
        return new ExpressionResult(comparison.apply(leftValue.asDouble(), rightValue.asDouble()));
    }
}


//    private JsonElement handleAddition(JsonElement leftValue, JsonElement rightValue) {
//        if (isNumber(leftValue) && isNumber(rightValue)) {
//            double result = leftValue.getAsDouble() + rightValue.getAsDouble();
//            return new JsonPrimitive(result);
//        } else if (isString(leftValue) || isString(rightValue)) {
//            String result = leftValue.getAsString() + rightValue.getAsString();
//            return new JsonPrimitive(result);
//        } else {
//            throw new EvaluationException("Unsupported inputs for + operation: " + leftValue + " and " + rightValue);
//        }
//    }
//
//    private JsonElement handleArithmetic(JsonElement leftValue, JsonElement rightValue,
//                                         BiFunction<Double, Double, Double> operation) {
//        if (isNumber(leftValue) && isNumber(rightValue)) {
//            double result = operation.apply(leftValue.getAsDouble(), rightValue.getAsDouble());
//            return new JsonPrimitive(result);
//        } else {
//            throw new EvaluationException("Unsupported inputs for " + operator + " operation: "
//                    + leftValue + " and " + rightValue);
//        }
//    }
//

//
//    private JsonElement handleLogical(JsonElement leftValue, JsonElement rightValue,
//                                      BiFunction<Boolean, Boolean, Boolean> logicOperation) {
//        if (isBoolean(leftValue) && isBoolean(rightValue)) {
//            boolean result = logicOperation.apply(leftValue.getAsBoolean(), rightValue.getAsBoolean());
//            return new JsonPrimitive(result);
//        } else {
//            throw new EvaluationException("Unsupported inputs for " + operator.toUpperCase()
//                    + " operation: " + leftValue + " and " + rightValue);
//        }
//    }
//
//    private boolean equals(JsonElement leftValue, JsonElement rightValue) {
//        if (isNumber(leftValue) && isNumber(rightValue)) {
//            return leftValue.getAsDouble() == rightValue.getAsDouble();
//        } else if (isString(leftValue) && isString(rightValue)) {
//            return leftValue.getAsString().equals(rightValue.getAsString());
//        } else if (isBoolean(leftValue) && isBoolean(rightValue)) {
//            return leftValue.getAsBoolean() == rightValue.getAsBoolean();
//        } else {
//            return leftValue.equals(rightValue);
//        }
//    }
//
//    private boolean isNumber(JsonElement element) {
//        return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
//    }
//
//    private boolean isString(JsonElement element) {
//        return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
//    }
//
//    private boolean isBoolean(JsonElement element) {
//        return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean();
//    }


