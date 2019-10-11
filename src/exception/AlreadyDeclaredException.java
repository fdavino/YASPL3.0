package exception;

public class AlreadyDeclaredException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AlreadyDeclaredException(String id, String scopeName) {
		System.err.println(String.format("%s già dichiarata nel %s scope",
				  id,
				  scopeName
	));
		System.exit(1);
	}
		
}
