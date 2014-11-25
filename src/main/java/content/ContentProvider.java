package content;

import content.source.ContentSource;
import content.source.fb.Facebook;
import content.source.linkedin.LinkedIn;
import content.source.vk.VK;
import server.ContentReceiver;
import util.ThreadPool;

import java.util.HashMap;
import java.util.Map;

public class ContentProvider implements IContentProvider {

    private ContentReceiver frontend;
    private ThreadPool threadPool = new ThreadPool();

    private Map<ContentSource, PersonList> sources = new HashMap<>();
    private PersonList mergedList = new PersonList(ContentSource.Type.NONE);


    public ContentProvider(ContentReceiver frontend) {
        this.frontend = frontend;

        ContentSource[] sources = {
                new VK(),
                new Facebook(),
                new LinkedIn()
        };
        for (ContentSource s : sources) {
            this.sources.put(s, new PersonList(s.getType()));
        }
    }

    @Override
    public void request(PersonCard request) throws InterruptedException {
        for (Map.Entry<ContentSource, PersonList> source : sources.entrySet()) {
            threadPool.execute(() -> {
                PersonList list = source.getValue();
                source.getKey().retrieve(request, list);

                frontend.postPersonList(list);
            });
        }
        threadPool.waitForFinish();

        frontend.onFinishedListsRetrieval();
    }

    @Override
    public void remove(PersonId[] ids) {
        for (PersonId id : ids) {
            for (ContentSource source : sources.keySet()) {
                if (source.getType() == id.getType()) {
                    sources.get(source).remove(id);
                    break;
                }
            }
        }

        frontend.onFinishedRemoval();
    }

    @Override
    public void merge(PersonIdsTuple tuple) {

        frontend.postResults(mergedList);
        frontend.onFinishedMerge();
    }

    public static void main(String[] args) throws InterruptedException {
        ContentProvider cp = new ContentProvider(null);
        cp.request(new PersonCard());
    }
}
