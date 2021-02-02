package com.yds.iocdemo.java;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EvenBase(listenerSetter = "setOnLongClickListener",listenerType = View.OnLongClickListener.class,callbackMethod = "onLongClick")
public @interface OnLongClick {
    int[] value();
}
