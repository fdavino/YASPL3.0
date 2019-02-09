package syntax_tree;

import syntax_tree.comp.Internal;
import syntax_tree.leaf.IdLeaf;
import syntax_tree.wrappers.ExprWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class Stat extends Internal implements Visitable {
	
	public Stat(String op, Vars v) {
		super(op, v);
	}
	
	public Stat(String op, Args a) {
		super(op, a);
	}
	
	public Stat(String op, IdLeaf id, ExprWrapper ex) {
		super(op, id, ex);
	}
	
	public Stat(String op, IdLeaf id, Args a) {
		super(op, id, a);
	}
	
	public Stat(String op, IdLeaf id) {
		super(op, id);
	}
	
	public Stat(String op, ExprWrapper ex, CompStat c1, CompStat c2) {
		super(op, ex, c1, c2);
	}
	
	//sia per while che per if
	public Stat(String op, ExprWrapper ex, CompStat c1) {
		super(op, ex, c1);
	}
	
	@Override
	public Stat accept(Visitor<?> visitor) {
		return (Stat) visitor.visit(this);
	}

}
