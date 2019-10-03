package com.niuniu.player.internal;

public interface Source {

    String value();

    <T> boolean is(Class<T> clazz);

    <T> T as(Class<T> clazz);

}
