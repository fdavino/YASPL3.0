package syntaxTree.logicOp;

import java_cup.runtime.ComplexSymbolFactory.Location;
import semantic.SymbolTable.Type;
import syntaxTree.Expr;
import visitor.Visitable;
import visitor.Visitor;

public class OrOp extends Expr implements Visitable {

	private String op;
	private Expr e1;
	private Expr e2;
	
	public OrOp(Location left, Location right, String op, Expr e1, Expr e2) {
		super(left, right, op, e1, e2);
		this.op = op;
		this.e1 = e1;
		this.e2 = e2;
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

	public Expr getE2() {
		return e2;
	}


	
	

}
