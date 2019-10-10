package exception;

public class DeclaredNotUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DeclaredNotUsedException(String mess) {
		System.err.print(mess);
	}
}
