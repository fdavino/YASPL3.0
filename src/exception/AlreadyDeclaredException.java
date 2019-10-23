package exception;

import java_cup.runtime.ComplexSymbolFactory.Location;
import syntaxTree.comp.Node;

public class AlreadyDeclaredException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AlreadyDeclaredException(String id, String scopeName, Node n) {
		System.err.println(String.format("%s già dichiarata nel %s scope : linea <%s> colonna <%s>",
				  id,
				  scopeName,
				  n.getLeft().getLine(),
				  n.getRight().getColumn()
	));
		System.exit(1);
	}
		
}
