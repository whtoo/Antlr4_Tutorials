package org.teachfx.antlr4.ep16.visitor;

import org.teachfx.antlr4.ep16.symtab.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.teachfx.antlr4.ep16.misc.Util;
import org.teachfx.antlr4.ep16.parser.CymbolParser.*;

/*
* @author Arthur.Bltiz
* @description 变量消解-标记每个ast节点的作用域归属问题.
*/
public class LocalDefine extends CymbolASTVisitor<Object> {
    private Scope currentScope = null;
    private ParseTreeProperty<Scope> scopes;

    public ParseTreeProperty<Scope> getScopes() {
        return scopes;
    }
    
    public LocalDefine() {
        BaseScope globalScope = new GlobalScope();
        currentScope = globalScope;
        scopes = new ParseTreeProperty<Scope>();
    }

    @Override
    public Object visitCompilationUnit(CompilationUnitContext ctx) {
        stashScope(ctx);
        super.visitCompilationUnit(ctx);

        return null;
    }

    @Override
    public Object visitFunctionDecl(FunctionDeclContext ctx) {
        MethodSymbol methodScope = new MethodSymbol(Util.name(ctx), currentScope, ctx);
        currentScope.define(methodScope);
        stashScope(ctx);
        pushScope(methodScope);
        super.visitFunctionDecl(ctx);
        popScope();
        System.out.println("enter scope with " + currentScope.getScopeName());
        return null;
    }
   
    
    @Override
    public Object visitFormalParameter(FormalParameterContext ctx) {
        System.out.println(tab + "collect param with "+ctx.getText());
        stashScope(ctx);
        return null;
    }

    @Override
    public Object visitBlock(BlockContext ctx) {
        Scope local = new LocalScope(currentScope);
        stashScope(ctx);
        pushScope(local);
        super.visitBlock(ctx);
        popScope();
        return null;
    }

   
    @Override
    public Object visitStatVarDecl(StatVarDeclContext ctx) {
        System.out.println(tab + "enter stat var declaration " + ctx.getText());
        stashScope(ctx);
        return null;
    }

    @Override
    public Object visitVarDecl(VarDeclContext ctx) {
        System.out.println(tab + "enter var declaration " + ctx.getText());
        stashScope(ctx);
        super.visitVarDecl(ctx);

        return null;
    }
    @Override
    public Object visitType(TypeContext ctx) {
        System.out.println(tab + "enter type " + ctx.getText());
        stashScope(ctx);
        return null;
    }
    @Override
    public Object visitPrimaryFLOAT(PrimaryFLOATContext ctx) {
        System.out.println(tab + "enter float constant");
        stashScope(ctx);
        return null;
    }

    @Override
    public Object visitPrimaryID(PrimaryIDContext ctx) {
        System.out.println(tab + "enter id  "+ctx.getText());

        stashScope(ctx);
        return null;
    }

    @Override
    public Object visitPrimaryINT(PrimaryINTContext ctx) {
        System.out.println(tab + "enter int constant");

        stashScope(ctx);
        return null;
    }

    public void stashScope(ParserRuleContext ctx) {
        scopes.put(ctx,currentScope);
    }

    public void pushScope(Scope scope) {
        currentScope = scope;
    }

    public void popScope() { 
        currentScope = currentScope.getEnclosingScope();
    }
    
}
