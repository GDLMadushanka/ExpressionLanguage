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
                case "+":
                    return handleAddition(leftValue, rightValue);
                case "-":
                    return handleArithmetic(leftValue, rightValue,"-");
                case "*":
                    return handleArithmetic(leftValue, rightValue, "*");
                case "/":
                    return handleArithmetic(leftValue, rightValue, "/");
                case "%":
                    return handleArithmetic(leftValue, rightValue, "%");
                case "==":
                    return handleEquality(leftValue, rightValue, String::equals);
                case "!=":
                    return handleEquality(leftValue, rightValue, (a, b) -> !a.equals(b));
                case "<":
                    return handleComparison(leftValue, rightValue, (a, b) -> a < b);
                case "<=":
                    return handleComparison(leftValue, rightValue, (a, b) -> a <= b);
                case ">":
                    return handleComparison(leftValue, rightValue, (a, b) -> a > b);
                case ">=":
                    return handleComparison(leftValue, rightValue, (a, b) -> a >= b);
                case "and":
                case "&&":
                    return handleLogical(leftValue, rightValue, (a, b) -> a && b);
                case "or":
                case "||":
                    return handleLogical(leftValue, rightValue, (a, b) -> a || b);
                default:
                    throw new EvaluationException("Unsupported operator: " + operator);
            }
        } catch (Exception e) {
            throw new EvaluationException("Error evaluating binary operation: \"" + operator + "\", "
                    + e.getMessage(), e);
        }
    }

    private ExpressionResult handleComparison(ExpressionResult leftValue, ExpressionResult rightValue,
                                              BiFunction<Double, Double, Boolean> comparison) {
        return new ExpressionResult(comparison.apply(leftValue.asDouble(), rightValue.asDouble()));
    }

    private ExpressionResult handleEquality(ExpressionResult leftValue, ExpressionResult rightValue,
                                            BiFunction<String, String, Boolean> comparison) {
        return new ExpressionResult(comparison.apply(leftValue.asString(), rightValue.asString()));
    }

    private ExpressionResult handleLogical(ExpressionResult leftValue, ExpressionResult rightValue,
                                           BiFunction<Boolean, Boolean, Boolean> logicOperation) {
        return new ExpressionResult(logicOperation.apply(leftValue.asBoolean(), rightValue.asBoolean()));
    }

    private ExpressionResult handleAddition(ExpressionResult leftValue, ExpressionResult rightValue) {
        if (leftValue.getType().equals(Double.class) || rightValue.getType().equals(Double.class)) {
            return new ExpressionResult(leftValue.asDouble() + rightValue.asDouble());
        } else if (leftValue.getType().equals(Integer.class) && rightValue.getType().equals(Integer.class)) {
            return new ExpressionResult(leftValue.asInt() + rightValue.asInt());
        } else if (leftValue.getType().equals(String.class) || rightValue.getType().equals(String.class)) {
            return new ExpressionResult(leftValue.asString().concat(rightValue.asString()));
        } else {
            throw new EvaluationException("Unsupported inputs for + operation: " + leftValue + " and " + rightValue);
        }
    }

    private ExpressionResult handleArithmetic(ExpressionResult leftValue, ExpressionResult rightValue,
                                         String operator) {
        boolean isDouble = leftValue.getType().equals(Double.class) || rightValue.getType().equals(Double.class);
        boolean isInteger = leftValue.getType().equals(Integer.class) && rightValue.getType().equals(Integer.class);
        switch (operator) {
            case "-":
                if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() - rightValue.asDouble());
                } else if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() - rightValue.asInt());
                } else {
                    throw new EvaluationException("Unsupported inputs for - operation: "
                            + leftValue.asString() + " and " + rightValue.asString());
                }
            case "*":
                if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() * rightValue.asDouble());
                } else if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() * rightValue.asInt());
                } else {
                    throw new EvaluationException("Unsupported inputs for * operation: "
                            + leftValue.asString() + " and " + rightValue.asString());
                }
            case "/":
                if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() / rightValue.asDouble());
                } else if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() / rightValue.asInt());
                } else {
                    throw new EvaluationException("Unsupported inputs for / operation: "
                            + leftValue.asString() + " and " + rightValue.asString());
                }
            case "%":
                if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() % rightValue.asDouble());
                } else if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() % rightValue.asInt());
                } else {
                    throw new EvaluationException("Unsupported inputs for % operation: "
                            + leftValue.asString() + " and " + rightValue.asString());
                }
            default:
                throw new EvaluationException("Unsupported operator: " + operator);
        }
    }
}
//

//

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


