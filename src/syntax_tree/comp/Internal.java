package syntax_tree.comp;

import java.util.ArrayList;

public class Internal extends Node {
	
	private ArrayList<Node> sons;
	
	public Internal(String op, Node...sons) {
		super(op);
		for(Node n:sons)
			this.sons.add(n);
	}
	
	public String toString() {
		return "";
	}
	
}
