package org.example.antlr.ast;

import com.google.gson.JsonElement;
import org.example.antlr.context.EvaluationContext;

import java.util.ArrayList;
import java.util.List;

public class ArgumentListNode implements ExpressionNode {
    private final List<ExpressionNode> arguments = new ArrayList<>();

    public ArgumentListNode() {

    }
    public void addArgument(ExpressionNode argument) {
        arguments.add(argument);
    }
    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public JsonElement evaluate(EvaluationContext context) {
        return null;
    }
}
