package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import org.example.antlr.exception.EvaluationException;

/**
 * This class represents the result of an expression evaluation.
 */
public class ExpressionResult {
    private final Object value;

    // Constructor for String
    public ExpressionResult(String value) {
        this.value = value;
    }

    public ExpressionResult(Number value) {
        this.value = value;
    }

    // Constructor for int
    public ExpressionResult(int value) {
        this.value = value;
    }

    // Constructor for double
    public ExpressionResult(double value) {
        this.value = value;
    }

    // Constructor for boolean
    public ExpressionResult(boolean value) {
        this.value = value;
    }

    // Constructor for JsonElement
    public ExpressionResult(JsonElement value) {
        this.value = value;
    }

    // Constructor for null
    public ExpressionResult() {
        this.value = null;
    }

    // Method to check if the result is null
    public boolean isNull() {
        return value == null || (value instanceof JsonElement && value.equals(JsonNull.INSTANCE));
    }

    // Method to get value as String
    public String asString() {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            // if quoted, remove quotes
            if (((String) value).startsWith("\"") && ((String) value).endsWith("\"")) {
                return ((String) value).substring(1, ((String) value).length() - 1);
            } else if (((String) value).startsWith("'") && ((String) value).endsWith("'")) {
                return ((String) value).substring(1, ((String) value).length() - 1);
            }
            return (String) value;
        } else if (value instanceof JsonPrimitive && ((JsonPrimitive) value).isString()) {
            return ((JsonPrimitive) value).getAsString();
        }
        return value.toString(); // Fallback to toString() for other types
    }

    // Method to get value as int
    public int asInt() {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof JsonPrimitive && ((JsonPrimitive) value).isNumber()) {
            return ((JsonPrimitive) value).getAsInt();
        }
        throw new EvaluationException("Value : " + value + " cannot be converted to int");
    }

    // Method to get value as double
    public double asDouble() {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof JsonPrimitive && ((JsonPrimitive) value).isNumber()) {
            return ((JsonPrimitive) value).getAsDouble();
        }
        throw new EvaluationException("Value : " + value + " cannot be converted to double");
    }

    // Method to get value as boolean
    public boolean asBoolean() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof JsonPrimitive && ((JsonPrimitive) value).isBoolean()) {
            return ((JsonPrimitive) value).getAsBoolean();
        }
        throw new EvaluationException("Value : " + value + " cannot be converted to boolean");
    }

    // Method to get value as JsonElement
    public JsonElement asJsonElement() {
        if (value instanceof JsonElement) {
            return (JsonElement) value;
        }
        throw new EvaluationException("Value is not a JsonElement");
    }

    // Method to check the actual type of the result
    public Class<?> getType() {
        if (value == null) {
            return null;
        }
        return value.getClass();
    }

    public boolean isInteger() {
        return value instanceof Integer || (value instanceof JsonPrimitive && isInteger((JsonPrimitive) value));
    }

    public boolean isDouble() {
        return value instanceof Double || (value instanceof JsonPrimitive && isDouble((JsonPrimitive) value));
    }

    public boolean isBoolean() {
        return value instanceof Boolean || (value instanceof JsonPrimitive && ((JsonPrimitive) value).isBoolean());
    }

    public boolean isString() {
        return value instanceof String || (value instanceof JsonPrimitive && ((JsonPrimitive) value).isString());
    }

    public boolean isObject() {
        return value instanceof JsonElement && ((JsonElement) value).isJsonObject();
    }

    public boolean isArray() {
        return value instanceof JsonElement && ((JsonElement) value).isJsonArray();
    }

    public boolean isJsonPrimitive() {
        return value instanceof JsonPrimitive;
    }

    private boolean isInteger(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isNumber()) {
            Number number = jsonPrimitive.getAsNumber();
            // Check if the number is an instance of integer types (int, long, short)
            boolean initialCheck = number instanceof Integer || number instanceof Long || number instanceof Short;
            if (!initialCheck && number instanceof LazilyParsedNumber) {
                // Check if the number is an instance of integer types (int, long, short)
                String numberString = number.toString();
                try {
                    Integer.parseInt(numberString);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return initialCheck;
        }
        return false; // Not a number, so it's not an integer
    }

    private boolean isDouble(JsonPrimitive jsonPrimitive) {
        if (jsonPrimitive.isNumber()) {
            Number number = jsonPrimitive.getAsNumber();
            // Check if the number is an instance of floating-point types (float, double)
            boolean initialCheck = number instanceof Float || number instanceof Double;
            if (!initialCheck && number instanceof LazilyParsedNumber) {
                // Check if the number is an instance of integer types (int, long, short)
                String numberString = number.toString();
                try {
                    Double.parseDouble(numberString);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false; // Not a number, so it's not a double
    }
}
