package syntax_tree;

import syntax_tree.comp.Node;
import syntax_tree.wrappers.ListParent;
import syntax_tree.wrappers.VarDeclsWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class VarDeclsInit extends ListParent<VarDeclsWrapper> implements Visitable{

	public VarDeclsInit(String op) {
		super(op);
		// TODO Auto-generated constructor stub
	}

	@Override
	public VarDeclsInit accept(Visitor<?> visitor) {
		return (VarDeclsInit) visitor.visit(this);
		// TODO Auto-generated method stub
	}

	@Override
	public ListParent<VarDeclsWrapper> addChild(VarDeclsWrapper node) {
		return super.addChild(node);
		// TODO Auto-generated method stub
	}
	
	

}
