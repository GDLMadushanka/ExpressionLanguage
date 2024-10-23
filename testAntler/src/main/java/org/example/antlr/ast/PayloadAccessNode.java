package org.example.antlr.ast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.apache.commons.lang3.StringUtils;
import org.example.antlr.Utils.ExpressionUtils;
import org.example.antlr.constants.Constants;
import org.example.antlr.context.EvaluationContext;
import org.example.antlr.exception.EvaluationException;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class PayloadAccessNode implements ExpressionNode {
    private String expression;
    private final Map<String, ExpressionNode> arguments;

    private boolean isVariable = false;

    public PayloadAccessNode(String expression, Map<String, ExpressionNode> arguments, boolean isVariable) {
        this.expression = expression;
        this.arguments = arguments;
        this.isVariable = isVariable;
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new GsonJsonProvider(new GsonBuilder().serializeNulls().create());
            private final MappingProvider mappingProvider = new GsonMappingProvider();

            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    @Override
    public ExpressionResult evaluate(EvaluationContext context) {
        if (expression.startsWith(Constants.PAYLOAD) && !isVariable) {
            expression = Constants.PAYLOAD_$ + expression.substring(Constants.PAYLOAD.length());
        }

        for (Map.Entry<String, ExpressionNode> entry : arguments.entrySet()) {
            if (entry.getValue() != null) {
                ExpressionResult result = entry.getValue().evaluate(context);
                if (result != null) {
                    String regex = ExpressionUtils.escapeSpecialCharacters(entry.getKey());
                    String resultString = result.asString();
                    if (result.isString() &&
                            !entry.getValue().getClass().equals(FilterExpressionNode.class)) {
                        resultString = "\"" + resultString + "\"";
                    }
                    if (entry.getValue().getClass().equals(ArrayIndexNode.class)) {
                        // remove quotes from overall expression ex:= "1,4" -> 1,4
                        resultString = resultString.replace("\"", "");
                    }
                    expression = expression.replaceFirst(regex, resultString);
                }
            }
        }

        Object result;
        if (!isVariable) {
            try {
                result = JsonPath.parse(context.getPayload().toString()).read(expression);
            } catch (PathNotFoundException e) {
                throw new EvaluationException("Evaluating expression: " + expression
                        + " failed. Path not found in payload");
            }
        } else {
            String[] variableAndExpression = ExpressionUtils.extractVariableAndJsonPath(expression);
            Object variable = context.getVariable(variableAndExpression[0]);
            if (variable == null) {
                throw new EvaluationException("Variable " + variableAndExpression[0] + " is not defined");
            }
            String expressionToEvaluate = variableAndExpression[1];
            if (StringUtils.isEmpty(expressionToEvaluate)) {
                JsonElement jsonElement = JsonParser.parseString(variable.toString());
                return new ExpressionResult(jsonElement);
            } else {
                expressionToEvaluate = expressionToEvaluate.startsWith(".") ? "$" + expressionToEvaluate
                        : "$." + expressionToEvaluate;
                try {
                    result = JsonPath.parse(variable.toString()).read(expressionToEvaluate);
                } catch (PathNotFoundException e) {
                    throw new EvaluationException("Evaluating expression: " + expression
                            + " failed. Path not found in variable");
                }
            }
        }
        if (result instanceof JsonPrimitive) {
            return new ExpressionResult((JsonPrimitive) result);
        } else if (result instanceof JsonElement) {
            return new ExpressionResult((JsonElement) result);
        }
        return null;
    }

}