package com.dhu.portal.utils;

/**
 * Created by Administrator on 2016/3/7.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public  @interface ConvertParameter {
    String value() default "";
    String dateFormat() default "yyyy-MM-dd hh:mm:ss";
    ModelConversion.ConvertType convertType() default ModelConversion.ConvertType.auto;
    boolean isAdd() default false;

}
