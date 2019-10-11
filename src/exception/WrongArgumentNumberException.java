package exception;

public class WrongArgumentNumberException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public WrongArgumentNumberException(String id, int require, int provided) {
		System.err.println(String.format("Wrong number of argument for function "
				+ "%s, required :%d provided:%d", id, require, provided));
		System.exit(1);
	}
		
}
