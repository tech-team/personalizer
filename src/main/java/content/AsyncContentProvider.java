package content;

import server.ContentReceiver;
import util.ThreadPool;

import java.util.Collection;
import java.util.List;

public class AsyncContentProvider implements IContentProvider {
    private ContentProvider contentProvider;
    private ThreadPool threadPool = new ThreadPool();

    public AsyncContentProvider(ContentReceiver frontend) {
        contentProvider = new ContentProvider(frontend);
    }

    @Override
    public void request(PersonCard request, boolean autoMerge) throws InterruptedException {
        threadPool.execute(() -> {
            try {
                contentProvider.request(request, autoMerge);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void remove(List<PersonId> ids) throws InterruptedException {
        threadPool.execute(() -> {
            contentProvider.remove(ids);
        });
    }

    @Override
    public void merge(Collection<PersonIdsTuple> tuples) throws InterruptedException {
        threadPool.execute(() -> {
            contentProvider.merge(tuples);
        });
    }
}
