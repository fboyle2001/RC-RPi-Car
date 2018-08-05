package lib.finlay.core.events;

/**
 * An exception thrown if the invocation of a listening method fails.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public class ListenerInvocationException extends RuntimeException {

	private static final long serialVersionUID = -1608175400461071192L;

	public ListenerInvocationException() {
		super();
	}
	
	public ListenerInvocationException(String message) {
		super(message);
	}
	
}
