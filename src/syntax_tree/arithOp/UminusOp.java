package syntax_tree.arithOp;

import syntax_tree.wrappers.ExprWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class UminusOp extends ExprWrapper implements Visitable{

	public UminusOp(String op, ExprWrapper l) {
		super(op, l);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public UminusOp accept(Visitor<?> visitor) {
		return (UminusOp) visitor.visit(this);
	}
	
}