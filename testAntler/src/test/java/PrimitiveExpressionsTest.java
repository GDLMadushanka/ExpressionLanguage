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
        Assert.assertEquals("Error evaluating binary operation: \">\", Value :" +
                " \"bla\" cannot be converted to double", exception.getMessage());
    }

    @Test
    public void testLT() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 < 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 < -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5 < -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 < \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \"<\", Value :" +
                " \"bla\" cannot be converted to double", exception.getMessage());

        Assert.assertEquals("true", TestUtils.evaluateExpression("5 == 5"));
    }

    @Test
    public void testGTE() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 >= 3"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("5 >= -3.4"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("-5 >= -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 >= \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \">=\", Value :" +
                " \"bla\" cannot be converted to double", exception.getMessage());
    }

    @Test
    public void testLTE() {
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 <= 3"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("5 <= -3.4"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("-5 <= -3.4"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 <= \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \"<=\", Value :" +
                " \"bla\" cannot be converted to double", exception.getMessage());
    }

    @Test
    public void testAnd(){
        Assert.assertEquals("true", TestUtils.evaluateExpression("true and true"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true and false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("true && true && false"));
    }

    @Test
    public void testOr(){
        Assert.assertEquals("true", TestUtils.evaluateExpression("true or true"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true or false"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("true || true || false"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("false or false"));
    }

    @Test
    public void testAdd(){
        Assert.assertEquals("7", TestUtils.evaluateExpression("5 + 3 + -1"));
        Assert.assertEquals("8.5", TestUtils.evaluateExpression("5.5 + 3"));
        Assert.assertEquals("9.0", TestUtils.evaluateExpression("5.5 + 3.5"));
        Assert.assertEquals("abcxyz", TestUtils.evaluateExpression("\"abc\" + \"xyz\""));
        Assert.assertEquals("abc5", TestUtils.evaluateExpression("\"abc\" + 5"));
        Assert.assertEquals("5abc", TestUtils.evaluateExpression("5 + \"abc\""));
    }

    @Test
    public void testSubtract(){
        Assert.assertEquals("-33", TestUtils.evaluateExpression("5 - 30 + 2 - 10"));
        Assert.assertEquals("2.5", TestUtils.evaluateExpression("5.5 - 3"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpression("5.5 - 3.5"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 - \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \"-\", " +
                "Unsupported inputs for - operation: 5 and bla", exception.getMessage());
    }

    @Test
    public void testMultiply(){
        Assert.assertEquals("-30", TestUtils.evaluateExpression("5 * 3 * -2"));
        Assert.assertEquals("16.5", TestUtils.evaluateExpression("5.5 * 3"));
        Assert.assertEquals("19.25", TestUtils.evaluateExpression("5.5 * 3.5"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 * \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \"*\"," +
                " Unsupported inputs for * operation: 5 and bla", exception.getMessage());
    }

    @Test
    public void testDivide(){
        Assert.assertEquals("-4.0", TestUtils.evaluateExpression("10 / 2 / -2.5 * 2"));
        Assert.assertEquals("1.8333333333333333", TestUtils.evaluateExpression("5.5 / 3"));
        Assert.assertEquals("1.5714285714285714", TestUtils.evaluateExpression("5.5 / 3.5"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 / \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \"/\"," +
                " Unsupported inputs for / operation: 5 and bla", exception.getMessage());
    }

    @Test
    public void testMod(){
        Assert.assertEquals("1", TestUtils.evaluateExpression("10 % 3"));
        Assert.assertEquals("2.5", TestUtils.evaluateExpression("5.5 % 3"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpression("5.5 % 3.5"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("5 % \"bla\""));
        Assert.assertEquals("Error evaluating binary operation: \"%\"," +
                " Unsupported inputs for % operation: 5 and bla", exception.getMessage());
    }
}
