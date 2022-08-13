package com.wheeler.ytarchiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CallbackOnCloseFileInputStream extends FileInputStream {
    private final Runnable onCloseCallback;

    public CallbackOnCloseFileInputStream(File file, Runnable onCloseCallback) throws FileNotFoundException {
        super(file);
        this.onCloseCallback = onCloseCallback;
    }

    @Override
    public void close() throws IOException {
        super.close();
        onCloseCallback.run();
    }
}
