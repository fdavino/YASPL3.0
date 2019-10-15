package exception;


public class SyntaxError extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	public SyntaxError(String val, int line, int column) {
		String toPrint = String.format("Errore di sintassi su %s linea:%d colonna:%d",val, line, column);
		System.err.println(toPrint);
		System.exit(1);
	}
	
	public SyntaxError(int line, int column) {
		String toPrint = String.format("Errore di sintassi linea:%d colonna:%d", line, column);
		System.err.println(toPrint);
		System.exit(1);
	}

}
