package syntaxTree.statOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.Args;
import syntaxTree.Stat;
import syntaxTree.comp.Leaf;
import syntaxTree.leaf.IdConst;
import visitor.Visitable;
import visitor.Visitor;

public class AssignOp extends Stat implements Visitable {

	private String op;
	private IdConst id;
	private Args a;
	
	public AssignOp(Location left, Location right, String op, IdConst id, Args a) {
		super(left, right, op, id, a);
		this.op = op;
		this.id = id;
		this.a = a;
	}

	@Override
	public Object accept(Visitor<?> visitor) {
		return visitor.visit(this);
	}

	public String getOp() {
		return op;
	}

	public IdConst getId() {
		return id;
	}

	public Args getA() {
		return a;
	}
	
	
}
