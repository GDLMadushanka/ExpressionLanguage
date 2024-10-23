package org.example.antlr.ast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.example.antlr.context.EvaluationContext;

public class LiteralNode implements ExpressionNode {
    private final String value;
    private ArgumentListNode parameterList = new ArgumentListNode();

    /*
        0: number
        1: string
        2: boolean
        3: null
        4: array
     */
    private final int type;

    public LiteralNode(String value, int type) {
        this.value = value;
        this.type = type;
    }

    public LiteralNode(ArgumentListNode value, int type) {
        this.parameterList = value;
        this.type = type;
        this.value = "";
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
            case 4:
                JsonArray jsonArray = new JsonArray();
                for (ExpressionNode expressionNode : parameterList.getArguments()) {
                    ExpressionResult result = expressionNode.evaluate(context);
                    if (result.getType().equals(JsonElement.class)) {
                        jsonArray.add(result.asJsonElement());
                    } else if (result.isInteger()) {
                        jsonArray.add(result.asInt());
                    } else if (result.isDouble()) {
                        jsonArray.add(result.asDouble());
                    } else {
                        jsonArray.add(result.asString());
                    }
                }
                return new ExpressionResult(jsonArray);
        }
        return null;
    }
}
