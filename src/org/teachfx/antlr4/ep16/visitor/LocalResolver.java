package org.teachfx.antlr4.ep16.visitor;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.teachfx.antlr4.ep16.parser.CymbolBaseVisitor;
import org.teachfx.antlr4.ep16.parser.CymbolParser.*;
import org.teachfx.antlr4.ep16.symtab.*;
import org.teachfx.antlr4.ep16.misc.*;

public class LocalResolver extends CymbolBaseVisitor<Object> {
    
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int ARRAY_EXPR = 0;
    private static final int FUNC_EXPR = 0;
    private static final int STRUCT = 0;
    private static final int MEMBER_PARENT = 2;
    private static final int MEMBER = 0;
    
    private ScopeUtil scopes;
    public ParseTreeProperty<Type> types;

    public LocalResolver(ScopeUtil scopes) {
        this.scopes = scopes;
        this.types = new ParseTreeProperty<Type>();
    }


    @Override
    public Object visitVarDecl(VarDeclContext ctx) {
        System.out.println(ctx.getClass().toString());
        Type type = scopes.lookup(ctx.type());
        System.out.println(ctx.getText());

        VariableSymbol var = new VariableSymbol(Util.name(ctx), type);
      
        if(type == null) { System.out.println(ctx + "Unknown type when declaring variable: " + var); }
        Scope scope = scopes.get(ctx);
        scope.define(var);
        return null;
    }

    @Override
    public Object visitFormalParameter(FormalParameterContext ctx) {
        Type type = scopes.lookup(ctx.type());
        VariableSymbol var = new VariableSymbol(Util.name(ctx), type);
        Scope scope = scopes.get(ctx);
        scope.define(var);
        return null;
    }
    
    @Override
    public Object visitFunctionDecl(FunctionDeclContext ctx){
        Symbol method = scopes.resolve(ctx);
        String returnType = ctx.type().getStart().getText();
        method.type = method.scope.lookup(returnType);
        return null;
    }

    // @Override
    // public void exitExpr_Call(Expr_CallContext ctx) {
    //     copyType(ctx.expr(FUNC_EXPR), ctx);
    // }

    // @Override
    // public void exitExpr_Array(Expr_ArrayContext ctx) {
    //     copyType(ctx.expr(ARRAY_EXPR), ctx);
    // }

    // @Override
    // public void exitExpr_Group(Expr_GroupContext ctx) {
    //     copyType(ctx.expr(), ctx);
    // }

    // @Override
    // public void visitTerminal(TerminalNode node) {
    //     if(node.getSymbol().getText().equals(".")) {
    //         ParserRuleContext parent = (ParserRuleContext) node.getParent();
    //         StructSymbol struct = (StructSymbol) types.get(parent.getChild(STRUCT));
    //         ParserRuleContext member = (ParserRuleContext) parent.getChild(MEMBER_PARENT).getChild(MEMBER);
    //         String name = member.start.getText();
    //         Type memberType = struct.resolveMember(name).type;
    //         stashType(member, memberType);
    //     }
    // }

    // @Override
    // public void exitExpr_Member(Expr_MemberContext ctx) {
    //     copyType(ctx.expr(RIGHT), ctx);
    // }

    // @Override
    // public void exitExpr_Binary(Expr_BinaryContext ctx) {
    //     copyType(ctx.expr(LEFT), ctx);
    // }

    // @Override
    // public void exitExpr_Unary(Expr_UnaryContext ctx) {
    //     copyType(ctx.expr(), ctx);
    // }

    // @Override
    // public void exitExpr_Primary(Expr_PrimaryContext ctx) {
    //     copyType(ctx.primary(), ctx);
    // }
    
    // @Override
    // public void enterPrimitiveType(PrimitiveTypeContext ctx) {
    //     setType(ctx);
    // }

    // @Override
    // public void enterPrim_Int(Prim_IntContext ctx) {
    //     setType(ctx);
    // }


    // @Override
    // public void enterPrim_String(Prim_StringContext ctx) {
    //     setType(ctx);
    // }
    
    // @Override
    // public void enterPrim_Id(Prim_IdContext ctx) {
    //     setType(ctx);
    // }



    // @Override
    // public void enterPrim(PrimContext ctx) {
    //     setType(ctx);
    // }

    // private void setType(ParserRuleContext ctx) {
    //     // already defined type as in the case of struct members
    //     if(types.get(ctx) != null) { return; }
        
    //     int tokenValue = ctx.start.getType();
    //     String tokenName = ctx.start.getText();
    //     if(tokenValue == CymbolParser.ID) {
    //         Scope scope = scopes.get(ctx);
    //         Symbol s = scope.resolve(tokenName);
            
    //         if(s == null) { compiler.reportError(ctx, "Unknown type for id: " + tokenName); }
    //         else { stashType(ctx, s.type); }
            
    //     } else if (tokenValue == CymbolParser.INT || 
    //                tokenName.equals("int")) {
    //         stashType(ctx, SymbolTable.INT);   
    //     } else if (tokenValue == CymbolParser.FLOAT ||
    //                tokenName.equals("float")) {
    //         stashType(ctx, SymbolTable.FLOAT);            
    //     } else if (tokenValue == CymbolParser.CHAR ||
    //                tokenName.equals("char")) {
    //         stashType(ctx, SymbolTable.CHAR);
    //     } else if (tokenName.equals("true") ||
    //                tokenName.equals("false")||
    //                tokenName.equals("boolean")) {
    //         stashType(ctx, SymbolTable.BOOLEAN);            
    //     } else if (tokenName.equals("void")) {
    //         stashType(ctx, SymbolTable.VOID);
    //     } else if (tokenName.equals("null")) {
    //         stashType(ctx, SymbolTable.NULL);
    //     }
    // }

    private void stashType(ParserRuleContext ctx, Type type) {
        types.put(ctx, type);
    }
    
    private void copyType(ParserRuleContext from, ParserRuleContext to) {
        Type type = types.get(from);
        types.put(to, type);
    }
    
}