package com.valenguard.client.network.shared;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by unene on 12/20/2017.
 */

@FunctionalInterface
public interface Write {
    void accept(ObjectOutputStream outStream) throws IOException;
}
