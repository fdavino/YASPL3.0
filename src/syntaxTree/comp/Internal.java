package syntaxTree.comp;

import java.util.ArrayList;

import syntaxTree.comp.Node;

public class Internal extends Node {
	
	private ArrayList<Node> childList;
	
	public Internal(String op, Node...sons) {
		super(op);
		if(childList == null)
			this.childList = new ArrayList<>();
		for(Node son : sons) {
			childList.add(son);
		}
	}
	
	public Internal(String op, String value) {
		super(op);
		if(childList == null)
			this.childList = new ArrayList<>();
		childList.add(new Leaf(op, value));
	}

	@Override
	public String toString() {
		String toReturn = "<"+this.getOp()+">\n";
		for(Node n: childList) {
			toReturn += n.toString();
		}
		toReturn += "</"+this.getOp()+">\n";
		return toReturn;
	}	
}