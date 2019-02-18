package exception;

public class AlreadyDeclaredException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AlreadyDeclaredException(String mess) {
		super(mess);
	}
		
}
