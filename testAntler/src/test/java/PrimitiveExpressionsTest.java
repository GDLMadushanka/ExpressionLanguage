import org.junit.Assert;
import org.junit.Test;

public class PrimitiveExpressionsTest {
    @Test
    public void testMathFunctionCalls() {
        Assert.assertEquals("10.0", TestUtils.evaluateExpression("Math.sqrt(100)"));
        Assert.assertEquals("3.14", TestUtils.evaluateExpression("Math.abs(-3.14)"));
        Assert.assertEquals("44", TestUtils.evaluateExpression("Math.abs(-44)"));
        Assert.assertEquals("-4.0", TestUtils.evaluateExpression("Math.floor(-3.67)"));
        Assert.assertEquals("-3.0", TestUtils.evaluateExpression("Math.ceil(-3.67)"));
        Assert.assertEquals("2.302585092994046", TestUtils.evaluateExpression("Math.log(10)"));
        Assert.assertEquals("8.0", TestUtils.evaluateExpression("Math.pow(2,3)"));
    }

    @Test
    public void testStringMethodCalls() {
//        Assert.assertEquals("\"LAHIRU\"", TestUtils.evaluateExpression("\"lahiru\".toUpper()"));
//        Assert.assertEquals("\"lahiru\"", TestUtils.evaluateExpression("\"LAHIRU\".toLower()"));
//        Assert.assertEquals("6", TestUtils.evaluateExpression("\"lahiru\".length()"));
        Assert.assertEquals("\"hiru\"", TestUtils.evaluateExpression("\"lahiru\".substring(2)"));
//        Assert.assertEquals("\"hi\"", TestUtils.evaluateExpression("\"lahiru\".substring(2,4)"));
    }
}
