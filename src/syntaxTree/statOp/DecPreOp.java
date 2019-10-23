package syntaxTree.statOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.Expr;
import syntaxTree.Stat;
import syntaxTree.leaf.IdConst;
import visitor.Visitable;
import visitor.Visitor;

public class DecPreOp extends Stat implements Visitable{

	private String op;
	private IdConst id;
	
	public DecPreOp(Location left, Location right, String op, IdConst id) {
		super(left,right,op,id);
		this.op = op;
		this.id = id;
	}
	
	
	
	public String getOp() {
		return op;
	}



	public void setOp(String op) {
		this.op = op;
	}



	public IdConst getId() {
		return id;
	}



	public void setId(IdConst id) {
		this.id = id;
	}



	@Override
	public Object accept(Visitor<?> visitor) throws RuntimeException {
		return visitor.visit(this);
	}

}
