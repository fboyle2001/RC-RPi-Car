package lib.finlay.core.events;

/**
 * Thrown if an event has not been initialised or is invalid.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public class UnknownEventException extends RuntimeException {

	private static final long serialVersionUID = 1635883622640167842L;

	public UnknownEventException() {
		super();
	}
	
	public UnknownEventException(String message) {
		super(message);
	}
	
}
