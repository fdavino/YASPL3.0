package syntaxTree;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.comp.Internal;
import syntaxTree.comp.Node;
import visitor.Visitable;
import visitor.Visitor;

public class Programma extends Internal implements Visitable {

	private String op;
	private Decls d;
	private Statements s;
	
	public Programma(Location left, Location right, String op, Decls d, Statements s) {
		super(left, right, op, d, s);
		this.op = op;
		this.d = d;
		this.s = s;
	}

	@Override
	public Object accept(Visitor<?> visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

	public String getOp() {
		return op;
	}

	public Decls getD() {
		return d;
	}

	public Statements getS() {
		return s;
	}
	
	

}
