package syntax_tree.def_decl_op;

import syntax_tree.wrappers.DefWrapper;
import syntax_tree.Body;
import syntax_tree.leaf.IdLeaf;
import visitor.Visitable;
import visitor.Visitor;

public class DefDeclOp extends DefWrapper implements Visitable {

	public DefDeclOp(String op, IdLeaf id, Body body) {
		super(op, id, body);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public DefDeclOp accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return (DefDeclOp) visitor.visit(this);
	}
}
