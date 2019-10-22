package exception;

import syntaxTree.comp.Node;

public class WrongArgumentNumberException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public WrongArgumentNumberException(String id, int require, int provided, Node n) {
		System.err.println(String.format("Wrong number of argument for function "
				+ "%s, required :%d provided:%d linea <%s> colonna <%s>", id, require, provided, n.getLeft().getLine(), n.getRight().getColumn()));
		System.exit(1);
	}
		
}
