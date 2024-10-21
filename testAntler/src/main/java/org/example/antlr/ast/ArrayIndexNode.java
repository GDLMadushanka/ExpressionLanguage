package org.example.antlr.ast;

import org.example.antlr.context.EvaluationContext;

import java.util.ArrayList;
import java.util.List;

public class ArrayIndexNode implements ExpressionNode {
    private List<ExpressionNode> indexArray = new ArrayList<>();
    private char separator = ',';
    public ArrayIndexNode(ArgumentListNode arguments, char separator) {
        this.indexArray = arguments.getArguments();
        this.separator = separator;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        List<String> indexList = new ArrayList<>();
        for (ExpressionNode index : indexArray) {
            if(index == null) {
                indexList.add("");
                continue;
            }
            ExpressionResult result = index.evaluate(context);
            if (result != null) {
                indexList.add(result.asString());
            }
        }
        return new ExpressionResult(String.join(String.valueOf(separator), indexList));
    }
}
