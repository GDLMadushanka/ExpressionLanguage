package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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
    public JsonElement evaluate(EvaluationContext context) {
        switch (type) {
            case 0:
                try {
                    return new JsonPrimitive(Integer.parseInt(value));
                } catch (NumberFormatException e1) {
                    try {
                        return new JsonPrimitive(Float.parseFloat(value));
                    } catch (NumberFormatException e2) {
                        try{
                            return new JsonPrimitive(Double.parseDouble(value));
                        } catch (NumberFormatException e3) {
                            throw new IllegalArgumentException("Value "+value+" is not a number");
                        }
                    }
                }
            case 1:
                return new JsonPrimitive(value.substring(1, value.length() - 1));
            case 2:
                return new JsonPrimitive(Boolean.parseBoolean(value));
            case 3:
                return null;
        }
        return null;
    }
}
