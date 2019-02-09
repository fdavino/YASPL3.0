package syntax_tree.wrappers;

import syntax_tree.comp.Internal;
import visitor.Visitor;

public class ExprWrapper extends Internal{

	public ExprWrapper(String op, ExprWrapper e1, ExprWrapper e2) {
		super(op, e1, e2);
		// TODO Auto-generated constructor stub
	}
	
	public ExprWrapper(String op, ExprWrapper leaf) {
		super(op, leaf);
	}
	
	public ExprWrapper accept(Visitor<?> visitor) {
		return (ExprWrapper) visitor.visit(this);
	}
}
