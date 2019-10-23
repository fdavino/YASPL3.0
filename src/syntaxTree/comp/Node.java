package syntaxTree.comp;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
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
	
	private Location left;
	private Location right;
	
	public Node(Location left, Location right, String op) {
		this.op = op;
		this.left = left;
		this.right = right;
	}
	
	public Node(Location left, Location right, String op, SymbolTable.Type t) {
		this.op = op; 
		this.type = t;
		this.left = left;
		this.right = right;
	}
	
	public Node(Location left, Location right, String op, SymbolTable st) {
		this.op = op; 
		this.symTableRef = st;
		this.left = left;
		this.right = right;
	}
	
	public Node(Location left, Location right, String op, SymbolTable.Type t, SymbolTable st) {
		this.op = op; 
		this.type = t;
		this.symTableRef = st;
		this.left = left;
		this.right = right;
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

	public Location getLeft() {
		return left;
	}

	public void setLeft(Location left) {
		this.left = left;
	}

	public Location getRight() {
		return right;
	}

	public void setRight(Location right) {
		this.right = right;
	}
	
	
	
	
	
	
	
	

}
