package syntax_tree;


import syntax_tree.comp.Internal;
import syntax_tree.comp.Node;
import syntax_tree.wrappers.ExprWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class VarInitValue extends Internal implements Visitable{
	
	public VarInitValue(String op, ExprWrapper e1) {
		super(op, e1);
	}

	@Override
	public VarInitValue accept(Visitor<?> visitor) {
		return (VarInitValue) visitor.visit(this);
	}

}
