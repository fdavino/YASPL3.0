package syntaxTree.statOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import semantic.SymbolTable;
import syntaxTree.Body;
import syntaxTree.Expr;
import syntaxTree.Stat;
import visitor.Visitable;
import visitor.Visitor;

public class IfThenElseOp extends Stat implements Visitable {

	private String op;
	private Expr e;
	private Body b1;
	private Body b2;
	
	private SymbolTable symTableRefElse;
	
	public IfThenElseOp(Location left, Location right, String op, Expr e, Body b1, Body b2) {
		super(left, right, op, e, b1, b2);
		this.op = op;
		this.e = e;
		this.b1 = b1;
		this.b2 = b2;
	}
	
	@Override
	public Object accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

	public String getOp() {
		return op;
	}

	public Expr getE() {
		return e;
	}

	public Body getB1() {
		return b1;
	}

	public Body getB2() {
		return b2;
	}

	public SymbolTable getSymTableRefElse() {
		return symTableRefElse;
	}

	public void setSymTableRefElse(SymbolTable symTableRefElse) {
		this.symTableRefElse = symTableRefElse;
	}
	
	
	
	

}
