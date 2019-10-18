
package lexer;

import java_cup.runtime.*;
import java.io.IOException;

import parser.LexerSym;
import static parser.LexerSym.*;

import exception.*;

%%

%class LexerLex

%unicode
%line
%column

%public
%final
// %abstract

%cupsym lexer.LexerSym
%cup
// %cupdebug

%init{
	// TODO: code that goes to constructor
%init}

%{
	private Symbol symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn+1);
	}
	
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }

%}

%eofval{
    return new Symbol(LexerSym.EOF);
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
"head"			{ return symbol(LexerSym.HEAD); }
"start"			{ return symbol(LexerSym.START); }
";"				{ return symbol(LexerSym.SEMI); }
"int"			{ return symbol(LexerSym.INT); }
"bool"			{ return symbol(LexerSym.BOOL); }
"double"		{ return symbol(LexerSym.DOUBLE); }
"string"		{ return symbol(LexerSym.STRING); }
"char"			{ return symbol(LexerSym.CHAR); }
","				{ return symbol(LexerSym.COMMA); }
"def"			{ return symbol(LexerSym.DEF, "cacacaca"); }
"("				{ return symbol(LexerSym.LPAR); }
")"				{ return symbol(LexerSym.RPAR); }
"{"				{ return symbol(LexerSym.LGPAR); }
"}"				{ return symbol(LexerSym.RGPAR); }
"<-"			{ return symbol(LexerSym.READ); }
"->"			{ return symbol(LexerSym.WRITE); }
"+"				{ return symbol(LexerSym.PLUS); }
"-"				{ return symbol(LexerSym.MINUS); }
"*"				{ return symbol(LexerSym.TIMES); }
"/"				{ return symbol(LexerSym.DIV); }
{intConst}		{ return symbol(LexerSym.INT_CONST, yytext()); }
{doubleConst}	{ return symbol(LexerSym.DOUBLE_CONST, yytext()); }
{stringConst}	{ return symbol(LexerSym.STRING_CONST, yytext().substring(1,yytext().length()-1)); }
{charConst}		{ return symbol(LexerSym.CHAR_CONST, ""+yytext().charAt(1)); }
"true"			{ return symbol(LexerSym.TRUE); }
"false"			{ return symbol(LexerSym.FALSE); }
"="				{ return symbol(LexerSym.ASSIGN); }
"if"			{ return symbol(LexerSym.IF); }
"then"			{ return symbol(LexerSym.THEN);  }
"while"			{ return symbol(LexerSym.WHILE); }
"do"			{ return symbol(LexerSym.DO); }
"else"			{ return symbol(LexerSym.ELSE); }
">"				{ return symbol(LexerSym.GT); }
">="			{ return symbol(LexerSym.GE); }
"<"				{ return symbol(LexerSym.LT); }
"<="			{ return symbol(LexerSym.LE); }
"=="			{ return symbol(LexerSym.EQ); }
"not"			{ return symbol(LexerSym.NOT); }
"and"			{ return symbol(LexerSym.AND); }
"or"			{ return symbol(LexerSym.OR); }
"in"			{ return symbol(LexerSym.IN); }
"out"			{ return symbol(LexerSym.OUT); }
"inout"			{ return symbol(LexerSym.INOUT); }
{id}			{ return symbol(LexerSym.ID, yytext()); }

[^]				{throw new SyntaxError(yyline, yycolumn);}
