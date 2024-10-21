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
        Assert.assertEquals("[\"BMW\",\"Fiat\",\"Honda\",\"Lexus\"]", TestUtils.evaluateExpressionWithPayload("$.cars[$.index:5]",1));
        Assert.assertEquals("[\"Ford\",\"BMW\"]", TestUtils.evaluateExpressionWithPayload("$.cars[:2]",1));
        Assert.assertEquals("[\"KIA\"]", TestUtils.evaluateExpressionWithPayload("$.cars[-1:]",1));
        Assert.assertEquals("[\"Lexus\",\"KIA\"]", TestUtils.evaluateExpressionWithPayload("payload.cars[payload.index + 3:]",1));
    }

    @Test
    public void testJSONPath() {
        Assert.assertEquals("[\"fiction\",\"Harper Lee\",\"To Kill a Mockingbird\",10.99]",
                TestUtils.evaluateExpressionWithPayload("$.store.book[3].*",2));
        Assert.assertEquals("[8.95,8.99,22.99,10.99,7.99,6.99,19.95]",
                TestUtils.evaluateExpressionWithPayload("$.store..price",2));
        Assert.assertEquals("[8.95,8.99,22.99,10.99,7.99,6.99,19.95]",
                TestUtils.evaluateExpressionWithPayload("$..price",2));
        Assert.assertEquals("[\"Sayings of the Century\",\"Moby Dick\",\"The Lord of the Rings\"," +
                        "\"To Kill a Mockingbird\",\"Animal Farm\",\"The Diary of a Young Girl\"]",
                TestUtils.evaluateExpressionWithPayload("$..book[*].title",2));
        Assert.assertEquals("[{\"category\":\"biography\",\"author\":\"Anne Frank\",\"title\":" +
                        "\"The Diary of a Young Girl\",\"price\":6.99}]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.category=='biography')]",2));
        Assert.assertEquals("[{\"category\":\"biography\",\"author\":\"Anne Frank\",\"title\":" +
                        "\"The Diary of a Young Girl\",\"price\":6.99}]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.category==payload.selectedCategory)]",2));
        Assert.assertEquals("[\"Animal Farm\"]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.author=='George Orwell')].title",2));
        Assert.assertEquals("[{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\"" +
                        ":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\"" +
                        ",\"author\":\"J.R.R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\"" +
                        ":\"0-395-19395-8\",\"price\":22.99}]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.isbn)]",2));
        Assert.assertEquals("[{\"category\":\"fiction\",\"author\":\"J.R.R. Tolkien\",\"title\":" +
                        "\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99},{\"category\":" +
                        "\"fiction\",\"author\":\"Harper Lee\",\"title\":\"To Kill a Mockingbird\",\"price\":10.99}]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.price > $.expensive)]",2));
        Assert.assertEquals("[{\"category\":\"fiction\",\"author\":\"J.R.R. Tolkien\",\"title\":" +
                        "\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.author =~ /.*Tolkien/i)]",2));
        Assert.assertEquals("[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\"" +
                        ":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"biography\",\"author\":" +
                        "\"Anne Frank\",\"title\":\"The Diary of a Young Girl\",\"price\":6.99}]",
                TestUtils.evaluateExpressionWithPayload("$..book[?(@.category == 'biography' " +
                        "|| @.category == 'reference')]",2));
        Assert.assertEquals("[\"John\",30,[\"Ford\",\"BMW\",\"Fiat\",\"Honda\",\"Lexus\",\"KIA\"],1,\"Ford\"," +
                        "\"BMW\",\"Fiat\",\"Honda\",\"Lexus\",\"KIA\"]",
                TestUtils.evaluateExpressionWithPayload("$..*",1));

    }
}
