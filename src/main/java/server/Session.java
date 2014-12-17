package server;

public class Session {
    private BufferedAsyncContentProvider cp;

    public Session() {
        this.cp = new BufferedAsyncContentProvider(new BufferedContentReceiver());
    }

    public Session(BufferedAsyncContentProvider cp) {
        this.cp = cp;
    }

    public BufferedAsyncContentProvider getCP() {
        return cp;
    }
}
