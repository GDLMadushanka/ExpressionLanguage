package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.example.antlr.context.EvaluationContext;

public class ConstantsNode implements ExpressionNode {
    private String value;

    public ConstantsNode(String value) {
        this.value = value;
    }


    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
//        value = value.toLowerCase();
//        if (value.equals("math.pi")) {
//            return new JsonPrimitive(Math.PI);
//        }
        return null;
    }
}
