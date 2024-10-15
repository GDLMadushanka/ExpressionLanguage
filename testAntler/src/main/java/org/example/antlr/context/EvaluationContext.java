package org.example.antlr.context;

import java.util.*;
import java.util.stream.Collectors;

public class EvaluationContext {

    private Map<String, Object> variables;
    private Object payload;
    private Map<String, Function> functions;

    public EvaluationContext() {
        variables = new HashMap<>();
        functions = new HashMap<>();
        registerBuiltInFunctions();
    }

    // Variable methods
    public Object getVariable(String name) {
        Object value = variables.get(name);
        return value;
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    // Payload methods
    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getPayloadValue(List<String> propertyPath) {
        Object current = payload;
        for (String prop : propertyPath) {
            if (current instanceof Map) {
                current = ((Map) current).get(prop);
            } else if (current instanceof List) {
                int index = Integer.parseInt(prop);
                current = ((List) current).get(index);
            } else {
                return null;
            }
        }
        return current;
    }

    // Function methods
    public void registerFunction(String name, Function function) {
        functions.put(name, function);
    }



    private void registerBuiltInFunctions() {
        // String Functions
        registerFunction("length", args -> ((String) args.get(0)).length());
        registerFunction("contains", args -> ((String) args.get(0)).contains((String) args.get(1)));
        registerFunction("startsWith", args -> ((String) args.get(0)).startsWith((String) args.get(1)));
        registerFunction("endsWith", args -> ((String) args.get(0)).endsWith((String) args.get(1)));
        registerFunction("substring", args -> {
            String str = (String) args.get(0);
            int start = ((Number) args.get(1)).intValue();
            int end = ((Number) args.get(2)).intValue();
            return str.substring(start, end);
        });
        registerFunction("toUpper", args -> ((String) args.get(0)).toUpperCase());
        registerFunction("toLower", args -> ((String) args.get(0)).toLowerCase());
        registerFunction("trim", args -> ((String) args.get(0)).trim());

        // Numeric Functions
        registerFunction("abs", args -> Math.abs(((Number) args.get(0)).doubleValue()));
        registerFunction("ceil", args -> Math.ceil(((Number) args.get(0)).doubleValue()));
        registerFunction("floor", args -> Math.floor(((Number) args.get(0)).doubleValue()));
        registerFunction("round", args -> Math.round(((Number) args.get(0)).doubleValue()));
        registerFunction("max", args -> Collections.max(args.stream()
                .map(a -> ((Number) a).doubleValue())
                .collect(Collectors.toList())));
        registerFunction("min", args -> Collections.min(args.stream()
                .map(a -> ((Number) a).doubleValue())
                .collect(Collectors.toList())));

        // Date Functions
        registerFunction("now", args -> new Date());
        // Implement dateFormat as needed

        // Logical Functions
        registerFunction("not", args -> !((Boolean) args.get(0)));
        registerFunction("boolean", args -> {
            Object val = args.get(0);
            if (val instanceof Boolean) return val;
            if (val instanceof Number) return ((Number) val).doubleValue() != 0;
            if (val instanceof String) return !((String) val).isEmpty();
            return val != null;
        });
    }

    @FunctionalInterface
    public interface Function {
        Object invoke(List<Object> args);
    }
}

