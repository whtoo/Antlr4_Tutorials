package org.teachfx.antlr4.ep15.symtab;

import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;


public abstract class MethodScopeSymbol extends Symbol implements Scope {
    Scope enclosingScope;
    public ParserRuleContext tree;

    public MethodScopeSymbol(String name, Type type, Scope enclosingScope) {
        super(name, type);
        this.enclosingScope = enclosingScope;
    }

    public MethodScopeSymbol(String name, Scope enclosingScope, ParserRuleContext tree) {
        super(name);
        this.enclosingScope = enclosingScope;
        this.tree = tree;
    }
    @Override
    public Type lookup(String name) {
        return (Type) resolve(name);
    }

    @Override
    public Symbol resolve(String name) {
        Symbol s = getMemebers().get(name);
        if(s != null) return s;
        if(getEnclosingScope() != null) {
            return getEnclosingScope().resolve(name);
        }

        return null;
    }

    public Symbol resolveType(String name) {
        return resolve(name);
    }

    @Override
    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    @Override
    public String getScopeName() {
        return name;
    }
    @Override
    public void define(Symbol sym) {
        getMemebers().put(sym.name, sym);
        sym.scope = this;
    }
    public abstract Map<String,Symbol> getMemebers();
}
