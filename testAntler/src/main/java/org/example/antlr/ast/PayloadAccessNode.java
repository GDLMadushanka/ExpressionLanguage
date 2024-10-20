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
import org.example.antlr.Utils.ExpressionUtils;
import org.example.antlr.constants.Constants;
import org.example.antlr.context.EvaluationContext;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class PayloadAccessNode implements ExpressionNode {
    private String expression;
    private final Map<String, ExpressionNode> arguments;

    public PayloadAccessNode(String expression, Map<String, ExpressionNode> arguments) {
        this.expression = expression;
        this.arguments = arguments;
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
        if (expression.startsWith(Constants.PAYLOAD)) {
            expression = Constants.PAYLOAD_$ + expression.substring(Constants.PAYLOAD.length());
        }

        for (Map.Entry<String, ExpressionNode> entry : arguments.entrySet()) {
            ExpressionResult result = entry.getValue().evaluate(context);
            if (result != null) {
                String regex = ExpressionUtils.escapeSpecialCharacters(entry.getKey());
                String resultString = result.asString();
                if (result.getType().equals(String.class)) {
                    resultString = "\"" + resultString + "\"";
                }
                if (entry.getValue().getClass().equals(ArrayIndexNode.class)) {
                    // remove quotes from overall expression ex:= "1,4" -> 1,4
                    resultString = resultString.replace("\"", "");
                }
                expression = expression.replaceFirst(regex, resultString);
            }
        }

        Object result = JsonPath.parse(context.getPayload().toString()).read(expression);
        if (result instanceof JsonPrimitive) {
            return new ExpressionResult((JsonPrimitive) result);
        } else if (result instanceof JsonElement) {
            return new ExpressionResult(result.toString());
        }
        return null;
    }

}