package ir.moke.aparat.download;

import ir.moke.aparat.Calculator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class CallBackByteChannel implements ReadableByteChannel {
    private ReadableByteChannel rbc;
    private long size;
    private ProgressCallBack delegate;
    private long sizeRead;

    public CallBackByteChannel(ReadableByteChannel rbc, long expectedSize, ProgressCallBack delegate) {
        this.rbc = rbc;
        this.size = expectedSize;
        this.delegate = delegate;
    }

    @Override
    public int read(ByteBuffer bb) throws IOException {
        int n;
        long progress;
        if ((n = rbc.read(bb)) > 0) {
            sizeRead += n;
            progress = size > 0 ? Calculator.percentage(size,sizeRead) : -1;
            delegate.callBack(this, sizeRead ,progress);
        }
        return n;
    }

    @Override
    public boolean isOpen() {
        return rbc.isOpen();
    }

    @Override
    public void close() throws IOException {
        rbc.close();
    }
}
