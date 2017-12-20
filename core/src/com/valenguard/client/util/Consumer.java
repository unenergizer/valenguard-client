package com.valenguard.client.util;

/**
 * Created by unene on 12/20/2017.
 */

@FunctionalInterface
public interface Consumer <T> {
    void accept(T data);
}
