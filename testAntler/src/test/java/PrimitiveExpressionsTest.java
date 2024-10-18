import org.example.antlr.ast.ExpressionResult;
import org.junit.Assert;
import org.junit.Test;

public class PrimitiveExpressionsTest {

    @Test
    public void testMathFunctionCalls() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 > 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 > -3.4"));
    }
}
