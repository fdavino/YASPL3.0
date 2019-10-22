package syntaxTree.statOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.Expr;
import syntaxTree.Stat;
import syntaxTree.leaf.IdConst;
import visitor.Visitable;
import visitor.Visitor;

public class IncOp extends Stat implements Visitable{

	private String op;
	private IdConst id;
	private boolean pre;
	
	public IncOp(Location left, Location right, String op, boolean prefix, IdConst id) {
		super(left,right,op,id);
		this.op = op;
		this.id = id;
		this.pre = prefix;
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



	public boolean isPrefix() {
		return pre;
	}



	public void setPrefix(boolean prefix) {
		this.pre = prefix;
	}



	@Override
	public Object accept(Visitor<?> visitor) throws RuntimeException {
		return visitor.visit(this);
	}

}
