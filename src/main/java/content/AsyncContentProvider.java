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
    public void request(PersonCard request) throws InterruptedException {
        threadPool.execute(() -> {
            try {
                contentProvider.request(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void remove(PersonId[] ids) throws InterruptedException {
        threadPool.execute(() -> {
            contentProvider.remove(ids);
        });
    }

    @Override
    public void merge(PersonIdsTuple tuple) throws InterruptedException {
        threadPool.execute(() -> {
            contentProvider.merge(tuple);
        });
    }
}
