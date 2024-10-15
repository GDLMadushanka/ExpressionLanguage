package org.example.antlr.ast;

import com.google.gson.*;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.util.ArrayList;
import java.util.List;

public class ArrayLiteralNode implements ExpressionNode {
    private final List<ExpressionNode> elements = new ArrayList<>();
    public ArrayLiteralNode() {

    }
    public void addElement(ExpressionNode element) {
        elements.add(element);
    }

    @Override
    public JsonElement evaluate(EvaluationContext context) {
        JsonArray array = new JsonArray();
        for (ExpressionNode element : elements) {
            Object result = element.evaluate(context);
            // try to parse result to json
            try {
                JsonElement temp = JsonParser.parseString(result.toString());
                array.add(temp);
            } catch (JsonSyntaxException ex) {
                throw new EvaluationException("Result of " + element + " is not a valid JSON");
            }
        }
        return array;
    }
}
