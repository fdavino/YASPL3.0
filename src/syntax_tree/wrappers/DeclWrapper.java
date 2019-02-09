package syntax_tree.wrappers;

import syntax_tree.leaf.TypeLeaf;
import syntax_tree.VarDeclsInit;
import syntax_tree.comp.Internal;
import visitor.Visitable;
import visitor.Visitor;

public class DeclWrapper extends Internal implements Visitable{

	//VarDecl 
	public DeclWrapper(String op, TypeLeaf tl, VarDeclsInit vdi) {
		super(op, tl, vdi);
		// TODO Auto-generated constructor stub
	}
	
	//DefDecl
	public DeclWrapper(String op, DefWrapper dw) {
		super(op, dw);
	}

	@Override
	public DeclWrapper accept(Visitor<?> visitor) {
		return (DeclWrapper) visitor.visit(this);
	}

}
