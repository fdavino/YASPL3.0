package syntax_tree.comp;

public class Node{
	
	private String op;
	
	public Node(String op) {
		this.op = op; 
	}
	
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
	
	public String toString() {
		return "\n<"+ this.getOp() + "\\>\n";
	}

}
