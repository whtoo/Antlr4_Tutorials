package org.teachfx.antlr4.ep14.cymbol.lang.symtab;

public class Symbol {
    String name;
    public Type type;
    public Scope scope;

    public Symbol(String name) {
        this.name = name;
        this.type = SymbolTable.UNDEFINED;
    }
    public Symbol(String name,Type type) {
        this(name);
        this.type = type != null ? type : SymbolTable.UNDEFINED;
    }  
    
    public String getName() {
        return name;
    }

    public String toString() {
        String s = "";
        if(scope != null) s = scope.getScopeName() + ".";
        if(type != null) return '<' + s + getName() + ":" + type + ">";
        return s + getName();
    }

    public static String stripBrackets(String s) {
        return s.substring(1,s.length() - 1);
    }
}
