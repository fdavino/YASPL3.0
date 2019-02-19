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
		System.err.println(String.format("Variabile %s non definita\n", id));
		System.exit(1);
	}
}
