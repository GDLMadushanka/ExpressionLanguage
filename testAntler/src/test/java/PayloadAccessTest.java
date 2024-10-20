import org.junit.Test;
import org.junit.Assert;
public class PayloadAccessTest {
    @Test
    public void testPayloadAccess() {
        Assert.assertEquals("John", TestUtils.evaluateExpressionWithPayload("payload.name",1));
        Assert.assertEquals("John", TestUtils.evaluateExpressionWithPayload("payload[\"name\"]",1));
        Assert.assertEquals("BMW", TestUtils.evaluateExpressionWithPayload("$.cars[1]",1));
        Assert.assertEquals("BMW", TestUtils.evaluateExpressionWithPayload("$.cars[$.index]",1));
        Assert.assertEquals("[\"BMW\",\"Lexus\"]", TestUtils.evaluateExpressionWithPayload("$.cars[$.index,4]",1));
    }
}
