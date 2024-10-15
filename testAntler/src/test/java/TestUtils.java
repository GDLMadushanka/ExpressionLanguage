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

public class TestUtils {
    public static String evaluateExpression(String expression) {
        CharStream input = CharStreams.fromString(expression);
        ExpressionLanguageLexer lexer = new ExpressionLanguageLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionLanguageParser parser = new ExpressionLanguageParser(tokens);
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();  // Remove default console error listener
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.expression();
        ExpressionLanguageParser.ExpressionContext expressionContext = parser.expression();
        ExpressionVisitor visitor = new ExpressionVisitor();
        ExpressionNode expressionNode = visitor.visit(tree);

        EvaluationContext context = new EvaluationContext();
        Object result = expressionNode.evaluate(context);
        return result.toString();
    }
}
