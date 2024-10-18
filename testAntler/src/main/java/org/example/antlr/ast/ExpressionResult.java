package org.example.antlr.ast;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

/**
 * This class represents the result of an expression evaluation.
 */
public class ExpressionResult {
    private Object value;

    // Constructor for String
    public ExpressionResult(String value) {
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
        throw new IllegalStateException("Value cannot be converted to int");
    }

    // Method to get value as double
    public double asDouble() {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof JsonPrimitive && ((JsonPrimitive) value).isNumber()) {
            return ((JsonPrimitive) value).getAsDouble();
        }
        throw new IllegalStateException("Value cannot be converted to double");
    }

    // Method to get value as boolean
    public boolean asBoolean() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof JsonPrimitive && ((JsonPrimitive) value).isBoolean()) {
            return ((JsonPrimitive) value).getAsBoolean();
        }
        throw new IllegalStateException("Value cannot be converted to boolean");
    }

    // Method to get value as JsonElement
    public JsonElement asJsonElement() {
        if (value instanceof JsonElement) {
            return (JsonElement) value;
        }
        throw new IllegalStateException("Value is not a JsonElement");
    }

    // Method to check the actual type of the result
    public Class<?> getType() {
        if (value == null) {
            return null;
        }
        return value.getClass();
    }
}
