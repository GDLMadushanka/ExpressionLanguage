package org.example.antlr.ast;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.example.antlr.Utils.ExpressionUtils;
import org.example.antlr.constants.Constants;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Base64;
import java.util.List;

public class PredefinedFunctionNode implements ExpressionNode {
    private final String functionName;
    private final List<ExpressionNode> arguments;

    public PredefinedFunctionNode(ArgumentListNode arguments, String functionName) {
        this.arguments = arguments.getArguments();
        this.functionName = functionName;
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        if (arguments.size() == 0) {
            if (functionName.equals(Constants.NOW)) {
                return new ExpressionResult(System.currentTimeMillis());
            }
        } else if (arguments.size() == 1) {
            ExpressionResult result = arguments.get(0).evaluate(context);
            switch (functionName) {
                case Constants.LENGTH:
                    if (result.isString()) {
                        return new ExpressionResult(result.asString().length());
                    } else if (result.isArray()) {
                        return new ExpressionResult(result.asJsonElement().getAsJsonArray().size());
                    } else if (result.isNull()) {
                        return new ExpressionResult(0);
                    } else {
                        throw new EvaluationException("Invalid argument type provided for length function");
                    }
                case Constants.TO_LOWER:
                    if (result.isString()) {
                        return new ExpressionResult(result.asString().toLowerCase());
                    } else if (result.isJsonPrimitive()) {
                        return new ExpressionResult(new JsonPrimitive(result.asJsonElement().getAsString().toLowerCase()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for toLower function");
                    }
                case Constants.TO_UPPER:
                    if (result.isString()) {
                        return new ExpressionResult(result.asString().toUpperCase());
                    } else if (result.isJsonPrimitive()) {
                        return new ExpressionResult(new JsonPrimitive(result.asJsonElement().getAsString().toUpperCase()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for toUpper function");
                    }
                case Constants.TRIM:
                    if (result.isString()) {
                        return new ExpressionResult(result.asString().trim());
                    } else {
                        throw new EvaluationException("Invalid argument type provided for trim function");
                    }
                case Constants.ABS:
                    if (result.isInteger()) {
                        return new ExpressionResult(Math.abs(result.asInt()));
                    } else if (result.isDouble()) {
                        return new ExpressionResult(Math.abs(result.asDouble()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for abs function");
                    }
                case Constants.CEIL:
                    if (result.isInteger()) {
                        return new ExpressionResult(result.asInt());
                    } else if (result.isDouble()) {
                        return new ExpressionResult(Math.ceil(result.asDouble()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for ceil function");
                    }
                case Constants.FLOOR:
                    if (result.isInteger()) {
                        return new ExpressionResult(result.asInt());
                    } else if (result.isDouble()) {
                        return new ExpressionResult(Math.floor(result.asDouble()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for floor function");
                    }
                case Constants.SQRT:
                    if (result.isInteger()) {
                        return new ExpressionResult(Math.sqrt(result.asInt()));
                    } else if (result.isDouble()) {
                        return new ExpressionResult(Math.sqrt(result.asDouble()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for sqrt function");
                    }
                case Constants.B64ENCODE:
                    if (result.isString()) {
                        return new ExpressionResult(Base64.getEncoder().encodeToString(result.asString().getBytes()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for base64encode function");
                    }
                case Constants.B64DECODE:
                    if (result.isString()) {
                        return new ExpressionResult(new String(Base64.getDecoder().decode(result.asString())));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for base64decode function");
                    }
                case Constants.URL_ENCODE:
                    if (result.isString()) {
                        return new ExpressionResult(URLEncoder.encode(result.asString(), StandardCharsets.UTF_8));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for urlEncode function");
                    }
                case Constants.URL_DECODE:
                    if (result.isString()) {
                        return new ExpressionResult(java.net.URLDecoder.decode(result.asString(), StandardCharsets.UTF_8));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for urlDecode function");
                    }
                case Constants.IS_STRING:
                    return new ExpressionResult(result.isString());
                case Constants.IS_NUMBER:
                    return new ExpressionResult(result.isInteger() || result.isDouble());
                case Constants.IS_ARRAY:
                    return new ExpressionResult(result.isArray());
                case Constants.IS_OBJECT:
                    return new ExpressionResult(result.isObject());
                case Constants.STRING:
                    return new ExpressionResult(result.asString());
                case Constants.INTEGER:
                    if (result.isInteger()) {
                        return new ExpressionResult(result.asInt());
                    }
                    try {
                        return new ExpressionResult(Integer.parseInt(result.asString()));
                    } catch (NumberFormatException e) {
                        throw new EvaluationException("Conversion to integer failed for : " + result.asString());
                    }
                case Constants.FLOAT:
                    if (result.isDouble()) {
                        return new ExpressionResult(result.asDouble());
                    }
                    try {
                        return new ExpressionResult(Double.parseDouble(result.asString()));
                    } catch (NumberFormatException e) {
                        throw new EvaluationException("Conversion to float failed for : " + result.asString());
                    }
                case Constants.BOOLEAN:
                    if (result.isBoolean()) {
                        return new ExpressionResult(result.asBoolean());
                    }
                    try {
                        return new ExpressionResult(Boolean.parseBoolean(result.asString()));
                    } catch (NumberFormatException e) {
                        throw new EvaluationException("Conversion to boolean failed for: " + result.asString());
                    }
            }
        } else if (arguments.size() == 2) {
            ExpressionResult source = arguments.get(0).evaluate(context);
            ExpressionResult argument1 = arguments.get(1).evaluate(context);
            switch (functionName) {
                case Constants.SUBSTRING:
                    if (source.isString() && argument1.isInteger()) {
                        if (source.asString().length() < argument1.asInt() || argument1.asInt() < 0) {
                            throw new EvaluationException("Invalid index for substring: " + argument1.asInt()
                                    + ", source string length: " + source.asString().length());
                        }
                        return new ExpressionResult(source.asString().substring(argument1.asInt()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for substring function");
                    }
                case Constants.STARTS_WITH:
                    if (source.isString() && argument1.isString()) {
                        return new ExpressionResult(source.asString().startsWith(argument1.asString()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for startsWith function");
                    }
                case Constants.ENDS_WITH:
                    if (source.isString() && argument1.isString()) {
                        return new ExpressionResult(source.asString().endsWith(argument1.asString()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for endsWith function");
                    }
                case Constants.CONTAINS:
                    if (source.isString() && argument1.isString()) {
                        return new ExpressionResult(source.asString().contains(argument1.asString()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for contains function");
                    }
                case Constants.SPLIT:
                    if (source.isString() && argument1.isString()) {
                        String[] splits = source.asString().split(argument1.asString());
                        JsonArray jsonArray = new JsonArray();
                        for (String split : splits) {
                            jsonArray.add(split);
                        }
                        return new ExpressionResult(jsonArray);
                    } else {
                        throw new EvaluationException("Invalid argument type provided for split function");
                    }
                case Constants.POW:
                    if ((source.isDouble() || source.isInteger()) && (argument1.isDouble() || argument1.isInteger())) {
                        return new ExpressionResult(Math.pow(source.asDouble(), argument1.asDouble()));
                    } else {
                        throw new EvaluationException("Invalid argument type provided for pow function");
                    }
                case Constants.B64ENCODE:
                    if (source.isString() && argument1.isString()) {
                        try {
                            return new ExpressionResult(Base64.getEncoder().encodeToString(
                                    source.asString().getBytes(ExpressionUtils.getCharset(argument1.asString()))));
                        } catch (UnsupportedCharsetException e) {
                            throw new EvaluationException("Invalid charset provided for urlEncode function");
                        }
                    } else {
                        throw new EvaluationException("Invalid argument type provided for base64encode function");
                    }
                case Constants.URL_ENCODE:
                    if (source.isString() && argument1.isString()) {
                        try {
                            return new ExpressionResult(URLEncoder.encode(source.asString(),
                                    ExpressionUtils.getCharset(argument1.asString())));
                        } catch (UnsupportedCharsetException e) {
                            throw new EvaluationException("Invalid charset provided for urlEncode function");
                        }
                    } else {
                        throw new EvaluationException("Invalid argument type provided for urlEncode function");
                    }
            }
        } else if (arguments.size() == 3) {
            ExpressionResult source = arguments.get(0).evaluate(context);
            ExpressionResult argument1 = arguments.get(1).evaluate(context);
            ExpressionResult argument2 = arguments.get(2).evaluate(context);
            if (functionName.equals(Constants.SUBSTRING)) {
                if (source.isString() && argument1.isInteger() && argument2.isInteger()) {
                    if (argument2.asInt() < 0 || argument1.asInt() < 0 || argument1.asInt() > argument2.asInt()
                            || argument2.asInt() > source.asString().length()) {
                        throw new EvaluationException("Invalid substring indices: start=" + argument1.asInt()
                                + ", end=" + argument2.asInt() + ", string length=" + source.asString().length());
                    }
                    return new ExpressionResult(source.asString().substring(argument1.asInt(), argument2.asInt()));
                } else {
                    throw new EvaluationException("Invalid argument type provided for substring function");
                }
            } else if (functionName.equals(Constants.REPLACE)) {
                if (source.isString() && argument1.isString() && argument2.isString()) {
                    return new ExpressionResult(source.asString().replace(argument1.asString(), argument2.asString()));
                } else {
                    throw new EvaluationException("Invalid argument type provided for replace function");
                }
            }
        }
        throw new EvaluationException("Invalid no of arguments: " + arguments.size() + " provided for the function: "
                + functionName);
    }
}
