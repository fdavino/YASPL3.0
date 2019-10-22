package exception;

import syntaxTree.comp.Node;

public class SemanticException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SemanticException(String mess, Node n) {
		System.err.println(String.format("%s line <%s> column <%s>", mess, n.getLeft().getLine(), n.getRight().getColumn()));
		System.exit(1);
	}
		
}
