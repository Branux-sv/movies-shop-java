package org.branuxsv.rentalmovies.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
* Public interface that implement NameBinding 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-01 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequiredAuthorization {

}
