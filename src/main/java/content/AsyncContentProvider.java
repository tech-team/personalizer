package content;

import server.Frontend;
import util.ThreadPool;

public class AsyncContentProvider implements IContentProvider {
    private ContentProvider contentProvider;
    private ThreadPool threadPool = new ThreadPool();

    public AsyncContentProvider(Frontend frontend) {
        contentProvider = new ContentProvider(frontend);
    }

    @Override
    public void request(Request request) {
        try {
            threadPool.execute(() -> {
                contentProvider.request(request);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            threadPool.stopExecution();
        }
    }

    @Override
    public void remove(PersonId[] ids) {
        try {
            threadPool.execute(() -> {
                contentProvider.remove(ids);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            threadPool.stopExecution();
        }
    }

    @Override
    public void merge(PersonIdsTuple tuple) {
        try {
            threadPool.execute(() -> {
                contentProvider.merge(tuple);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            threadPool.stopExecution();
        }
    }
}
