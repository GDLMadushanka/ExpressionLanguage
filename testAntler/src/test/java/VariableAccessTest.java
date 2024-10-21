import org.junit.Test;
import org.junit.Assert;
public class VariableAccessTest {
    @Test
    public void testVariableAccess() {
        Assert.assertEquals("John", TestUtils.evaluateExpressionWithPayload("payload.name",1));

    }
}
