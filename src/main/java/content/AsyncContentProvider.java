package content;

import util.ThreadPool;

public class AsyncContentProvider {
    public static interface ContentCallback {
        void onReady(PersonCard contentData);
    }



    private ContentProvider contentProvider = new ContentProvider();
    private ThreadPool threadPool = new ThreadPool();

    public void execute(Request request, ContentCallback cb) {
        try {
            threadPool.execute(() -> {
                PersonCard resp = contentProvider.execute(request);
                cb.onReady(resp);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            threadPool.stopExecution();
        }
    }
}
