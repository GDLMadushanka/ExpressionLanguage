package org.example.antlr.exception;

import org.antlr.v4.runtime.RecognitionException;

public class SyntaxError {
    private final int line;
    private final int charPositionInLine;
    private final String message;
    private final Object offendingSymbol;
    private final RecognitionException exception;

    public SyntaxError(int line, int charPositionInLine, String message, Object offendingSymbol, RecognitionException exception) {
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.message = message;
        this.offendingSymbol = offendingSymbol;
        this.exception = exception;
    }

    public int getLine() {
        return line;
    }

    public int getCharPositionInLine() {
        return charPositionInLine;
    }

    public String getMessage() {
        return message;
    }

    public Object getOffendingSymbol() {
        return offendingSymbol;
    }

    public RecognitionException getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "SyntaxError{" +
                "line=" + line +
                ", charPositionInLine=" + charPositionInLine +
                ", message='" + message + '\'' +
                ", offendingSymbol=" + offendingSymbol +
                ", exception=" + (exception != null ? exception.getClass().getSimpleName() : "None") +
                '}';
    }
}