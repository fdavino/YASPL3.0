package syntaxTree.comp;

import semantic.SymbolTable;
import semantic.Tuple;

/**
 * @author ferdi
 *
 */
public class Node {
	
	private String op;
	private SymbolTable.Type type;
	private SymbolTable symTableRef;
	
	public Node(String op) {
		this.op = op; 
	}
	
	public Node(String op, SymbolTable.Type t) {
		this.op = op; 
		this.type = t;
	}
	
	public Node(String op, SymbolTable st) {
		this.op = op; 
		this.symTableRef = st;
	}
	
	public Node(String op, SymbolTable.Type t, SymbolTable st) {
		this.op = op; 
		this.type = t;
		this.symTableRef = st;
	}
	
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public SymbolTable.Type getType() {
		return type;
	}

	public void setType(SymbolTable.Type t) {
		this.type = t;
	}

	public SymbolTable getSymTableRef() {
		return symTableRef;
	}

	public void setSymTableRef(SymbolTable symTableRef) {
		this.symTableRef = symTableRef;
	}
	
	
	
	
	
	

}
