package com.babyduncan.purpleFrog.partview.internal.anno;

import java.lang.annotation.*;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-9
 * Time: 11:12
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PartialViewMapping {

    // the name of this partview
    String value() default "";

    //this partview's father
    String parent() default "";

    //this partview's child
    String defaultChild() default "";

    //this partview's params
    PartialViewParameter[] param() default {};
}
