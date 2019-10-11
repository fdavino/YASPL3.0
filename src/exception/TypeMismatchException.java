package exception;

import semantic.SymbolTable.Type;

public class TypeMismatchException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public TypeMismatchException(String op, Type t1) {
		String toPrint = String.format("Conflitto di tipo in %s, %s non compatibile", op, 
				t1.toString());
		System.err.println(toPrint);
		System.exit(1);
	}
	
	public TypeMismatchException(String op, Type t1, Type t2) {
		String toPrint = String.format("Conflitto di tipo in %s, richiesto %s fornito %s", op, 
				t1.toString(),
				t2.toString());
		System.err.println(toPrint);
		System.exit(1);
	}

}
