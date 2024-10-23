package org.example.antlr.Utils;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Charset getCharset(String charsetName) {
        if (charsetName == null) {
            return null;
        }
        return Charset.forName(charsetName);
    }

    /**
     * Extracts the variable name and JSONPath from the input expression.
     *
     * @param input The input string (e.g., var.abc.students[1].name or var["abc"]["students"][1].name).
     * @return A string array where index 0 is the variable name and index 1 is the JSONPath.
     */
    public static String[] extractVariableAndJsonPath(String input) {

        // Regular expression to extract the variable part and the JSONPath part
        // Updated to account for any combination of dot and bracket notation after the variable
//        String regex = "var(?:\\[\"([^\"]+)\"\\]|\\.([a-zA-Z_][a-zA-Z0-9_]*))((?:\\[[^\\]]+\\]|\\.[a-zA-Z_][a-zA-Z0-9_]*|\\['[^']+'\\])*)";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(input);
//
//        if (matcher.find()) {
//            // Get the variable name (either from bracket or dot notation)
//            String variableName = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
//            // Get the JSONPath part (everything after the variable)
//            String jsonPath = matcher.group(3) != null ? matcher.group(3) : "";
//            // Return the variable name and JSONPath
//            return new String[] { variableName, jsonPath };
//        }
        // If no match is found, throw an exception

        if (input.startsWith("var.[\"")) {
            String remaining = input.substring(6);
            int endBracketIndex = remaining.indexOf("\"]");
            String variableName = remaining.substring(0, endBracketIndex);
            String expression = remaining.substring(endBracketIndex + 2);
            return new String[]{variableName, expression};
        } else if (input.startsWith("var[\"")) {
            String remaining = input.substring(5);
            int endBracketIndex = remaining.indexOf("\"]");
            String variableName = remaining.substring(0, endBracketIndex);
            String expression = remaining.substring(endBracketIndex + 2);
            return new String[]{variableName, expression};
        } else if (input.startsWith("var.")) {
            String remaining = input.substring(4);
            int endDotIndex = remaining.indexOf(".");
            int beginArrIndex = remaining.indexOf("[");
            String variableName = "";
            String expression = "";

            if (endDotIndex == -1 && beginArrIndex == -1) {
                variableName = remaining;
            } else if (endDotIndex == -1) {
                variableName = remaining.substring(0, beginArrIndex);
                expression = remaining.substring(beginArrIndex);
            } else if (beginArrIndex == -1) {
                variableName = remaining.substring(0, endDotIndex);
                expression = remaining.substring(endDotIndex);
            } else {
                int minIndex = Math.min(endDotIndex, beginArrIndex);
                variableName = remaining.substring(0, minIndex);
                expression = remaining.substring(minIndex);
            }
            return new String[]{variableName, expression};
        } else {
            throw new IllegalArgumentException("Invalid input format. Could not parse variable and JSONPath.");
        }
    }
}
