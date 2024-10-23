import org.example.antlr.exception.EvaluationException;
import org.junit.Assert;
import org.junit.Test;

public class PrimitiveExpressionsTest {

    @Test
    public void testEQ() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5.3 == -5.3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 == 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true == true"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true == false"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("\"abc\" == \"abc\""));
        Assert.assertEquals("false", TestUtils.evaluateExpression("\"abc\" == \"pqr\""));
    }

    @Test
    public void testNEQ() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5.3 != -5.3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 != 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true != true"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true != false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("\"abc\" != \"abc\""));
        Assert.assertEquals("true", TestUtils.evaluateExpression("\"abc\" != \"pqr\""));
    }

    @Test
    public void testGT() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 > 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 > -3.4"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5 > -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 > \"bla\""));
        Assert.assertEquals("Invalid arguments provided for comparison operation", exception.getMessage());
    }

    @Test
    public void testLT() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 < 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 < -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5 < -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 < \"bla\""));
        Assert.assertEquals("Invalid arguments provided for comparison operation", exception.getMessage());

        Assert.assertEquals("true", TestUtils.evaluateExpression("5 == 5"));
    }

    @Test
    public void testGTE() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 >= 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 >= -3.4"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5 >= -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 >= \"bla\""));
        Assert.assertEquals("Invalid arguments provided for comparison operation", exception.getMessage());
    }

    @Test
    public void testLTE() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 <= 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 <= -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5 <= -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 <= \"bla\""));
        Assert.assertEquals("Invalid arguments provided for comparison operation", exception.getMessage());
    }

    @Test
    public void testAnd() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("true and true"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true and false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true && true && false"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 and \"bla\""));
        Assert.assertEquals("Invalid arguments provided for logical operation", exception.getMessage());
    }

    @Test
    public void testOr() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("true or true"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true or false"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true || true || false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("false or false"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 or \"bla\""));
        Assert.assertEquals("Invalid arguments provided for logical operation", exception.getMessage());
    }

    @Test
    public void testAdd() {
        Assert.assertEquals("7", TestUtils.evaluateExpression("5 + 3 + -1"));
        Assert.assertEquals("8.5", TestUtils.evaluateExpression("5.5 + 3"));
        Assert.assertEquals("9.0", TestUtils.evaluateExpression("5.5 + 3.5"));
        Assert.assertEquals("abcxyz", TestUtils.evaluateExpression("\"abc\" + \"xyz\""));
        Assert.assertEquals("7.5", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1 + var.num3", 2, 1));
        Assert.assertEquals("20.0", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1 + payload.expensive", 2, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("\"abc\" + 5"));
        Assert.assertEquals("Invalid arguments provided for + operation", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayloadAndVariables("5 + var.name", 2, 1));
        Assert.assertEquals("Invalid arguments provided for + operation", exception.getMessage());
    }

    @Test
    public void testSubtract() {
        Assert.assertEquals("-33", TestUtils.evaluateExpression("5 - 30 + 2 - 10"));
        Assert.assertEquals("2.5", TestUtils.evaluateExpression("5.5 - 3"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpression("5.5 - 3.5"));
        Assert.assertEquals("12.5", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1 - var.num3", 2, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 - \"bla\""));
        Assert.assertEquals("Invalid arguments provided for - operation", exception.getMessage());
    }

    @Test
    public void testMultiply() {
        Assert.assertEquals("-30", TestUtils.evaluateExpression("5 * 3 * -2"));
        Assert.assertEquals("16.5", TestUtils.evaluateExpression("5.5 * 3"));
        Assert.assertEquals("19.25", TestUtils.evaluateExpression("5.5 * 3.5"));
        Assert.assertEquals("-25.0", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1 * var.num3", 2, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 * \"bla\""));
        Assert.assertEquals("Invalid arguments provided for * operation", exception.getMessage());
    }

    @Test
    public void testDivide() {
        Assert.assertEquals("-4.0", TestUtils.evaluateExpression("10 / 2 / -2.5 * 2"));
        Assert.assertEquals("3", TestUtils.evaluateExpression("9 / 3"));
        Assert.assertEquals("5.0", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1 / 2", 2, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 / \"bla\""));
        Assert.assertEquals("Invalid arguments provided for / operation", exception.getMessage());
    }

    @Test
    public void testMod() {
        Assert.assertEquals("1", TestUtils.evaluateExpression("10 % 3"));
        Assert.assertEquals("2.5", TestUtils.evaluateExpression("5.5 % 3"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpression("5.5 % 3.5"));
        Assert.assertEquals("0.0", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1 % 2", 2, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 % \"bla\""));
        Assert.assertEquals("Invalid arguments provided for % operation", exception.getMessage());
    }
}
