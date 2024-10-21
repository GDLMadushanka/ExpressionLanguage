package org.example.antlr.ast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.apache.commons.lang3.StringUtils;
import org.example.antlr.Utils.ExpressionUtils;
import org.example.antlr.constants.Constants;
import org.example.antlr.context.EvaluationContext;

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
                    if (result.getType().equals(String.class) &&
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
            result = JsonPath.parse(context.getPayload().toString()).read(expression);
        } else {
            String variableName;
            String varExpression = null;
            // remove var. prefix
            String withoutPrefix = expression.substring(4);
            // Check if there is a dot to separate the variable name from the expression
            int firstDotIndex = withoutPrefix.indexOf('.');
            if (firstDotIndex == -1) {
                variableName = withoutPrefix;
            } else {
                variableName = withoutPrefix.substring(0, firstDotIndex);
                varExpression = withoutPrefix.substring(firstDotIndex + 1);
            }
            Object variable = context.getVariable(variableName);
            if (StringUtils.isEmpty(expression)) {
                return new ExpressionResult(variable.toString());
            } else {
                result = JsonPath.parse(variable).read("$." + varExpression);
            }
        }
        if (result instanceof JsonPrimitive) {
            return new ExpressionResult((JsonPrimitive) result);
        } else if (result instanceof JsonElement) {
            return new ExpressionResult(result.toString());
        }
        return null;
    }

}