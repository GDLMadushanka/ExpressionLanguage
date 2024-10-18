package org.example.antlr.ast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.example.antlr.constants.Constants;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.util.ArrayList;
import java.util.List;

public class PredefinedMethodCallNode implements ExpressionNode {
    private ExpressionNode target;
    private String methodName;
    private final List<ExpressionNode> arguments;
    /**
     * 0: String
     * 1: Array
     * 2: Object
     */
    private int methodType;

    public PredefinedMethodCallNode(ArgumentListNode arguments) {
        this.arguments = arguments.getArguments();
    }

    public void setTarget(ExpressionNode target) {
        this.target = target;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
//        JsonElement temp = target.evaluate(context);
//        if (methodType == 0) {
//            switch (methodName) {
//                case Constants.LENGTH:
//                    if (temp.isJsonArray()) {
//                        return new JsonPrimitive(temp.getAsJsonArray().size());
//                    } else if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        return new JsonPrimitive(temp.getAsString().length());
//                    } else {
//                        throw new EvaluationException("Invalid method: length() called on " + temp);
//                    }
//                case Constants.TO_UPPER:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        return new JsonPrimitive(temp.getAsString().toUpperCase());
//                    } else {
//                        throw new EvaluationException("Invalid method: toUpper() called on " + temp);
//                    }
//                case Constants.TO_LOWER:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        return new JsonPrimitive(temp.getAsString().toLowerCase());
//                    } else {
//                        throw new EvaluationException("Invalid method: toLower() called on " + temp);
//                    }
//                case Constants.SUBSTRING:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        if (arguments.size() == 1) {
//                            int start = arguments.get(0).evaluate(context).getAsInt();
//                            return new JsonPrimitive(temp.getAsString().substring(start));
//                        } else if (arguments.size() == 2) {
//                            int start = arguments.get(0).evaluate(context).getAsInt();
//                            int end = arguments.get(1).evaluate(context).getAsInt();
//                            return new JsonPrimitive(temp.getAsString().substring(start, end));
//                        } else {
//                            throw new EvaluationException("Invalid number of arguments for subString()");
//                        }
//                    } else {
//                        throw new EvaluationException("Invalid method: subString() called on " + temp);
//                    }
//                case Constants.STARTS_WITH:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        String prefix = arguments.get(0).evaluate(context).getAsString();
//                        return new JsonPrimitive(temp.getAsString().startsWith(prefix));
//                    } else {
//                        throw new EvaluationException("Invalid method: startsWith() called on " + temp);
//                    }
//                case Constants.ENDS_WITH:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        String suffix = arguments.get(0).evaluate(context).getAsString();
//                        return new JsonPrimitive(temp.getAsString().endsWith(suffix));
//                    } else {
//                        throw new EvaluationException("Invalid method: endsWith() called on " + temp);
//                    }
//                case Constants.CONTAINS:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        String substring = arguments.get(0).evaluate(context).getAsString();
//                        return new JsonPrimitive(temp.getAsString().contains(substring));
//                    } else {
//                        throw new EvaluationException("Invalid method: contains() called on " + temp);
//                    }
//                case Constants.TRIM:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        return new JsonPrimitive(temp.getAsString().trim());
//                    } else {
//                        throw new EvaluationException("Invalid method: trim() called on " + temp);
//                    }
//                case Constants.REPLACE:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        String oldString = arguments.get(0).evaluate(context).getAsString();
//                        String newString = arguments.get(1).evaluate(context).getAsString();
//                        return new JsonPrimitive(temp.getAsString().replace(oldString, newString));
//                    } else {
//                        throw new EvaluationException("Invalid method: replace() called on " + temp);
//                    }
//                case Constants.SPLIT:
//                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
//                        String delimiter = arguments.get(0).evaluate(context).getAsString();
//                        String[] parts = temp.getAsString().split(delimiter);
//                        Gson gson = new Gson();
//                        return gson.toJsonTree(parts).getAsJsonArray();
//                    } else {
//                        throw new EvaluationException("Invalid method: split() called on " + temp);
//                    }
//            }
//        }
        return null;
    }

}
