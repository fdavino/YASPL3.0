package exception;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.comp.Node;

public class DeclaredNotUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DeclaredNotUsedException(String mess, Node n) {
		System.err.print(String.format("%s linea <%s> colonna <%s>", mess, n.getLeft().getLine(), n.getRight().getColumn()));
	}
}
