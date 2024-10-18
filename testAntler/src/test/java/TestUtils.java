
import com.example.antlr.ExpressionLexer;
import com.example.antlr.ExpressionParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.antlr.ast.ExpressionNode;
import org.example.antlr.ast.ExpressionResult;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.evaluator.ExpressionVisitor;
import org.example.antlr.exception.SyntaxErrorListener;

public class TestUtils {
    public static String evaluateExpression(String expression) {
        CharStream input = CharStreams.fromString(expression);
        ExpressionLexer lexer = new ExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();  // Remove default console error listener
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.expression();
        ExpressionParser.ExpressionContext expressionContext = parser.expression();
        ExpressionVisitor visitor = new ExpressionVisitor();
        ExpressionNode expressionNode = visitor.visit(tree);

        EvaluationContext context = new EvaluationContext();
        ExpressionResult result = expressionNode.evaluate(context);
        return result.asString();
    }
}
