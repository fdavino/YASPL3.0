package syntax_tree.statOp;

import syntax_tree.wrapper.StatWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class CallOp extends StatWrapper implements Visitable {

	public CallOp(String op) {
		super(op);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}
}
