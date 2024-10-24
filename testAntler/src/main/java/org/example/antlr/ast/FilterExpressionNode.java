package org.example.antlr.ast;

import org.example.antlr.Utils.ExpressionUtils;
import org.example.antlr.context.EvaluationContext;
import java.util.Map;

public class FilterExpressionNode implements ExpressionNode {


    private String expression;
    private final Map<String, ExpressionNode> arguments;

    public FilterExpressionNode(String expression, Map<String, ExpressionNode> arguments) {
        this.expression = expression;
        this.arguments = arguments;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        for (Map.Entry<String, ExpressionNode> entry : arguments.entrySet()) {
            if (entry.getValue() != null) {
                ExpressionResult result = entry.getValue().evaluate(context);
                if (result != null) {
                    String regex = ExpressionUtils.escapeSpecialCharacters(entry.getKey());
                    String resultString = result.asString();
                    if (result.isString()) {
                        resultString = "\"" + resultString + "\"";
                    }
                    expression = expression.replaceFirst(regex, resultString);
                }
            }
        }
        return new ExpressionResult("?" + expression);
    }
}
