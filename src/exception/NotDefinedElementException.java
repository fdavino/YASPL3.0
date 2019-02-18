/**
 * 
 */
package exception;


/**
 * @author ferdi
 *
 */
public class NotDefinedElementException extends RuntimeException {

	public NotDefinedElementException(String id) {
		super(String.format("Variabile %s non definita", id));
	}
}
