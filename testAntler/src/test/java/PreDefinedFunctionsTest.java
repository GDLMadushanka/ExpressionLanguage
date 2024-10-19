import org.junit.Test;
import org.junit.Assert;

public class PreDefinedFunctionsTest {
    @Test
    public void testLength() {
        Assert.assertEquals("6", TestUtils.evaluateExpression("length(\"Lahiru\")"));
    }
}
