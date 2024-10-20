package org.example.antlr.Utils;

public class ExpressionUtils {
    public static String escapeSpecialCharacters(String input) {
        StringBuilder escapedString = new StringBuilder();
        for (char c : input.toCharArray()) {
            if ("\\.^$|?*+()[]{}".indexOf(c) != -1) {
                escapedString.append('\\');
            }
            escapedString.append(c);
        }
        return escapedString.toString();
    }
}
