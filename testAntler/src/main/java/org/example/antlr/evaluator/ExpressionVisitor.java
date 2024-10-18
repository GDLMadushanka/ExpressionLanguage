package org.example.antlr.evaluator;

import com.example.antlr.ExpressionParser;
import com.example.antlr.ExpressionParserBaseVisitor;
import com.example.antlr.ExpressionParserVisitor;
import org.example.antlr.ast.*;

import java.util.logging.Logger;

public class ExpressionVisitor extends ExpressionParserBaseVisitor<ExpressionNode>
        implements ExpressionParserVisitor<ExpressionNode> {
    Logger logger = Logger.getLogger(ExpressionVisitor.class.getName());

    @Override
    public ExpressionNode visitExpression(ExpressionParser.ExpressionContext ctx) {
        System.out.println("Visiting expression: " + ctx.getText());
        return ctx.comparisonExpression() != null ? visitComparisonExpression(ctx.comparisonExpression()) : null;
    }

    @Override
    public ExpressionNode visitComparisonExpression(ExpressionParser.ComparisonExpressionContext ctx) {
        if (ctx.logicalExpression() != null) {
            if (ctx.logicalExpression().size() == 1) {
                return visitLogicalExpression(ctx.logicalExpression().get(0));
            } else {
                ExpressionNode left = visit(ctx.logicalExpression().get(0));
                for (int i = 1; i < ctx.logicalExpression().size(); i++) {
                    ExpressionNode right = visit(ctx.logicalExpression(i));
                    left = new BinaryOperationNode(left, ctx.getChild(i).getText(), right);
                }
                return left;
            }
        }
        return null;
    }

    @Override
    public ExpressionNode visitLogicalExpression(ExpressionParser.LogicalExpressionContext ctx) {
        if(ctx.arithmeticExpression()!=null){
            return visitArithmeticExpression(ctx.arithmeticExpression());
        }
        return null;
    }

    @Override public ExpressionNode visitArithmeticExpression(ExpressionParser.ArithmeticExpressionContext ctx)
    { return ctx.term() != null ? visit(ctx.term(0)) : null; }

    @Override
    public ExpressionNode visitTerm(ExpressionParser.TermContext ctx)
    { return ctx.factor() != null ? visit(ctx.factor(0)) : null; }

    @Override
    public ExpressionNode visitFactor(ExpressionParser.FactorContext ctx)
    { return ctx.literal() != null ? visit(ctx.literal()) : null; }

    @Override
    public ExpressionNode visitLiteral(ExpressionParser.LiteralContext ctx)
    {
        if (ctx.NUMBER() != null) {
            return new LiteralNode(ctx.NUMBER().getText(), 0);
        } else if (ctx.BOOLEAN_LITERAL() != null) {
            return new LiteralNode(ctx.BOOLEAN_LITERAL().getText(), 1);
        } else if (ctx.STRING_LITERAL() != null) {
            return new LiteralNode(ctx.STRING_LITERAL().getText(), 2);
        }
        return null;
    }

//
//    @Override
//    public ExpressionNode visitConditionalExpression(ExpressionLanguageParser.ConditionalExpressionContext ctx) {
//        System.out.println("Visiting conditional expression: " + ctx.getText());
//        // Example logic: create a new BinaryOperationNode for the conditional expression
//        ExpressionNode condition = visit(ctx.expression(0));
//        ExpressionNode trueExpr = visit(ctx.expression(1));
//        ExpressionNode falseExpr = visit(ctx.expression(2));
//        return new IfElseConditionNode(condition, trueExpr, falseExpr);
//    }
//
//    @Override
//    public ExpressionNode visitLogicalOrExpression(ExpressionLanguageParser.LogicalOrExpressionContext ctx) {
//        System.out.println("Visiting logical or expression: " + ctx.getText());
//        if (ctx.logicalAndExpression().size() == 1) {
//            return visit(ctx.logicalAndExpression(0));
//        }
//
//        // Start by visiting the first logicalAndExpression
//        ExpressionNode left = visit(ctx.logicalAndExpression(0));
//
//        // Iterate over the remaining logicalAndExpressions and OR operators
//        for (int i = 1; i < ctx.logicalAndExpression().size(); i++) {
//            ExpressionNode right = visit(ctx.logicalAndExpression(i));
//            left = new BinaryOperationNode(left, Constants.OR, right);
//        }
//        return left;
//    }
//
//    @Override
//    public ExpressionNode visitLogicalAndExpression(ExpressionLanguageParser.LogicalAndExpressionContext ctx) {
//        System.out.println("Visiting logical AND expression: " + ctx.getText());
//        if (ctx.equalityExpression().size() == 1) {
//            return visit(ctx.equalityExpression(0));
//        }
//        ExpressionNode left = visit(ctx.equalityExpression(0));
//        for (int i = 1; i < ctx.equalityExpression().size(); i++) {
//            ExpressionNode right = visit(ctx.equalityExpression(i));
//            left = new BinaryOperationNode(left, Constants.AND, right);
//        }
//        return left;
//    }
//
//    @Override
//    public ExpressionNode visitEqualityExpression(ExpressionLanguageParser.EqualityExpressionContext ctx) {
//        System.out.println("Visiting equality expression: " + ctx.getText());
//        if (ctx.relationalExpression().size() == 1) {
//            return visit(ctx.relationalExpression(0));
//        }
//        ExpressionNode left = visit(ctx.relationalExpression(0));
//        for (int i = 1; i < ctx.relationalExpression().size(); i++) {
//            if (ctx.getChild(2 * i - 1) != null) {
//                String operator = ctx.getChild(2 * i - 1).getText();
//                ExpressionNode right = visit(ctx.relationalExpression(i));
//                left = new BinaryOperationNode(left, operator, right);
//            } else {
//                // handle error
//                return null;
//            }
//        }
//        return left;
//    }
//
//    @Override
//    public ExpressionNode visitRelationalExpression(ExpressionLanguageParser.RelationalExpressionContext ctx) {
//        System.out.println("Visiting relational expression: " + ctx.getText());
//        if (ctx.additiveExpression().size() == 1) {
//            return visit(ctx.additiveExpression(0));
//        }
//        ExpressionNode left = visit(ctx.additiveExpression(0));
//        for (int i = 1; i < ctx.additiveExpression().size(); i++) {
//            if (ctx.getChild(2 * i - 1) != null) {
//                String operator = ctx.getChild(2 * i - 1).getText();
//                ExpressionNode right = visit(ctx.additiveExpression(i));
//                left = new BinaryOperationNode(left, operator, right);
//            } else {
//                // handle error
//                return null;
//            }
//        }
//        return left;
//    }
//
//    @Override
//    public ExpressionNode visitAdditiveExpression(ExpressionLanguageParser.AdditiveExpressionContext ctx) {
//        System.out.println("Visiting additive expression: " + ctx.getText());
//        if (ctx.multiplicativeExpression().size() == 1) {
//            return visit(ctx.multiplicativeExpression(0));
//        }
//        ExpressionNode left = visit(ctx.multiplicativeExpression(0));
//        for (int i = 1; i < ctx.multiplicativeExpression().size(); i++) {
//            if (ctx.getChild(2 * i - 1) != null) {
//                String operator = ctx.getChild(2 * i - 1).getText();
//                ExpressionNode right = visit(ctx.multiplicativeExpression(i));
//                left = new BinaryOperationNode(left, operator, right);
//            } else {
//                // handle error
//                return null;
//            }
//        }
//        return left;
//    }
//
//    @Override
//    public ExpressionNode visitMultiplicativeExpression(ExpressionLanguageParser.MultiplicativeExpressionContext ctx) {
//        System.out.println("Visiting equality expression: " + ctx.getText());
//        if (ctx.unaryExpression().size() == 1) {
//            return visit(ctx.unaryExpression(0));
//        }
//        ExpressionNode left = visit(ctx.unaryExpression(0));
//        for (int i = 1; i < ctx.unaryExpression().size(); i++) {
//            if (ctx.getChild(2 * i - 1) != null) {
//                String operator = ctx.getChild(2 * i - 1).getText();
//                ExpressionNode right = visit(ctx.unaryExpression(i));
//                left = new BinaryOperationNode(left, operator, right);
//            } else {
//                // handle error
//                return null;
//            }
//        }
//        return left;
//    }
//
//    @Override
//    public ExpressionNode visitUnaryExpression(ExpressionLanguageParser.UnaryExpressionContext ctx) {
//        System.out.println("Visiting unary expression: " + ctx.getText());
//        if (ctx.NOT() != null) {
//            ExpressionNode right = visit(ctx.unaryExpression());
//            return new UnaryOperationNode(right, true);
//        } else {
//            return visit(ctx.memberExpression());
//        }
//    }
//
//    @Override
//    public ExpressionNode visitMemberExpression(ExpressionLanguageParser.MemberExpressionContext ctx) {
//        System.out.println("Visiting member expression: " + ctx.getText());
//        ExpressionNode node = visit(ctx.primary());
//        for (ExpressionLanguageParser.MemberAccessContext memberAccessCtx : ctx.memberAccess()) {
//            if (memberAccessCtx.DOT() != null) {
//                // Handle property access or method call
//                if (memberAccessCtx.IDENTIFIER() != null) {
//                    // Property access: node.property
////                    String propertyName = memberAccessCtx.IDENTIFIER().getText();
////                    node = new PropertyAccessNode(node, propertyName);
//                } else if (memberAccessCtx.methodCall() != null) {
//                    PredefinedMethodCallNode methodCallNode = (PredefinedMethodCallNode) visit(memberAccessCtx.methodCall());
//                    methodCallNode.setTarget(node);
//                    return methodCallNode;
//                } else {
//                    throw new RuntimeException("Unexpected member access with DOT but no IDENTIFIER or methodCall");
//                }
////            } else if (memberAccessCtx.LBRACKET() != null) {
////                // Handle indexing: node[expression]
////                ExpressionNode indexExpression = visit(memberAccessCtx.expression());
////                node = new IndexAccessNode(node, indexExpression);
////            } else {
////                throw new RuntimeException("Unexpected member access without DOT or LBRACKET");
////            }
//            }
//        }
//        // Step 3: Return the final expression node
//        return node;
//    }
//
//    @Override
//    public ExpressionNode visitMethodCall(ExpressionLanguageParser.MethodCallContext ctx) {
//        System.out.println("Visiting method call : " + ctx.getText());
//        if (ctx.predefinedMethodCall() != null) {
//            return visit(ctx.predefinedMethodCall());
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitPredefinedMethodCall(ExpressionLanguageParser.PredefinedMethodCallContext ctx) {
//        System.out.println("Visiting predefined method call: " + ctx.getText());
//        if (ctx.stringMethodCall() != null) {
//            return visit(ctx.stringMethodCall());
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitStringMethodCall(ExpressionLanguageParser.StringMethodCallContext ctx) {
//        System.out.println("Visiting string method call: " + ctx.getText());
//        ArgumentListNode parameterList = new ArgumentListNode();
//        if (ctx.argumentList() != null) {
//            parameterList = (ArgumentListNode) visit(ctx.argumentList());
//        }
//        PredefinedMethodCallNode methodCallNode = new PredefinedMethodCallNode(parameterList);
//        methodCallNode.setMethodType(0);
//        if (ctx.TO_UPPER() != null) {
//            methodCallNode.setMethodName(Constants.TO_UPPER);
//        } else if (ctx.TO_LOWER() != null) {
//            methodCallNode.setMethodName(Constants.TO_LOWER);
//        } else if (ctx.LENGTH() != null) {
//            methodCallNode.setMethodName(Constants.LENGTH);
//        } else if (ctx.SUBSTRING() != null) {
//            methodCallNode.setMethodName(Constants.SUBSTRING);
//        } else if (ctx.STARTS_WITH() != null){
//            methodCallNode.setMethodName(Constants.STARTS_WITH);
//        } else if (ctx.ENDS_WITH() != null){
//            methodCallNode.setMethodName(Constants.ENDS_WITH);
//        } else if (ctx.CONTAINS() != null){
//            methodCallNode.setMethodName(Constants.CONTAINS);
//        } else if (ctx.TRIM() != null){
//            methodCallNode.setMethodName(Constants.TRIM);
//        } else if (ctx.REPLACE() != null){
//            methodCallNode.setMethodName(Constants.REPLACE);
//        } else if (ctx.SPLIT() != null){
//            methodCallNode.setMethodName(Constants.SPLIT);
//        }
//        return methodCallNode;
//    }
//
//    @Override
//    public ExpressionNode visitPrimary(ExpressionLanguageParser.PrimaryContext ctx) {
//        System.out.println("Visiting primary: " + ctx.getText());
//        if (ctx.literal() != null) {
//            return visit(ctx.literal());
//        } else if (ctx.variableReference() != null) {
//            return visit(ctx.variableReference());
//        } else if (ctx.mathConstant() != null) {
//            return visit(ctx.mathConstant());
//        } else if (ctx.LPAREN() != null) {
//            return visit(ctx.expression());
//        } else if (ctx.functionCall() != null) {
//            return visit(ctx.functionCall());
//        } else {
//            throw new RuntimeException("Unsupported primary expression: " + ctx.getText());
//        }
//    }
//
//    @Override
//    public ExpressionNode visitFunctionCall(ExpressionLanguageParser.FunctionCallContext ctx) {
//        System.out.println("Visiting function call: " + ctx.getText());
//        if (ctx.predefinedFunctionCall() != null) {
//            return visit(ctx.predefinedFunctionCall());
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitPredefinedFunctionCall(ExpressionLanguageParser.PredefinedFunctionCallContext ctx) {
//        System.out.println("Visiting predefined function call: " + ctx.getText());
//        if (ctx.mathFunctionCall() != null) {
//            return visit(ctx.mathFunctionCall());
//        } else if (ctx.conversionFunctionCall() != null) {
//            return visit(ctx.conversionFunctionCall());
//        } else if (ctx.dateFunctionCall() != null) {
//            return visit(ctx.dateFunctionCall());
//        } else if (ctx.typeCheckFunctionCall() != null) {
//            return visit(ctx.typeCheckFunctionCall());
//        } else if (ctx.registryFunctionCall() != null) {
//            return visit(ctx.registryFunctionCall());
//        } else if (ctx.secretFunctionCall() != null) {
//            return visit(ctx.secretFunctionCall());
//        } else if (ctx.encodingFunctionCall() != null) {
//            return visit(ctx.encodingFunctionCall());
//        } else if (ctx.jsonFunctionCall() != null) {
//            return visit(ctx.jsonFunctionCall());
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitMathFunctionCall(ExpressionLanguageParser.MathFunctionCallContext ctx) {
//        System.out.println("Visiting math function call: " + ctx.getText());
//        if (ctx.argumentList() != null) {
//            PredefinedFunctionNode methodCallNode = new PredefinedFunctionNode((ArgumentListNode) visit(ctx.argumentList()));
//            if (ctx.ABS() != null) {
//                methodCallNode.setFunctionName(Constants.ABS);
//            } else if (ctx.CEIL() != null) {
//                methodCallNode.setFunctionName(Constants.CEIL);
//            } else if (ctx.FLOOR() != null) {
//                methodCallNode.setFunctionName(Constants.FLOOR);
//            } else if (ctx.MAX() != null) {
//                methodCallNode.setFunctionName(Constants.MAX);
//            } else if (ctx.MIN() != null) {
//                methodCallNode.setFunctionName(Constants.MIN);
//            } else if (ctx.POW() != null) {
//                methodCallNode.setFunctionName(Constants.POW);
//            } else if (ctx.LOG() != null) {
//                methodCallNode.setFunctionName(Constants.LOGARITHM);
//            } else if (ctx.SQRT() != null) {
//                methodCallNode.setFunctionName(Constants.SQRT);
//            } else if (ctx.SUM() != null) {
//                methodCallNode.setFunctionName(Constants.SUM);
//            } else if (ctx.AVERAGE() != null) {
//                methodCallNode.setFunctionName(Constants.AVG);
//            }
//            return methodCallNode;
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitArgumentList(ExpressionLanguageParser.ArgumentListContext ctx) {
//        System.out.println("Visiting argument list: " + ctx.getText());
//        ArgumentListNode argumentListNode = new ArgumentListNode();
//        for (ExpressionLanguageParser.ExpressionContext context : ctx.expression()) {
//            argumentListNode.addArgument(visit(context));
//        }
//        return argumentListNode;
//    }
//
//    @Override
//    public ExpressionNode visitMathConstant(ExpressionLanguageParser.MathConstantContext ctx) {
//        System.out.println("Visiting math constant: " + ctx.getText());
//        if (!StringUtils.isEmpty(ctx.getText())) {
//            return new ConstantsNode(ctx.getText());
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitLiteral(ExpressionLanguageParser.LiteralContext ctx) {
//        System.out.println("Visiting literal: " + ctx.getText());
//        if (!StringUtils.isEmpty(ctx.getText())) {
//            if (ctx.STRING() != null) {
//                return new LiteralNode(ctx.STRING().getText(), 1);
//            } else if (ctx.NUMBER() != null) {
//                return new LiteralNode(ctx.NUMBER().getText(), 0);
//            } else if (ctx.TRUE() != null) {
//                return new LiteralNode(ctx.TRUE().getText(), 2);
//            } else if (ctx.FALSE() != null) {
//                return new LiteralNode(ctx.FALSE().getText(), 2);
//            } else if (ctx.NULL() != null) {
//                return new LiteralNode(ctx.NULL().getText(), 3);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public ExpressionNode visitMemberAccess(ExpressionLanguageParser.MemberAccessContext ctx) {
//        System.out.println("Visiting member access: " + ctx.getText());
//
//        if (ctx.DOT() != null) {
//            if (ctx.IDENTIFIER() != null) {
//                String propertyName = ctx.IDENTIFIER().getText();
//                return null;
//            } else if (ctx.methodCall() != null) {
//                return visit(ctx.methodCall());
//            } else {
//                throw new RuntimeException("Unexpected member access with DOT but no IDENTIFIER or methodCall");
//            }
//        } else if (ctx.LBRACKET() != null) {
//            ExpressionNode indexExpression = visit(ctx.expression());
//            return null;
//        } else {
//            throw new RuntimeException("Unexpected member access without DOT or LBRACKET");
//        }
//    }
//
//    @Override
//    public ExpressionNode visitArrayLiteral(ExpressionLanguageParser.ArrayLiteralContext ctx) {
//        System.out.println("Visiting array literal: " + ctx.getText());
//        ArrayLiteralNode arrayLiteralNode = new ArrayLiteralNode();
//        for (ExpressionLanguageParser.ExpressionContext context : ctx.expression()) {
//            arrayLiteralNode.addElement(visit(context));
//        }
//        return arrayLiteralNode;
//    }

    @Override
    public ExpressionNode visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
        // Implement the logic to visit children of a rule node
        System.out.println("Visiting children of rule node: " + node.getText());
        return super.visitChildren(node);
    }

    @Override
    public ExpressionNode visitTerminal(org.antlr.v4.runtime.tree.TerminalNode node) {
        // Implement the logic to visit a terminal node
        System.out.println("Visiting terminal: " + node.getText());
        return super.visitTerminal(node);
    }

    @Override
    public ExpressionNode visitErrorNode(org.antlr.v4.runtime.tree.ErrorNode node) {
        // Implement the logic to visit an error node
        System.err.println("Visiting error node: " + node.getText());
        return super.visitErrorNode(node);
    }

    @FunctionalInterface
    public interface MemberAccessOperation {
        ExpressionNode apply(ExpressionNode target);
    }

}

