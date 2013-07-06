package com.babyduncan.purpleFrog.partview.internal.anno;

import java.lang.annotation.*;

/**
 * User: guohaozhao@yahoo.cn
 * Date: 13-3-9
 * Time: 17:21
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PartialViewParameter {
    //the name of the paramter
    String name();
}
