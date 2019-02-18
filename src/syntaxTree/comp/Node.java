package syntaxTree.comp;

import semantic.SymbolTable;
import semantic.Tuple;

public class Node {
	
	private String op;
	private Tuple tupleRef;
	private SymbolTable symTableRef;
	
	public Node(String op) {
		this.op = op; 
	}
	
	public Node(String op, Tuple t) {
		this.op = op; 
		this.tupleRef = t;
	}
	
	public Node(String op, SymbolTable st) {
		this.op = op; 
		this.symTableRef = st;
	}
	
	public Node(String op, Tuple t, SymbolTable st) {
		this.op = op; 
		this.tupleRef = t;
		this.symTableRef = st;
	}
	
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Tuple getTupleRef() {
		return tupleRef;
	}

	public void setTupleRef(Tuple tupleRef) {
		this.tupleRef = tupleRef;
	}

	public SymbolTable getSymTableRef() {
		return symTableRef;
	}

	public void setSymTableRef(SymbolTable symTableRef) {
		this.symTableRef = symTableRef;
	}
	
	
	
	

}
