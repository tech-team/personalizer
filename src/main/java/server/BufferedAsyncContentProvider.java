package server;

import content.AsyncContentProvider;

public class BufferedAsyncContentProvider extends AsyncContentProvider {
    private BufferedContentReceiver contentReceiver;

    public BufferedAsyncContentProvider(BufferedContentReceiver contentReceiver) {
        super(contentReceiver);
        this.contentReceiver = contentReceiver;
    }

    public BufferedContentReceiver getContentReceiver() {
        return contentReceiver;
    }
}
