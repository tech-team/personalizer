package content;

import content.source.ContentSource;
import content.source.fb.Facebook;
import content.source.linkedin.LinkedIn;
import content.source.vk.VK;
import server.Frontend;
import util.ThreadPool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContentProvider implements IContentProvider {

    private Frontend frontend;
    private ThreadPool threadPool = new ThreadPool();

    private Map<ContentSource, PersonList> sources = new HashMap<>();
    private PersonList mergedList = new PersonList(ContentSource.Type.NONE);


    public ContentProvider(Frontend frontend) {
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
                source.getKey().retrieve(request, source.getValue());
            });
        }
        
        threadPool.waitForFinish();
    }

    @Override
    public void remove(PersonId[] ids) {

    }

    @Override
    public void merge(PersonIdsTuple tuple) {

    }

    public static void main(String[] args) throws InterruptedException {
        ContentProvider cp = new ContentProvider(null);
        cp.request(new PersonCard());
    }
}
