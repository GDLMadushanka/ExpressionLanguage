package org.example.antlr.evaluator;

import com.example.antlr.ExpressionParser;
import com.example.antlr.ExpressionParserBaseVisitor;
import com.example.antlr.ExpressionParserVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.antlr.ast.*;
import org.example.antlr.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
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
                    left = new BinaryOperationNode(left, ctx.getChild(2 * i - 1).getText(), right);
                }
                return left;
            }
        }
        return null;
    }

    @Override
    public ExpressionNode visitLogicalExpression(ExpressionParser.LogicalExpressionContext ctx) {
        if (ctx.arithmeticExpression() != null) {
            ExpressionNode left = visit(ctx.arithmeticExpression());
            if (ctx.logicalExpression() != null && ctx.getChild(1) != null) {
                ExpressionNode right = visit(ctx.logicalExpression());
                left = new BinaryOperationNode(left, ctx.getChild(1).getText(), right);
            }
            return left;
        }
        return null;
    }

    @Override
    public ExpressionNode visitArithmeticExpression(ExpressionParser.ArithmeticExpressionContext ctx) {
        if (ctx.term() != null) {
            if (ctx.term().size() == 1) {
                return visit(ctx.term().get(0));
            } else {
                ExpressionNode left = visit(ctx.term().get(0));
                for (int i = 1; i < ctx.term().size(); i++) {
                    ExpressionNode right = visit(ctx.term(i));
                    left = new BinaryOperationNode(left, ctx.getChild(2 * i - 1).getText(), right);
                }
                return left;
            }
        }
        return null;
    }

    @Override
    public ExpressionNode visitTerm(ExpressionParser.TermContext ctx) {
        if (ctx.factor() != null) {
            if (ctx.factor().size() == 1) {
                return visit(ctx.factor().get(0));
            } else {
                ExpressionNode left = visit(ctx.factor().get(0));
                for (int i = 1; i < ctx.factor().size(); i++) {
                    ExpressionNode right = visit(ctx.factor(i));
                    left = new BinaryOperationNode(left, ctx.getChild(2 * i - 1).getText(), right);
                }
                return left;
            }
        }
        return null;
    }

    @Override
    public ExpressionNode visitFactor(ExpressionParser.FactorContext ctx) {
        if (ctx.literal() != null) {
            return visit(ctx.literal());
        } else if (ctx.functionCall() != null) {
            return visit(ctx.functionCall());
        } else if (ctx.expression() != null) {
            return visit(ctx.expression());
        } else if (ctx.payloadAccess() != null) {
            return visit(ctx.payloadAccess());
        } else if (ctx.variableAccess() != null) {
            return visit(ctx.variableAccess());
        }
        return null;
    }

    @Override
    public ExpressionNode visitFunctionCall(ExpressionParser.FunctionCallContext ctx) {
        ArgumentListNode parameterList = new ArgumentListNode();
        if (ctx.expression() != null) {
            for (ExpressionParser.ExpressionContext expressionContext : ctx.expression()) {
                parameterList.addArgument(visit(expressionContext));
            }
        }
        if (ctx.LENGTH() != null) {
            return new PredefinedFunctionNode(parameterList, Constants.LENGTH);
        } else if (ctx.TOUPPER() != null) {
            return new PredefinedFunctionNode(parameterList, Constants.TO_UPPER);
        } else if (ctx.TOLOWER() != null) {
            return new PredefinedFunctionNode(parameterList, Constants.TO_LOWER);
        }
        return null;
    }

    @Override
    public ExpressionNode visitLiteral(ExpressionParser.LiteralContext ctx) {
        if (ctx.NUMBER() != null) {
            return new LiteralNode(ctx.NUMBER().getText(), 0);
        } else if (ctx.BOOLEAN_LITERAL() != null) {
            return new LiteralNode(ctx.BOOLEAN_LITERAL().getText(), 2);
        } else if (ctx.STRING_LITERAL() != null) {
            return new LiteralNode(ctx.STRING_LITERAL().getText(), 1);
        } else if (ctx.arrayLiteral() != null) {
            return visit(ctx.arrayLiteral());
        }
        return null;
    }

    @Override
    public ExpressionNode visitArrayLiteral(ExpressionParser.ArrayLiteralContext ctx) {
        if (ctx.expression() != null) {
            ArgumentListNode parameterList = new ArgumentListNode();
            for (ExpressionParser.ExpressionContext expressionContext : ctx.expression()) {
                parameterList.addArgument(visit(expressionContext));
            }
            return new LiteralNode(parameterList, 4);
        }
        return null;
    }

    @Override
    public ExpressionNode visitVariableAccess(ExpressionParser.VariableAccessContext ctx) {
        Map<String, ExpressionNode> expressionNodeMap = new HashMap<>();
        if (ctx.arrayIndex() != null) {
            for (ExpressionParser.ArrayIndexContext expressionContext : ctx.arrayIndex()) {
                expressionNodeMap.put(expressionContext.getText(), visit(expressionContext));
            }
        }
        return new PayloadAccessNode(ctx.getText(), expressionNodeMap,true);
    }

    @Override
    public ExpressionNode visitPayloadAccess(ExpressionParser.PayloadAccessContext ctx) {
        Map<String, ExpressionNode> expressionNodeMap = new HashMap<>();
        if (ctx.arrayIndex() != null) {
            for (ExpressionParser.ArrayIndexContext expressionContext : ctx.arrayIndex()) {
                expressionNodeMap.put(expressionContext.getText(), visit(expressionContext));
            }
        }
        return new PayloadAccessNode(ctx.getText(), expressionNodeMap,false);
    }

    @Override
    public ExpressionNode visitArrayIndex(ExpressionParser.ArrayIndexContext ctx) {
        if (ctx.NUMBER() != null) {
            return new LiteralNode(ctx.NUMBER().getText(), 0);
        } else if (ctx.STRING_LITERAL() != null) {
            return new LiteralNode(ctx.STRING_LITERAL().getText(), 1);
        } else if (ctx.expression() != null) {
            if (ctx.expression().size() == 1) {
                return visit(ctx.expression(0));
            }
        } else if (ctx.ASTERISK() != null) {
            return null;
        }
        return visitChildren(ctx);
    }

    @Override
    public ExpressionNode visitMultipleArrayIndices(ExpressionParser.MultipleArrayIndicesContext ctx) {
        if (ctx.expression() != null) {
            ArgumentListNode expressionNodes = new ArgumentListNode();
            for (ExpressionParser.ExpressionContext expressionContext : ctx.expression()) {
                expressionNodes.addArgument(visit(expressionContext));
            }
            return new ArrayIndexNode(expressionNodes, ',');
        }
        return null;
    }

    @Override
    public ExpressionNode visitSliceArrayIndex(ExpressionParser.SliceArrayIndexContext ctx) {
        if (ctx.signedExpressions() != null) {
            ArgumentListNode expressionNodes = new ArgumentListNode();
            if (ctx.getChildCount() == 2 && ctx.getChild(0).getText().equals(":")) {
                expressionNodes.addArgument(null);
            }
            for (ExpressionParser.SignedExpressionsContext expressionContext : ctx.signedExpressions()) {
                expressionNodes.addArgument(visit(expressionContext));
            }
            if (ctx.getChildCount() == 2 && ctx.getChild(1).getText().equals(":")) {
                expressionNodes.addArgument(null);
            }
            return new ArrayIndexNode(expressionNodes, ':');
        }
        return null;
    }

    @Override
    public ExpressionNode visitSignedExpressions(ExpressionParser.SignedExpressionsContext ctx) {
        if (ctx.expression() != null) {
            if (ctx.MINUS() != null) {
                return new SignedExpressionNode(visit(ctx.expression()), true);
            } else {
                return visit(ctx.expression());
            }
        }
        return null;
    }

    @Override
    public ExpressionNode visitFilterExpression(ExpressionParser.FilterExpressionContext ctx) {
        Map<String, ExpressionNode> expressionNodeMap = new HashMap<>();
        if (ctx.filterComponent() != null) {
            for (ExpressionParser.FilterComponentContext filterExpressionContext : ctx.filterComponent()) {
                expressionNodeMap.put(filterExpressionContext.getText(), visit(filterExpressionContext));
            }
        }
        return new FilterExpressionNode(ctx.getText(), expressionNodeMap);
    }

    @Override
    public ExpressionNode visitFilterComponent(ExpressionParser.FilterComponentContext ctx) {
        if (ctx.payloadAccess() != null) {
            return visit(ctx.payloadAccess());
        } else if (ctx.stringOrOperator() != null) {
            return null;
        } else if (ctx.variableAccess() != null) {
            return visit(ctx.variableAccess());
        }
        return null;
    }

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

