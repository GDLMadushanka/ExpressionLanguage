package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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
    public JsonElement evaluate(EvaluationContext context) {
        JsonElement temp = target.evaluate(context);
        if (methodType == 0) {
            switch (methodName) {
                case "length":
                    if (temp.isJsonArray()) {
                        return new JsonPrimitive(temp.getAsJsonArray().size());
                    } else if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
                        return new JsonPrimitive(temp.getAsString().length());
                    } else {
                        throw new EvaluationException("Invalid method: length() called on " + temp);
                    }
                case "toUpper":
                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
                        return new JsonPrimitive(temp.getAsString().toUpperCase());
                    } else {
                        throw new EvaluationException("Invalid method: toUpper() called on " + temp);
                    }
                case "toLower":
                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
                        return new JsonPrimitive(temp.getAsString().toLowerCase());
                    } else {
                        throw new EvaluationException("Invalid method: toLower() called on " + temp);
                    }
                case "subString":
                    if (temp.isJsonPrimitive() && temp.getAsJsonPrimitive().isString()) {
                        if (arguments.size() != 2) {
                            throw new EvaluationException("Invalid number of arguments for subString()");
                        }
                        int start = arguments.get(0).evaluate(context).getAsInt();
                        int end = arguments.get(1).evaluate(context).getAsInt();
                        return new JsonPrimitive(temp.getAsString().substring(start, end));
                    } else {
                        throw new EvaluationException("Invalid method: subString() called on " + temp);
                    }

            }
        }
        return null;
    }

}
