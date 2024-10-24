package org.example.antlr.exception;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyntaxErrorListener extends BaseErrorListener {
    private boolean hasErrors = false;

    // List to store error details
    private final List<SyntaxError> errors = Collections.synchronizedList(new ArrayList<>());

    // Logger instance
    private final Logger logger = Logger.getLogger(SyntaxErrorListener.class.getName());

    /**
     * Check if there are any syntax errors captured.
     *
     * @return true if any errors were captured, false otherwise.
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * Get the list of syntax errors.
     *
     * @return List of SyntaxError objects containing error details.
     */
    public List<SyntaxError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Clear any captured errors (useful for reuse of the listener).
     */
    public void clearErrors() {
        hasErrors = false;
        errors.clear();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        // Mark that errors have been encountered
        hasErrors = true;

        // Create a SyntaxError object and store it
        SyntaxError error = new SyntaxError(line, charPositionInLine, msg, offendingSymbol, e);
        errors.add(error);

        // Log the error at a more appropriate level
        logger.log(Level.SEVERE, "Syntax error at line " + line + ":" + charPositionInLine + " - " + msg);

        // Optional: If you'd like to handle specific types of RecognitionException, you could do so here
        if (e != null) {
            logger.log(Level.SEVERE, "RecognitionException: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

}
