package lib.finlay.core.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.TreeMap;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * Manages events allowing them to be called from any class to trigger other actions.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
public final class EventManager {
	
	private EventManager() {}

	private static ArrayList<Class<? extends Event>> events;
	private static TreeMap<String, ArrayList<Method>> eventHandlers;
	private static boolean started;
	
	static {
		started = false;
	}
	
	/**
	 * This method needs to be called in order to start the loading of the EventManager.<br>
	 * If a class utilises events, this <strong>must</strong> be called first (preferably in the main method)
	 */
	public static void start() {
		if(started) {
			return;
		}
		
		events = new ArrayList<>();
		eventHandlers = new TreeMap<>();
		loadEvents();
		loadEventMethods();
	}
	
	/**
	 * Calls a specific event using reflection.
	 * @param event The event instance to be passed to each method listening for the event.
	 * @return True if it was successful or false if it was unsuccessful.
	 * @throws NullPointerException Thrown if the event parameter is null.
	 * @throws UnknownEventException Thrown if the event parameter is not a valid, loaded event.
	 * @throws ListenerInvocationException Thrown if the invocation of a listening method fails.
	 */
	public static boolean callEvent(Event event) {
		if(event == null) {
			throw new NullPointerException("Event parameter cannot be null.");
		}

		if(!events.contains(event.getClass())) {
			throw new UnknownEventException(event.getClass().getName() + " is not a loaded event.");
		}

		if(!eventHandlers.containsKey(event.getClass().getName())) {
			return false;
		}
		
		ArrayList<Method> methods = eventHandlers.get(event.getClass().getName());
		
		if(methods.size() == 0) {
			return false;
		}

		for(Method method : methods) {
			try {
				method.invoke(method.getDeclaringClass().getConstructor().newInstance(), event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
				throw new ListenerInvocationException("Failed to start " + method.getName() + ": " + e.getMessage());
			}
		}
		
		return true;
	}
	
	private static void loadEvents() {
		ArrayList<Class<? extends Event>> potentialEvents = new ArrayList<>();
		new FastClasspathScanner().matchSubclassesOf(Event.class, potentialEvents::add).scan();
		
		for(Class<? extends Event> event : potentialEvents) {
			if(!Modifier.isAbstract(event.getModifiers()) && Modifier.isPublic(event.getModifiers())) {
				events.add(event);
				System.out.println(event.getCanonicalName());
			}
		}
	}
	
	private static void loadEventMethods() {
		TreeMap<String, ArrayList<Method>> potentialMethods = new TreeMap<>();
		ArrayList<Class<?>> classes = new ArrayList<>();
		
		new FastClasspathScanner().matchClassesWithAnnotation(EventListener.class, classes::add).scan();
		
		for(Class<?> annotatedClass : classes) {
			if(!Modifier.isAbstract(annotatedClass.getModifiers()) && Modifier.isPublic(annotatedClass.getModifiers())) {
				for(Method method : annotatedClass.getMethods()) {
					if(!method.isAnnotationPresent(EventMethod.class)) {
						continue;
					}
					
					Class<?>[] parameterTypes = method.getParameterTypes();
					
					if(parameterTypes.length != 1) {
						continue;
					}
					
					if(parameterTypes[0].getSuperclass() == null) {
						continue;
					}
					
					if(!parameterTypes[0].getSuperclass().equals(Event.class)) {
						continue;
					}
	
					ArrayList<Method> eventMethods = (potentialMethods.containsKey(parameterTypes[0].getName()) ? 
							potentialMethods.get(parameterTypes[0].getName()) : new ArrayList<>());
					eventMethods.add(method);
					
					potentialMethods.put(parameterTypes[0].getName(), eventMethods);
				}
			}
		}
		
		eventHandlers = potentialMethods;
	}
	
}
