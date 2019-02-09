package syntax_tree.comp;

public class Leaf extends Node {

	private String value;
	
	public Leaf(String op, String value) {
		super(op);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return "\n<"+ this.getOp() + " value=\"" + this.getValue() + "\">\n</"+this.getOp()+">\n";
	}
	
	

}
