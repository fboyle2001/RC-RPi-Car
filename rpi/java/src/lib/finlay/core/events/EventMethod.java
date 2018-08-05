package lib.finlay.core.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Placed above methods that use events as parameters.<br>
 * These are then called when the corresponding event is called.<br>
 * Must be paired with an {@link EventClass} annotation.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventMethod {}
