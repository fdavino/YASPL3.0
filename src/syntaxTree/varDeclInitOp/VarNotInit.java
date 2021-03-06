package syntaxTree.varDeclInitOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.leaf.IdConst;
import syntaxTree.wrapper.VarDeclsInitWrapper;
import visitor.Visitable;
import visitor.Visitor;

public class VarNotInit extends VarDeclsInitWrapper implements Visitable {

	private String op;
	private IdConst id;
	
	public VarNotInit(Location left, Location right, String op, IdConst id) {
		super(left, right, op,id);
		this.op = op;
		this.id = id;
	}

	@Override
	public Object accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

	public String getOp() {
		return op;
	}

	public IdConst getId() {
		return id;
	}

}
