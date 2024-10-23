import org.example.antlr.exception.EvaluationException;
import org.junit.Test;
import org.junit.Assert;

public class PayloadAndVariableAccessTest {

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
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayloadAndVariables("payload.random",1,1));
        Assert.assertEquals("Evaluating expression: $.random failed. Path not found in payload", exception.getMessage());
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
        Assert.assertEquals("[\"John\",30,[\"Ford\",\"BMW\",\"Fiat\",\"Honda\",\"Lexus\",\"KIA\"],1," +
                        "\" Hello World \",\"Ford\",\"BMW\",\"Fiat\",\"Honda\",\"Lexus\",\"KIA\"]",
                TestUtils.evaluateExpressionWithPayload("$..*",1));
    }

    @Test
    public void testVariableAccess() {
        Assert.assertEquals("John", TestUtils.evaluateExpressionWithPayloadAndVariables("var.name",1,1));
        Assert.assertEquals("10", TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1",1,1));
        Assert.assertEquals("-29.0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "(var.num1 * var.num3) - var.num2 + payload.index",1,1));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.num1 >= var.num2",1,1));
        Assert.assertEquals("2", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var.json3[1]",0,2));
        Assert.assertEquals("2", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var[\"json3\"][1]",0,2));
        Assert.assertEquals("[\"The Lord of the Rings\"]", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "var[\"json2\"][\"store\"][\"book\"][?(@.author=='J.R.R. Tolkien')].title",0,2));
        Assert.assertEquals("[\"Moby Dick\",\"To Kill a Mockingbird\"]",
                TestUtils.evaluateExpressionWithPayloadAndVariables(
                        "var[\"json2\"][\"store\"][\"book\"][1,3].title",0,2));
        Assert.assertEquals("[\"Animal Farm\",\"The Diary of a Young Girl\"]",
                TestUtils.evaluateExpressionWithPayloadAndVariables(
                        "var[\"json2\"][\"store\"][\"book\"][-2:].title",0,2));
        Assert.assertEquals("[\"Moby Dick\",\"The Lord of the Rings\",\"To Kill a Mockingbird\",\"Animal Farm\"]",
                TestUtils.evaluateExpressionWithPayloadAndVariables(
                        "var.json2.store.book[?(@.category=='fiction')].title",0,2));
        Assert.assertEquals("[\"The Lord of the Rings\",\"To Kill a Mockingbird\"]",
                TestUtils.evaluateExpressionWithPayloadAndVariables(
                        "var[\"json2\"][\"store\"][\"book\"][?(@.price > payload.expensive)].title",2,2));
        Assert.assertEquals("[\"The Lord of the Rings\",\"To Kill a Mockingbird\"]",
                TestUtils.evaluateExpressionWithPayloadAndVariables(
                        "var[\"json2\"].store.[\"book\"][?(@.price > payload.expensive)].title",2,2));
        Assert.assertEquals("[\"The Lord of the Rings\",\"To Kill a Mockingbird\"]",
                TestUtils.evaluateExpressionWithPayloadAndVariables(
                        "var.[\"json2\"].store.[\"book\"][?(@.price > payload.expensive)].title",2,2));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayloadAndVariables("var.random",0,1));
        Assert.assertEquals("Variable random is not defined", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayloadAndVariables("var.num1[0]",0,1));
        Assert.assertEquals("Evaluating expression: var.num1[0] failed. Path not found in variable", exception.getMessage());
    }
}
