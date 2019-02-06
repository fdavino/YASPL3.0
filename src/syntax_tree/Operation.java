package syntax_tree;

import java.util.ArrayList;

public class Operation extends Node {
	
	private ArrayList<Node> nodeList;
	
	public Operation(String op, Node...sons) {
		super(op);
		this.nodeList = new ArrayList<>();
		for(Node son : sons) {
			nodeList.add(son);
		}
	}
	
	public Operation addChild(Node son) {
		nodeList.add(son);
		return this;
	}
	
	public ArrayList<Node> getNodeList(){
		return nodeList;
	}
	
	public String toString() {
		String toReturn = "->";
		toReturn = toReturn + "<"+ this.getOp() +">\n\t";
		for(Node n:nodeList)
			toReturn = toReturn + n.toString();
		
		return toReturn;
	}
	
}
