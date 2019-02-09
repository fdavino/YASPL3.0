package syntax_tree.arithOp;

import syntax_tree.wrappers.ExprWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class DivOp extends ExprWrapper implements Visitable{

	public DivOp(String op, ExprWrapper e1, ExprWrapper e2) {
		super(op, e1, e2);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public DivOp accept(Visitor<?> visitor) {
		return (DivOp) visitor.visit(this);
	}
	
}