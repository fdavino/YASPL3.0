package syntax_tree.logicOp;

import syntax_tree.wrappers.ExprWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class NotOp extends ExprWrapper implements Visitable{

	public NotOp(String op, ExprWrapper e1) {
		super(op, e1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public NotOp accept(Visitor<?> visitor) {
		return (NotOp) visitor.visit(this);
	}
	
}