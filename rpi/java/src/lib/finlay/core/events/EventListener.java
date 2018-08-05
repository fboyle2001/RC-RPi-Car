package lib.finlay.core.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to denote that a class contains at least one method requiring an event as a parameter.
 * @author Finlay
 * @version 1.0
 * @since 1.0
 * @see {@link EventMethod}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventListener {}
