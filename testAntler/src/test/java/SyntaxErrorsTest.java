import org.example.antlr.exception.SyntaxError;
import org.example.antlr.exception.SyntaxErrorListener;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SyntaxErrorsTest {

    SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener();

    @Test
    public void testValidExpressions() {
        TestUtils.evaluateWithErrorListener(syntaxErrorListener,"5 > 3");
        Assert.assertFalse(syntaxErrorListener.hasErrors());
    }

    @Test
    public void testOperationError() {
        TestUtils.evaluateWithErrorListener(syntaxErrorListener,"5 >> 3");
        Assert.assertTrue("Should throw an error",syntaxErrorListener.hasErrors());

        List<SyntaxError> errors = syntaxErrorListener.getErrors();
        Assert.assertEquals(1, errors.size());

        // Assert details of the error
        SyntaxError error = errors.get(0);
        Assert.assertTrue(error.getMessage().contains("extraneous input '>'"));
        Assert.assertEquals(3, error.getCharPositionInLine());

        syntaxErrorListener.clearErrors();
    }

//    @Test
//    public void testReservedKeyError() {
//        TestUtils.evaluateWithErrorListener(syntaxErrorListener,"payload.toUpper");
//        Assert.assertTrue("Should throw an error", syntaxErrorListener.hasErrors());
//
//        List<SyntaxError> errors = syntaxErrorListener.getErrors();
//        Assert.assertEquals(3, errors.size());
//
//        // Assert details of the error
//        SyntaxError error = errors.get(2);
//        Assert.assertTrue(error.getMessage().contains("missing '(' at '<EOF>"));
//        Assert.assertEquals(3, error.getCharPositionInLine());
//    }
}
