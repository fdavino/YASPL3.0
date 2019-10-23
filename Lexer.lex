
package lexer;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java.io.IOException;
import java.io.InputStreamReader;

import parser.LexerSym;
import static parser.LexerSym.*;

import exception.*;

%%

%class LexerLex

%unicode
%line
%column

%public

%cup


%{

	private StringBuffer sb;
    private ComplexSymbolFactory symbolFactory;

	public LexerLex(ComplexSymbolFactory sf, java.io.InputStream is){
		this(new InputStreamReader(is));
		sb = new StringBuffer();
        symbolFactory = sf;
    }
    
	private Symbol symbol(String name, int code) {
		return (ComplexSymbol)symbolFactory.newSymbol(name, code,
						new Location(yyline+1,yycolumn+1 - yylength()),
						new Location(yyline+1,yycolumn+1));
	}
	
    private Symbol symbol(String name, int code, Object value) {
        return (ComplexSymbol) symbolFactory.newSymbol(name, code,
    					new Location(yyline+1, yycolumn+1),
    					new Location(yyline+1, yycolumn+yylength()),
    					value);
    }

%}

%eofval{
    return symbolFactory.newSymbol("EOF", LexerSym.EOF,
						new Location(yyline+1,yycolumn+1 - yylength()),
						new Location(yyline+1,yycolumn+1));
%eofval}

id 			= ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*
digit 		= [0-9]
intConst 	= {digit}+
doubleConst = {intConst}("."{intConst})?
any			= .
stringConst = \"~\"
charConst	= '({any})?'


lineTerminator = \r|\n|\r\n
inputCharacter = [^\r\n]
whiteSpace     = {lineTerminator} | [ \t\f]

traditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
endOfLineComment     = "//" {inputCharacter}* {lineTerminator}?
comment = {traditionalComment} | {endOfLineComment}

%%
{whiteSpace} 	{ /* ignore */ }
{comment}		{ /* ignore */ }
"head"			{ return symbol("HEAD", LexerSym.HEAD); }
"start"			{ return symbol("START", LexerSym.START); }
";"				{ return symbol("SEMI", LexerSym.SEMI); }
"int"			{ return symbol("INT", LexerSym.INT); }
"bool"			{ return symbol("BOOL", LexerSym.BOOL); }
"double"		{ return symbol("DOUBLE", LexerSym.DOUBLE); }
"string"		{ return symbol("STRING", LexerSym.STRING); }
"char"			{ return symbol("CHAR", LexerSym.CHAR); }
","				{ return symbol("COMMA", LexerSym.COMMA); }
"def"			{ return symbol("DEF", LexerSym.DEF); }
"("				{ return symbol("LPAR", LexerSym.LPAR); }
")"				{ return symbol("RPAR", LexerSym.RPAR); }
"{"				{ return symbol("LGPAR", LexerSym.LGPAR); }
"}"				{ return symbol("RGPAR", LexerSym.RGPAR); }
"<-"			{ return symbol("READ", LexerSym.READ); }
"->"			{ return symbol("WRITE", LexerSym.WRITE); }
"++"			{ return symbol("INC", LexerSym.INC); }
"--"			{ return symbol("DEC", LexerSym.DEC); }
"+"				{ return symbol("PLUS", LexerSym.PLUS); }
"-"				{ return symbol("MINUS", LexerSym.MINUS); }
"*"				{ return symbol("TIMES", LexerSym.TIMES); }
"/"				{ return symbol("DIV", LexerSym.DIV); }
{intConst}		{ return symbol("INT_CONST", LexerSym.INT_CONST, yytext()); }
{doubleConst}	{ return symbol("DOUBLE_CONST", LexerSym.DOUBLE_CONST, yytext()); }
{stringConst}	{ return symbol("STRING_CONST",LexerSym.STRING_CONST, yytext()); }
{charConst}		{ return symbol("CHAR_CONST",LexerSym.CHAR_CONST, yytext()); }
"true"			{ return symbol("TRUE",LexerSym.TRUE); }
"false"			{ return symbol("FALSE",LexerSym.FALSE); }
"="				{ return symbol("ASSIGN",LexerSym.ASSIGN); }
"if"			{ return symbol("IF",LexerSym.IF); }
"then"			{ return symbol("THEN",LexerSym.THEN);  }
"while"			{ return symbol("WHILE",LexerSym.WHILE); }
"do"			{ return symbol("DO",LexerSym.DO); }
"else"			{ return symbol("ELSE",LexerSym.ELSE); }
">"				{ return symbol("GT",LexerSym.GT); } 
">="			{ return symbol("GE",LexerSym.GE); }
"<"				{ return symbol("LT",LexerSym.LT); }
"<="			{ return symbol("LE",LexerSym.LE); }
"=="			{ return symbol("EQ",LexerSym.EQ); }
"not"			{ return symbol("NOT",LexerSym.NOT); }
"and"			{ return symbol("AND",LexerSym.AND); }
"or"			{ return symbol("OR",LexerSym.OR); }
"in"			{ return symbol("IN",LexerSym.IN); }
"out"			{ return symbol("OUT",LexerSym.OUT); }
"inout"			{ return symbol("INOUT",LexerSym.INOUT); }
{id}			{ return symbol("ID",LexerSym.ID, yytext()); }

[^]				{throw new SyntaxError(yyline, yycolumn);}
