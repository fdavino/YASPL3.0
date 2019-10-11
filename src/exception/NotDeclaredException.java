package exception;

public class NotDeclaredException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NotDeclaredException(String id, String scopeName) {
		System.err.println(String.format("%s non dichiarata nel %s scope",
				  id,
				  scopeName
	));
		System.exit(1);
	}
}
