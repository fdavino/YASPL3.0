
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20160615 (GIT 4ac7450)
//----------------------------------------------------

package parser;

/** CUP generated interface containing symbol constants. */
public interface ParserSym {
  /* terminals */
  public static final int CHAR_CONST = 25;
  public static final int LGPAR = 14;
  public static final int CHAR = 8;
  public static final int GE = 35;
  public static final int UMINUS = 46;
  public static final int DOUBLE_CONST = 23;
  public static final int DOUBLE = 6;
  public static final int SEMI = 4;
  public static final int INT = 45;
  public static final int OUT = 43;
  public static final int MINUS = 19;
  public static final int WRITE = 17;
  public static final int INOUT = 44;
  public static final int AND = 40;
  public static final int NOT = 39;
  public static final int LT = 36;
  public static final int RPAR = 13;
  public static final int IN = 42;
  public static final int OR = 41;
  public static final int BOOL = 5;
  public static final int RGPAR = 15;
  public static final int COMMA = 10;
  public static final int DIV = 21;
  public static final int PLUS = 18;
  public static final int ASSIGN = 28;
  public static final int IF = 29;
  public static final int ID = 9;
  public static final int LE = 37;
  public static final int EOF = 0;
  public static final int INT_CONST = 22;
  public static final int TRUE = 26;
  public static final int error = 1;
  public static final int START = 3;
  public static final int EQ = 38;
  public static final int HEAD = 2;
  public static final int TIMES = 20;
  public static final int ELSE = 33;
  public static final int READ = 16;
  public static final int WHILE = 31;
  public static final int THEN = 30;
  public static final int LPAR = 12;
  public static final int STRING = 7;
  public static final int DEF = 11;
  public static final int FALSE = 27;
  public static final int STRING_CONST = 24;
  public static final int GT = 34;
  public static final int DO = 32;
  public static final String[] terminalNames = new String[] {
  "EOF",
  "error",
  "HEAD",
  "START",
  "SEMI",
  "BOOL",
  "DOUBLE",
  "STRING",
  "CHAR",
  "ID",
  "COMMA",
  "DEF",
  "LPAR",
  "RPAR",
  "LGPAR",
  "RGPAR",
  "READ",
  "WRITE",
  "PLUS",
  "MINUS",
  "TIMES",
  "DIV",
  "INT_CONST",
  "DOUBLE_CONST",
  "STRING_CONST",
  "CHAR_CONST",
  "TRUE",
  "FALSE",
  "ASSIGN",
  "IF",
  "THEN",
  "WHILE",
  "DO",
  "ELSE",
  "GT",
  "GE",
  "LT",
  "LE",
  "EQ",
  "NOT",
  "AND",
  "OR",
  "IN",
  "OUT",
  "INOUT",
  "INT",
  "UMINUS"
  };

  /* non terminals */
  static final int Type = 4;
  static final int Expr = 16;
  static final int Programma = 0;
  static final int Def_decl = 8;
  static final int Statements = 2;
  static final int Var_decls = 9;
  static final int Par_type = 11;
  static final int Var_decl = 3;
  static final int Var_decls_init = 5;
  static final int Decls = 1;
  static final int Comp_stat = 15;
  static final int Stat = 13;
  static final int Body = 12;
  static final int Var_init_value = 6;
  static final int Args = 14;
  static final int Par_decls = 10;
  static final int Vars = 7;
}

