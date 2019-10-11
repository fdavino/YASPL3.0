package exception;

public class SemanticException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SemanticException(String mess) {
		System.err.println(mess);
		System.exit(1);
	}
		
}
