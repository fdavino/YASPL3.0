package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import lexer.*;
import parser.*;
import syntaxTree.Programma;
import visitor.CLangCodeGenerator;
import visitor.SymTableVisitor;
import visitor.TreePrinterVisitor;
import visitor.TypeCheckerVisitor;
import java_cup.runtime.Symbol;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileInputStream fs = new FileInputStream(new File("src/main/example/palindormo.yaspl"));
			LexerLex lexer = new LexerLex(fs);
			ParserCup parser = new ParserCup(lexer);
			Programma p = (Programma) parser.parse().value;
		    SymTableVisitor stfv = new SymTableVisitor("src/main/scope.log");
		    stfv.visit(p);
		    TypeCheckerVisitor tckv = new TypeCheckerVisitor();
		    tckv.visit(p);
		    CLangCodeGenerator clcg = new CLangCodeGenerator();
		    FileWriter fw = new FileWriter("src/main/intermedio.c");
		    fw.write(clcg.visit(p));
		    fw.flush();
		    fw.close();
		    TreePrinterVisitor tpv = new TreePrinterVisitor();
			String r = tpv.visit(p);
			fw = new FileWriter("src/main/ast1.xml");
		    fw.write(r);
		    fw.close();
		   
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
