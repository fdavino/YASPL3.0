package syntaxTree;

import syntaxTree.comp.Internal;
import syntaxTree.comp.Leaf;
import visitor.Visitable;
import visitor.Visitor;

public class Expr extends Internal {
	
	private String op;
	private Expr e1;
	private Expr e2;
	private String leaf;
	
	//Operazioni binarie
	public Expr(String op, Expr e1, Expr e2) {
		super(op, e1, e2);
		this.op = op;
		this.e1 = e1;
		this.e2 = e2;
	}
	
	//Operazioni unarie
	public Expr(String op, Expr e) {
		super(op, e);
		this.op = op;
		this.e1 = e;
	}
	
	//non creo IdLeaf così facendo non ho necessità di creare un nodo interno per contenere un IdLeaf
	public Expr(String op, String id) {
		super(op, id);
		this.op = op;
		this.leaf = id;
	}
	
	public Object accept(Visitor<?> visitor) {
		return visitor.visit(this);
	}

	public String getOp() {
		return op;
	}

	public Expr getE1() {
		return e1;
	}

	public Expr getE2() {
		return e2;
	}

	public String getLeaf() {
		return leaf;
	}
	
	
		

}
