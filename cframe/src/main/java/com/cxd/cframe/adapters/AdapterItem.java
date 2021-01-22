package com.cxd.cframe.adapters;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.FIELD})
public @interface AdapterItem {
    Class<?> value();
}
