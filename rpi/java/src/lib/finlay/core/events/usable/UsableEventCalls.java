package lib.finlay.core.events.usable;

import lib.finlay.core.events.EventManager;

/**
 * Registers call event hooks for predefined events.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public final class UsableEventCalls {

	private UsableEventCalls() {}
	
	private static boolean registered = false;
	
	/**
	 * Registers the events in this package.
	 */
	public static void registerEventCalls() {
		if(registered) return;
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			EventManager.callEvent(new SystemExitEvent());
		}));
		
		registered = true;
	}
	
}
