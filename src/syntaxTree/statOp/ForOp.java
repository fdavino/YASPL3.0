package syntaxTree.statOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.Body;
import syntaxTree.CompStat;
import syntaxTree.Expr;
import syntaxTree.Stat;
import syntaxTree.leaf.IdConst;
import syntaxTree.leaf.IntConst;
import visitor.Visitable;
import visitor.Visitor;

public class ForOp extends Stat implements Visitable {

	private String op;
	private IdConst id;
	private Expr e1;
	private Expr e2;
	private IntConst step;
	private Body b;
	
	public ForOp(Location left, Location right, String op,IdConst id, Expr e1, Expr e2, IntConst step, Body b) {
		super(left, right, op, id, e1, e2, step, b);
		this.op = op;
		this.id = id;
		this.e1 = e1;
		this.e2 = e2;
		this.step = step;
		this.b = b;
	}
	
	@Override
	public Object accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

	public String getOp() {
		return op;
	}

	public Expr getE1() {
		return e1;
	}

	public Body getB() {
		return b;
	}
	
	
	public void setB(Body b) {
		this.b = b;
	}

	public IdConst getId() {
		return id;
	}

	public void setId(IdConst id) {
		this.id = id;
	}

	public Expr getE2() {
		return e2;
	}

	public void setE2(Expr e2) {
		this.e2 = e2;
	}

	public IntConst getStep() {
		return step;
	}

	public void setStep(IntConst step) {
		this.step = step;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public void setE1(Expr e1) {
		this.e1 = e1;
	}
	
	
	
	
	
	

}
