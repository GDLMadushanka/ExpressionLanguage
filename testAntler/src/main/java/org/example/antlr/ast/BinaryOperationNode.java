package org.example.antlr.ast;

import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.util.function.BiFunction;
import java.util.logging.Logger;

/**
 * This class handles binary and logical operations between two nodes.
 */
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

        switch (operator) {
            case "+":
                return handleAddition(leftValue, rightValue);
            case "-":
                return handleArithmetic(leftValue, rightValue, "-");
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
    }

    private ExpressionResult handleComparison(ExpressionResult leftValue, ExpressionResult rightValue,
                                              BiFunction<Double, Double, Boolean> comparison) {
        if ((leftValue.isDouble() || leftValue.isInteger()) && (rightValue.isDouble() || rightValue.isInteger())) {
            return new ExpressionResult(comparison.apply(leftValue.asDouble(), rightValue.asDouble()));
        }
        throw new EvaluationException("Invalid arguments provided for comparison operation");
    }

    private ExpressionResult handleEquality(ExpressionResult leftValue, ExpressionResult rightValue,
                                            BiFunction<String, String, Boolean> comparison) {
        return new ExpressionResult(comparison.apply(leftValue.asString(), rightValue.asString()));
    }

    private ExpressionResult handleLogical(ExpressionResult leftValue, ExpressionResult rightValue,
                                           BiFunction<Boolean, Boolean, Boolean> logicOperation) {
        if (leftValue.isBoolean() && rightValue.isBoolean()) {
            return new ExpressionResult(logicOperation.apply(leftValue.asBoolean(), rightValue.asBoolean()));
        }
        throw new EvaluationException("Invalid arguments provided for logical operation");
    }

    private ExpressionResult handleAddition(ExpressionResult leftValue, ExpressionResult rightValue) {
        if (leftValue.isDouble() || rightValue.isDouble()) {
            return new ExpressionResult(leftValue.asDouble() + rightValue.asDouble());
        } else if (leftValue.isInteger() && rightValue.isInteger()) {
            return new ExpressionResult(leftValue.asInt() + rightValue.asInt());
        } else if (leftValue.isString() && rightValue.isString()) {
            return new ExpressionResult(leftValue.asString().concat(rightValue.asString()));
        } else {
            throw new EvaluationException("Invalid arguments provided for + operation");
        }
    }

    private ExpressionResult handleArithmetic(ExpressionResult leftValue, ExpressionResult rightValue,
                                              String operator) {
        boolean isDouble = leftValue.isDouble() || rightValue.isDouble();
        boolean isInteger = leftValue.isInteger() && rightValue.isInteger();
        switch (operator) {
            case "-":
                if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() - rightValue.asInt());
                } else if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() - rightValue.asDouble());
                } else {
                    throw new EvaluationException("Invalid arguments provided for - operation");
                }
            case "*":
                if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() * rightValue.asInt());
                } else if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() * rightValue.asDouble());
                } else {
                    throw new EvaluationException("Invalid arguments provided for * operation");
                }
            case "/":
                if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() / rightValue.asInt());
                } else if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() / rightValue.asDouble());
                } else {
                    throw new EvaluationException("Invalid arguments provided for / operation");
                }
            case "%":
                if (isInteger) {
                    return new ExpressionResult(leftValue.asInt() % rightValue.asInt());
                } else if (isDouble) {
                    return new ExpressionResult(leftValue.asDouble() % rightValue.asDouble());
                } else {
                    throw new EvaluationException("Invalid arguments provided for % operation");
                }
            default:
                throw new EvaluationException("Unsupported operator: " + operator);
        }
    }
}
