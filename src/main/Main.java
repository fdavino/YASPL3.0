package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import lexer.*;
import parser.*;
import syntaxTree.Programma;
import visitor.SymbolTableFillerVisitor;
import visitor.TreePrinterVisitor;
import java_cup.runtime.Symbol;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileInputStream fs = new FileInputStream(new File("src/main/source.yaspl"));
			LexerLex lexer = new LexerLex(fs);
		   	/*for(int tokenId= lexer.next_token().sym; tokenId != LexerSym.EOF; tokenId  = lexer.next_token().sym) {
		   		System.out.println("token returned is "+ LexerSym.terminalNames[tokenId] + "\n");
		   	}*/
			ParserCup parser = new ParserCup(lexer);
			Programma p = (Programma) parser.parse().value;
			TreePrinterVisitor tpv = new TreePrinterVisitor();
			String r = tpv.visit(p);
			FileWriter fw = new FileWriter("src/main/ast.xml");
		    fw.write(r);
		    fw.close();
		    
		    SymbolTableFillerVisitor stfv = new SymbolTableFillerVisitor();
		    stfv.visit(p);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Add argoment");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
