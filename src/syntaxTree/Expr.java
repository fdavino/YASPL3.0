package syntaxTree;

import java_cup.runtime.ComplexSymbolFactory.Location;
import semantic.SymbolTable;
import syntaxTree.comp.Internal;
import syntaxTree.comp.Leaf;
import visitor.Visitable;
import visitor.Visitor;

public abstract class Expr extends Internal {
	
	private String op;
	
	//Operazioni binarie
	public Expr(Location left, Location right, String op, Expr e1, Expr e2) {
		super(left, right, op, e1, e2);
	}
	
	//Operazioni unarie
	public Expr(Location left, Location right, String op, Expr e) {
		super(left,right, op, e);
	}
	
	//non creo IdLeaf così facendo non ho necessità di creare un nodo interno per contenere un IdLeaf
	public Expr(Location left, Location right, String op, Leaf id) {
		super(left, right, op, id);
	}
	
	public abstract Object accept(Visitor<?> visitor);

	public String getOp() {
		return op;
	}

	
		

}
