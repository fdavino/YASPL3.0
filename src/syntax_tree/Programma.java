package syntax_tree;

import syntax_tree.comp.Internal;
import visitor.Visitable;
import visitor.Visitor;

public class Programma extends Internal implements Visitable{

	public Programma(String op, Decls d, Statements s) {
		super(op, d, s);
	}

	@Override
	public Programma accept(Visitor<?> visitor) {
		return (Programma) visitor.visit(this);
	}

}
