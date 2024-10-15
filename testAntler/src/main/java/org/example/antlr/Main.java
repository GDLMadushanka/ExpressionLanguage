package org.example.antlr;

import com.example.antlr.ExpressionLanguageLexer;
import com.example.antlr.ExpressionLanguageParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.antlr.ast.ExpressionNode;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.evaluator.ExpressionVisitor;
import org.example.antlr.exception.SyntaxErrorListener;

public class Main {
    public static void main(String[] args) {
        CharStream input = CharStreams.fromString("Math.sqrt(100)");
        ExpressionLanguageLexer lexer = new ExpressionLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionLanguageParser parser = new ExpressionLanguageParser(tokens);
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();  // Remove default console error listener
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.expression();
        ExpressionLanguageParser.ExpressionContext expressionContext = parser.expression();
        ExpressionVisitor visitor = new ExpressionVisitor();
        ExpressionNode expression = visitor.visit(tree);

        EvaluationContext context = new EvaluationContext();
        Object result = expression.evaluate(context);
        System.out.println("Result: " + result);
    }
}
