package org.example.antlr.exception;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class SyntaxErrorListener extends BaseErrorListener {
    private boolean hasErrors = false;

    public boolean hasErrors() {
        return hasErrors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        hasErrors = true;
        System.err.println("Syntax error at line " + line + ":" + charPositionInLine + " - " + msg);
    }
}
