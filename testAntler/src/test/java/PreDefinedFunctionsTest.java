import org.example.antlr.exception.EvaluationException;
import org.junit.Test;
import org.junit.Assert;

public class PreDefinedFunctionsTest {
    @Test
    public void testLength() {
        Assert.assertEquals("6", TestUtils.evaluateExpression("length(\"Lahiru\")"));
        Assert.assertEquals("16", TestUtils.evaluateExpression("length(\"Lahiru\") + 10"));
        Assert.assertEquals("3", TestUtils.evaluateExpression("length([\"LAHIRU\",3,5])"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("length(34)"));
        Assert.assertEquals("Invalid argument type provided for length function", exception.getMessage());
        Assert.assertEquals("6", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "length(payload.store.book)", 2, 0));
        Assert.assertEquals("6", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "length(var.cars)", 2, 1));
        Assert.assertEquals("0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "length(var[\"empty\"])", 2, 1));
    }

    @Test
    public void testToUpper() {
        Assert.assertEquals("LAHIRU", TestUtils.evaluateExpression("toUpper(\"lahiru\")"));
        Assert.assertEquals("JOHN", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "toUpper(payload.name)", 1, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("toUpper(34)"));
        Assert.assertEquals("Invalid argument type provided for toUpper function", exception.getMessage());
        Assert.assertEquals("SPACE X", TestUtils.evaluateExpression("toUpper(\"space\") + \" X\""));
        Assert.assertEquals("GEORGE ORWELL",
                TestUtils.evaluateExpressionWithPayload("toUpper(payload.store.book[4].author)", 2));
    }

    @Test
    public void testToLower() {
        Assert.assertEquals("lahiru", TestUtils.evaluateExpression("toLower(\"LAHIRU\")"));
        Assert.assertEquals("the diary of a young girl", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "toLower(payload[\"store\"][\"book\"][5][\"title\"])", 2, 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("toLower(34)"));
        Assert.assertEquals("Invalid argument type provided for toLower function", exception.getMessage());
        Assert.assertEquals("europa clipper", TestUtils.evaluateExpression("\"europa \" + toLower(\"CLIpper\")"));
        Assert.assertEquals("george orwell",
                TestUtils.evaluateExpressionWithPayload("toLower(payload.store.book[4].author)", 2));
    }

    @Test
    public void testSubString() {
        Assert.assertEquals("hiru", TestUtils.evaluateExpression("subString(\"Lahiru\",2)"));
        Assert.assertEquals("hi", TestUtils.evaluateExpression("subString(\"Lahiru\",2,4)"));
        Assert.assertEquals("2", TestUtils.evaluateExpression("length(toUpper(subString(\"Lahiru\",2,4)))"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("subString(\"Hello\",\"a\",4)"));
        Assert.assertEquals("Invalid argument type provided for substring function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("subString(\"Hello\",\"a\")"));
        Assert.assertEquals("Invalid argument type provided for substring function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("subString(\"Hello\",20)"));
        Assert.assertEquals("Invalid index for substring: 20, source string length: 5", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("subString(\"Hello\",-2)"));
        Assert.assertEquals("Invalid index for substring: -2, source string length: 5", exception.getMessage());
        Assert.assertEquals("", TestUtils.evaluateExpression("subString(\"Hello\",5)"));
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("subString(\"Hello\",-2,4)"));
        Assert.assertEquals("Invalid substring indices: start=-2, end=4, string length=5", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayloadAndVariables("subString(\"Hello\",2,var.num1)", 2, 1));
        Assert.assertEquals("Invalid substring indices: start=2, end=10, string length=5", exception.getMessage());
    }

    @Test
    public void testStartsWith() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("startsWith(\"Curiosity\",\"Curi\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("startsWith(\"Curiosity\",\"sity\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload(
                "startsWith(\"Curiosity\",$.store.bicycle.color)", 2));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayload(
                "startsWith(\"red flag\",$.store.bicycle.color)", 2));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("startsWith(\"Curiosity\",34)"));
        Assert.assertEquals("Invalid argument type provided for startsWith function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("startsWith(payload.cars,\"Ford\")", 1));
        Assert.assertEquals("Invalid argument type provided for startsWith function", exception.getMessage());
    }

    @Test
    public void testEndsWith() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("endsWith(\"Opportunity\",\"unity\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("endsWith(\"Opportunity\",\"tune\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload(
                "endsWith(\"Curiosity\",$.store.bicycle.color)", 2));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayload(
                "endsWith(\"discovered\",$.store.bicycle.color)", 2));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("endsWith(\"Curiosity\",34)"));
        Assert.assertEquals("Invalid argument type provided for endsWith function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("endsWith(payload.cars,\"Ford\")", 1));
        Assert.assertEquals("Invalid argument type provided for endsWith function", exception.getMessage());
    }

    @Test
    public void testContains() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("contains(\"Perseverance\",\"sever\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("contains(\"Perseverance\",\"server\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload(
                "contains(\"Perseverance\",$.store.bicycle.color)", 2));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayload(
                "contains(\"discovered\",$.store.bicycle.color)", 2));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("contains(\"Curiosity\",34)"));
        Assert.assertEquals("Invalid argument type provided for contains function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("contains(payload.cars,\"Ford\")", 1));
        Assert.assertEquals("Invalid argument type provided for contains function", exception.getMessage());
        Assert.assertEquals("false", TestUtils.evaluateExpression("contains(\"sever\",\"Perseverance\")"));
    }

    @Test
    public void testTrim() {
        Assert.assertEquals("Ingenuity", TestUtils.evaluateExpression("trim(\" Ingenuity \")"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("trim(\" Ingenuity \") == \"Ingenuity\""));
        Assert.assertEquals("Hello World", TestUtils.evaluateExpressionWithPayload("trim($[\"string\"])", 1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("trim(34)"));
        Assert.assertEquals("Invalid argument type provided for trim function", exception.getMessage());
    }

    @Test
    public void testReplace() {
        Assert.assertEquals("Heppo", TestUtils.evaluateExpression("replace(\"Hello\", \"l\", \"p\")"));
        Assert.assertEquals("Hello", TestUtils.evaluateExpression("replace(\"Hello\", \"p\", \"q\")"));
        Assert.assertEquals("Hello", TestUtils.evaluateExpressionWithPayload(
                "replace(\"Hello\", \"p\", payload.name)",1));
        Assert.assertEquals("John has a BMW", TestUtils.evaluateExpressionWithPayload(
                "replace(\"John has a \" + payload.cars[2], \"Fiat\" , payload.cars[1])",1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload(
                        "replace(\"John has a \" + payload.cars[2], \"Fiat\" , payload.cars)",1));
        Assert.assertEquals("Invalid argument type provided for replace function", exception.getMessage());
    }

    @Test
    public void testSplit() {
        Assert.assertEquals("[\"Split\",\"a\",\"string\",\"by\",\"spaces\",\"and\",\"comma\"]",
                TestUtils.evaluateExpression("split(\"Split a string by spaces and,comma\", \"[, ]\")"));
        Assert.assertEquals("7", TestUtils.evaluateExpression(
                "length(split(\"Split a string by spaces and,comma\", \"[, ]\"))"));
        Assert.assertEquals("6", TestUtils.evaluateExpression(
                "length(split(\"NASA, launches; rovers to explore. Mars\", \"[,; .]+\"))"));
        Assert.assertEquals("[\" Moon Mars \"]", TestUtils.evaluateExpression(
                "split(\" Moon Mars \", \",\")"));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload(
                        "split(payload.cars, \"34\")",1));
        Assert.assertEquals("Invalid argument type provided for split function", exception.getMessage());
    }

    @Test
    public void testAbs() {
        Assert.assertEquals("5", TestUtils.evaluateExpression("abs(-5)"));
        Assert.assertEquals("5", TestUtils.evaluateExpression("abs(5)"));
        Assert.assertEquals("5.0", TestUtils.evaluateExpression("abs(-5.0)"));
        Assert.assertEquals("2.0", TestUtils.evaluateExpressionWithPayloadAndVariables("abs(var.num4)",0,1));
        Assert.assertEquals("8.0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "abs(var.num4 + payload.expensive )",2,1));
        Assert.assertEquals("4.0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "abs(var.num1 / var.num3)",2,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("abs(payload.cars)",1));
        Assert.assertEquals("Invalid argument type provided for abs function", exception.getMessage());
    }

    @Test
    public void testFloor() {
        Assert.assertEquals("-6.0", TestUtils.evaluateExpression("floor(-5.4)"));
        Assert.assertEquals("5.0", TestUtils.evaluateExpression("floor(5.9)"));
        Assert.assertEquals("-3.0", TestUtils.evaluateExpressionWithPayloadAndVariables("floor(var.num3)",0,1));
        Assert.assertEquals("2.0", TestUtils.evaluateExpressionWithPayloadAndVariables("floor(-1 * var.num3)",0,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("floor(payload.cars)",1));
        Assert.assertEquals("Invalid argument type provided for floor function", exception.getMessage());
    }

    @Test
    public void testCeil() {
        Assert.assertEquals("-5.0", TestUtils.evaluateExpression("ceil(-5.4)"));
        Assert.assertEquals("6.0", TestUtils.evaluateExpression("ceil(5.9)"));
        Assert.assertEquals("-2.0", TestUtils.evaluateExpressionWithPayloadAndVariables("ceil(var.num3)",0,1));
        Assert.assertEquals("3.0", TestUtils.evaluateExpressionWithPayloadAndVariables("ceil(-1 * var.num3)",0,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("ceil(payload.cars)",1));
        Assert.assertEquals("Invalid argument type provided for ceil function", exception.getMessage());
    }

    @Test
    public void testSqrt() {
        Assert.assertEquals("5.0", TestUtils.evaluateExpression("sqrt(25)"));
        Assert.assertEquals("NaN", TestUtils.evaluateExpression("sqrt(-25.0)"));
        Assert.assertEquals("10.0", TestUtils.evaluateExpressionWithPayloadAndVariables("sqrt(var.num1 * var.num1)",0,1));
        Assert.assertEquals("2.5", TestUtils.evaluateExpressionWithPayloadAndVariables("sqrt(var.num3 * var.num3)",0,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("sqrt(payload.cars)",1));
        Assert.assertEquals("Invalid argument type provided for sqrt function", exception.getMessage());
    }

    @Test
    public void testPow(){
        Assert.assertEquals("8.0", TestUtils.evaluateExpression("pow(2,3)"));
        Assert.assertEquals("1.0", TestUtils.evaluateExpression("pow(2,0)"));
        Assert.assertEquals("0.25", TestUtils.evaluateExpression("pow(2,-2)"));
        Assert.assertEquals("1.0", TestUtils.evaluateExpressionWithPayloadAndVariables("pow(var.num1,0)",0,1));
        Assert.assertEquals("1.0", TestUtils.evaluateExpressionWithPayloadAndVariables("pow(var.num3,0)",0,1));
        Assert.assertEquals("0.16", TestUtils.evaluateExpressionWithPayloadAndVariables("pow(var.num3,-2)",0,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("pow(payload.cars,2)",1));
        Assert.assertEquals("Invalid argument type provided for pow function", exception.getMessage());
    }

    @Test
    public void testB64Encode(){
        Assert.assertEquals("SGVsbG8gV29ybGQ=", TestUtils.evaluateExpression("base64encode(\"Hello World\")"));
        Assert.assertEquals("Sm9obg==", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "base64encode(payload[\"name\"])",1,0));
        Assert.assertEquals("Sm9obg==", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "base64encode(var.name,\"UTF-8\")",0,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("base64encode(34)"));
        Assert.assertEquals("Invalid argument type provided for base64encode function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("base64encode(\"Hello\",\"UTF-99\")"));
        Assert.assertEquals("Invalid charset provided for urlEncode function", exception.getMessage());
    }

    @Test
    public void testB64Decode(){
        Assert.assertEquals("admin:admin", TestUtils.evaluateExpression("base64decode(\"YWRtaW46YWRtaW4=\")"));
        Assert.assertEquals("WSO2MI", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "base64decode(var.encoded)",0,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("base64decode(34)"));
        Assert.assertEquals("Invalid argument type provided for base64decode function", exception.getMessage());
    }

    @Test
    public void testUrlEncode(){
        Assert.assertEquals("Hello+World", TestUtils.evaluateExpression("urlEncode(\"Hello World\")"));
        Assert.assertEquals("+Hello+World+", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "urlEncode(payload[\"string\"])",1,0));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("urlEncode(34)"));
        Assert.assertEquals("Invalid argument type provided for urlEncode function", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("urlEncode(\"Hello\",\"UTF-99\")"));
        Assert.assertEquals("Invalid charset provided for urlEncode function", exception.getMessage());
    }

    @Test
    public void testUrlDecode(){
        Assert.assertEquals("Hello World", TestUtils.evaluateExpression("urlDecode(\"Hello+World\")"));
        Assert.assertEquals(" Hello World  &", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "urlDecode(payload[\"string\"]) + \" \" + urlDecode(\"%26\")",1,0));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("urlDecode(34)"));
        Assert.assertEquals("Invalid argument type provided for urlDecode function", exception.getMessage());
    }

    @Test
    public void testIsNumber() {
        Assert.assertEquals("true", TestUtils.evaluateExpression("isNumber(34)"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("isNumber(\"34\")"));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isNumber(payload[\"age\"]) && isNumber(var.num1)",1,1));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isNumber(payload[\"string\"]) || isNumber(var.name)",1,1));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isNumber(var.empty)",1,1));
        Assert.assertEquals("false", TestUtils.evaluateExpression("isNumber(\"Hello\")"));
    }

    @Test
    public void testIsString(){
        Assert.assertEquals("true", TestUtils.evaluateExpression("isString(\"Hello\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("isString(34)"));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isString(payload[\"name\"]) && isString(var.name)",1,1));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isString(payload[\"age\"]) || isString(var.num1)",1,1));
        Assert.assertEquals("true", TestUtils.evaluateExpression("isString(\"34\")"));
    }

    @Test
    public void testIsArray(){
        Assert.assertEquals("true", TestUtils.evaluateExpression("isArray([\"Hello\",34])"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("isArray(\"Hello\")"));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isArray(payload[\"cars\"]) && isArray(var.cars)",1,1));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isArray(payload[\"age\"]) || isArray(var.num1)",1,1));
        Assert.assertEquals("true", TestUtils.evaluateExpression("isArray([\"34\"])"));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isArray(payload)",3,1));
    }

    @Test
    public void testIsObject(){
        Assert.assertEquals("false", TestUtils.evaluateExpression("isObject(\"Hello\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isObject(payload) && isObject(var.name)",3,1));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isObject(payload[\"age\"]) || isObject(var.num1)",1,1));
        Assert.assertEquals("true", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "isObject(payload.store)",2,1));
    }

    @Test
    public void testConvertToString(){
        Assert.assertEquals("12 Angry Men", TestUtils.evaluateExpression("string(12) + \" Angry Men\""));
        Assert.assertEquals("Hello", TestUtils.evaluateExpression("string(\"Hello\")"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("string(true)"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("string(false)"));
        Assert.assertEquals("[\"When\",\"my\",\"time\",\"comes\",\"Forget\",\"the\",\"wrong\",\"that\"," +
                "\"I've\",\"done\"]", TestUtils.evaluateExpressionWithPayload("string(payload)",3));
        Assert.assertEquals("300", TestUtils.evaluateExpressionWithPayloadAndVariables("string(var.age * 10)",0,1));
    }

    @Test
    public void testConvertToInteger() {
        Assert.assertEquals("34", TestUtils.evaluateExpression("integer(34)"));
        Assert.assertEquals("20.0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "integer(payload[\"expensive\"]) + var.num1",2,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("integer(\"Hello\")"));
        Assert.assertEquals("Conversion to integer failed for : Hello", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("integer(34.5)"));
        Assert.assertEquals("Conversion to integer failed for : 34.5", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("integer(payload)",3));
        Assert.assertEquals("Conversion to integer failed for : [\"When\",\"my\",\"time\",\"comes\",\"Forget\"," +
                "\"the\",\"wrong\",\"that\",\"I've\",\"done\"]", exception.getMessage());
    }

    @Test
    public void testConvertToFloat(){
        Assert.assertEquals("-34.0", TestUtils.evaluateExpression("float(-34)"));
        Assert.assertEquals("15.0", TestUtils.evaluateExpressionWithPayloadAndVariables(
                "float(payload[\"expensive\"]) + var.num2",2,1));
        EvaluationException exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpression("float(\"Hello\")"));
        Assert.assertEquals("Conversion to float failed for : Hello", exception.getMessage());
        exception = Assert.assertThrows(EvaluationException.class,
                () -> TestUtils.evaluateExpressionWithPayload("float(payload)",3));
        Assert.assertEquals("Conversion to float failed for : [\"When\",\"my\",\"time\",\"comes\",\"Forget\"," +
                "\"the\",\"wrong\",\"that\",\"I've\",\"done\"]", exception.getMessage());
    }

    @Test
    public void testConvertToBoolean(){
        Assert.assertEquals("true", TestUtils.evaluateExpression("boolean(\"true\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("boolean(\"bla\")"));
        Assert.assertEquals("false", TestUtils.evaluateExpression("boolean(\"1\")"));
        Assert.assertEquals("true", TestUtils.evaluateExpression("boolean(\"0\") || (5 > 2)"));
        Assert.assertEquals("false", TestUtils.evaluateExpressionWithPayload("boolean(payload)",3));
    }
}
