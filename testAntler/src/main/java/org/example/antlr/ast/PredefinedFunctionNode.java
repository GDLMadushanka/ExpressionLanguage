package org.example.antlr.ast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.example.antlr.constants.Constants;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.util.List;

public class PredefinedFunctionNode implements ExpressionNode {
    private String functionName;
    private final List<ExpressionNode> arguments;

    public PredefinedFunctionNode(ArgumentListNode arguments, String functionName) {
        this.arguments = arguments.getArguments();
        this.functionName = functionName;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        if (arguments.size() == 0) {
            if (functionName.equals(Constants.NOW)) {
                return new ExpressionResult(System.currentTimeMillis());
            }
        } else if (arguments.size() == 1) {
            ExpressionResult result = arguments.get(0).evaluate(context);
            if (functionName.equals(Constants.LENGTH)) {
                if (result.getType().equals(String.class)) {
                    return new ExpressionResult(result.asString().length());
                } else if (result.getType().equals(JsonArray.class)) {
                    return new ExpressionResult(result.asJsonElement().getAsJsonArray().size());
                } else {
                    throw new EvaluationException("Invalid argument type for length function");
                }
            } else if (functionName.equals(Constants.TO_LOWER)) {
                if (result.getType().equals(String.class)) {
                    return new ExpressionResult(result.asString().toLowerCase());
                } else if (result.getType().equals(JsonElement.class)) {
                    if (result.asJsonElement().isJsonPrimitive()) {
                        return new ExpressionResult(new JsonPrimitive(result.asJsonElement().getAsString().toLowerCase()));
                    } else {
                        throw new EvaluationException("Invalid argument type for toLower function");
                    }
                } else {
                    throw new EvaluationException("Invalid argument type for toLower function");
                }
            } else if (functionName.equals(Constants.TO_UPPER)) {
                if (result.getType().equals(String.class)) {
                    return new ExpressionResult(result.asString().toUpperCase());
                } else if (result.getType().equals(JsonElement.class)) {
                    if (result.asJsonElement().isJsonPrimitive()) {
                        return new ExpressionResult(new JsonPrimitive(result.asJsonElement().getAsString().toUpperCase()));
                    } else {
                        throw new EvaluationException("Invalid argument type for toUpper function");
                    }
                } else {
                    throw new EvaluationException("Invalid argument type for toUpper function");
                }
            }
        }

//        JsonElement result = arguments.get(0).evaluate(context);
//
//        if (functionName.equals(Constants.SUM) || functionName.equals(Constants.AVG) ||
//                functionName.equals(Constants.MIN) || functionName.equals(Constants.MAX)) {
//            // Array operations
//            if (!(result.isJsonArray())) {
//                throw new IllegalArgumentException("Invalid argument type for array function");
//            }
//            double sum = 0;
//            double min = Double.MAX_VALUE;
//            double max = Double.MIN_VALUE;
//            try {
//                for (JsonElement item : result.getAsJsonArray()) {
//                    if (item.isJsonPrimitive()) {
//                        double temp = item.getAsNumber().doubleValue();
//                        sum += temp;
//                        if (temp < min) {
//                            min = temp;
//                        }
//                        if (temp > max) {
//                            max = temp;
//                        }
//                    } else {
//                        throw new IllegalArgumentException("Array contains non-number elements");
//                    }
//                }
//            } catch (UnsupportedOperationException ex) {
//                throw new EvaluationException("Array " + result.getAsString() + " contains non-number elements");
//            }
//            switch (functionName) {
//                case Constants.SUM:
//                    return new JsonPrimitive(sum);
//                case Constants.AVG:
//                    return new JsonPrimitive(sum / result.getAsJsonArray().size());
//                case Constants.MIN:
//                    return new JsonPrimitive(min);
//                case Constants.MAX:
//                    return new JsonPrimitive(max);
//            }
//        } else {
//            // Single value operations
//            if (!(result.isJsonPrimitive() && result.getAsJsonPrimitive().isNumber())) {
//                throw new IllegalArgumentException("Invalid argument provided for math function "
//                        + result.getAsString());
//            }
//            Number numberValue = result.getAsNumber();
//            switch (functionName) {
//                case Constants.ABS:
//                    if (numberValue instanceof Integer) {
//                        return new JsonPrimitive(Math.abs(numberValue.intValue()));
//                    } else if (numberValue instanceof Long) {
//                        return new JsonPrimitive(Math.abs(numberValue.longValue()));
//                    } else if (numberValue instanceof Float) {
//                        return new JsonPrimitive(Math.abs(numberValue.floatValue()));
//                    } else if (numberValue instanceof Double) {
//                        return new JsonPrimitive(Math.abs(numberValue.doubleValue()));
//                    } else {
//                        throw new IllegalArgumentException("Unsupported number type for Math.abs");
//                    }
//                case Constants.CEIL:
//                    return new JsonPrimitive(Math.ceil(numberValue.doubleValue()));
//                case Constants.FLOOR:
//                    return new JsonPrimitive(Math.floor(numberValue.doubleValue()));
//                case Constants.SQRT:
//                    return new JsonPrimitive(Math.sqrt(numberValue.doubleValue()));
//                case Constants.LOGARITHM:
//                    return new JsonPrimitive(Math.log(numberValue.doubleValue()));
//                case Constants.POW:
//                    if (arguments.size() != 2) {
//                        throw new IllegalArgumentException("Invalid number of arguments for Math.pow function");
//                    }
//                    JsonElement powerResult = arguments.get(1).evaluate(context);
//                    if (!(powerResult.isJsonPrimitive())) {
//                        throw new IllegalArgumentException("Invalid argument type for function Math.pow");
//                    }
//                    if (powerResult.getAsJsonPrimitive().isNumber()) {
//                        return new JsonPrimitive(Math.pow(numberValue.doubleValue(),
//                                powerResult.getAsJsonPrimitive().getAsNumber().doubleValue()));
//                    } else {
//                        throw new EvaluationException(result.getAsString() + " is not a number");
//                    }
//            }
//        }
        return null;
    }
}
