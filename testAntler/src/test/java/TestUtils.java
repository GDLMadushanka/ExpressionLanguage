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

import java.util.Map;

public class TestUtils {

    private static String PAYLOAD1 = "{\"name\":\"John\",\"age\":30,\"cars\":[ \"Ford\", " +
            "\"BMW\", \"Fiat\", \"Honda\", \"Lexus\", \"KIA\" ],\"index\":1,\"string\":\" Hello World \"}";
    private static String PAYLOAD2 = "{\n" +
            "  \"store\": {\n" +
            "    \"book\": [\n" +
            "      {\n" +
            "        \"category\": \"reference\",\n" +
            "        \"author\": \"Nigel Rees\",\n" +
            "        \"title\": \"Sayings of the Century\",\n" +
            "        \"price\": 8.95\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"Herman Melville\",\n" +
            "        \"title\": \"Moby Dick\",\n" +
            "        \"isbn\": \"0-553-21311-3\",\n" +
            "        \"price\": 8.99\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"J.R.R. Tolkien\",\n" +
            "        \"title\": \"The Lord of the Rings\",\n" +
            "        \"isbn\": \"0-395-19395-8\",\n" +
            "        \"price\": 22.99\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"Harper Lee\",\n" +
            "        \"title\": \"To Kill a Mockingbird\",\n" +
            "        \"price\": 10.99\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"George Orwell\",\n" +
            "        \"title\": \"Animal Farm\",\n" +
            "        \"price\": 7.99\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"biography\",\n" +
            "        \"author\": \"Anne Frank\",\n" +
            "        \"title\": \"The Diary of a Young Girl\",\n" +
            "        \"price\": 6.99\n" +
            "      }\n" +
            "    ],\n" +
            "    \"bicycle\": {\n" +
            "      \"color\": \"red\",\n" +
            "      \"price\": 19.95\n" +
            "    }\n" +
            "  },\n" +
            "  \"expensive\": 10,\n" +
            "  \"selectedCategory\": \"biography\"\n" +
            "}\n";

    private static final Map<String,Object> variableMap1 = Map.of("name","John","age","30","cars",
            "[\"Ford\",\"BMW\",\"Fiat\",\"Honda\",\"Lexus\",\"KIA\"]","index","1",
            "num1",10,"num2",5,"num3",-2.5,"num4",-2.0,"encoded","V1NPMk1J",
            "empty","");

    private static final Map<String,Object> variableMap2 = Map.of("json1",PAYLOAD1,"json2",PAYLOAD2,"json3","[1,2,3,\"abc\"]");

    public static String evaluateExpression(String expression) {
        return evaluateExpressionWithPayload(expression, 0);
    }
    public static String evaluateExpressionWithPayload(String expression, int payloadId) {
        return evaluateExpressionWithPayloadAndVariables(expression, payloadId, 0);
    }
    public static String evaluateExpressionWithPayloadAndVariables(String expression, int payloadId, int variableMapId) {
        CharStream input = CharStreams.fromString(expression);
        ExpressionLexer lexer = new ExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokens);
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();  // Remove default console error listener
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.expression();
        ExpressionVisitor visitor = new ExpressionVisitor();
        ExpressionNode expressionNode = visitor.visit(tree);

        EvaluationContext context = new EvaluationContext();
        if (variableMapId == 1) {
            for (Map.Entry<String, Object> entry : variableMap1.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        } else if (variableMapId == 2) {
            for (Map.Entry<String, Object> entry : variableMap2.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        if (payloadId == 1) {
            context.setPayload(PAYLOAD1);
        } else if (payloadId == 2) {
            context.setPayload(PAYLOAD2);
        }
        ExpressionResult result = expressionNode.evaluate(context);
        return result.asString();
    }

}
