import org.junit.Test;
import org.junit.Assert;

public class PreDefinedFunctionsTest {
    @Test
    public void testLength() {
        Assert.assertEquals("6", TestUtils.evaluateExpression("length(\"Lahiru\")"));
        //combined with other functions
        Assert.assertEquals("16", TestUtils.evaluateExpression("length(\"Lahiru\") + 10"));
        Assert.assertEquals("3", TestUtils.evaluateExpression("length([\"LAHIRU\",3,5])"));
    }

    @Test
    public void testToUpper(){
        Assert.assertEquals("LAHIRU", TestUtils.evaluateExpression("toUpper(\"lahiru\")"));
    }

    @Test
    public void testToLower(){
        Assert.assertEquals("lahiru", TestUtils.evaluateExpression("toLower(\"LAHIRU\")"));
    }
}
