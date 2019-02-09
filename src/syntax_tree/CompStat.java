package syntax_tree;

import syntax_tree.comp.Internal;
import syntax_tree.comp.Node;
import visitor.Visitable;
import visitor.Visitor;

public class CompStat extends Internal implements Visitable {
	
	public CompStat(String op, Statements s) {
		super(op, s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CompStat accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return (CompStat) visitor.visit(this);
	}
	
}