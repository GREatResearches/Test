package br.ufc.great.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Erick on 2016-05-07.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControlInterpect {
	public ControlContext[] contexts() default {};
	public ControlFeature[] features() default {};
}

