package syntax_tree;

import syntax_tree.wrappers.ListParent;
import syntax_tree.comp.Node;
import visitor.Visitable;
import visitor.Visitor;

public class ParDecls extends ListParent<ParDecls> implements Visitable{

	public ParDecls(String op) {
		super(op);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ParDecls accept(Visitor<?> visitor) {
		return (ParDecls) visitor.visit(this);
	}
	
}