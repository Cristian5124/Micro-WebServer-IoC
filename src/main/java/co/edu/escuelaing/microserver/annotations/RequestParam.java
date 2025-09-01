package co.edu.escuelaing.microserver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to bind a method parameter to a web request parameter.
 *
 * @param value        the name of the request parameter to bind to
 * @param defaultValue the default value to use if the parameter is not present
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {

    String value();

    String defaultValue() default "";
}
