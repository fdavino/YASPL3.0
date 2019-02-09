package syntax_tree.def_decl_op;

import syntax_tree.wrappers.DefWrapper;
import syntax_tree.Body;
import syntax_tree.ParDecls;
import syntax_tree.leaf.IdLeaf;
import visitor.Visitable;
import visitor.Visitor;

public class DefDeclOpWithPar extends DefWrapper implements Visitable {

	public DefDeclOpWithPar(String op, IdLeaf id, Body body, ParDecls pd) {
		super(op, id, body, pd);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public DefDeclOpWithPar accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return (DefDeclOpWithPar) visitor.visit(this);
	}

}
