package content;

import content.source.ContentSource;
import content.source.fb.Facebook;
import content.source.linkedin.LinkedIn;
import content.source.vk.VK;
import server.ContentReceiver;
import util.ThreadPool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContentProvider implements IContentProvider {

    private ContentReceiver frontend;
    private ThreadPool threadPool = new ThreadPool();

    private Map<ContentSource, PersonList> sources = new HashMap<>();
    private PersonList mergedList = new PersonList(ContentSource.Type.NONE);
    private Map<PersonId, PersonCard> fullList;


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
    public void request(PersonCard request, boolean autoMerge) throws InterruptedException {
        for (Map.Entry<ContentSource, PersonList> source : sources.entrySet()) {
            threadPool.execute(() -> {
                PersonList list = source.getValue();
                source.getKey().retrieve(request, list);

                frontend.postPersonList(list);
            });
        }
        threadPool.waitForFinish();

        fullList = new HashMap<>();
        for (ContentSource cs : sources.keySet()) {
            fullList.putAll(sources.get(cs).getPersons());
        }

        if (autoMerge) {
            automaticMerge();
        }

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

        for (PersonId id : tuple.getIds()) {
            for (PersonId id2 : tuple.getIds()) {
                fullList.get(id).linkWith(fullList.get(id2));
            }
        }

        frontend.postResults(mergedList);
        frontend.onFinishedMerge();
    }


    private void automaticMerge() {
        for (PersonCard card : fullList.values()) {

            for (PersonCard targetCard : fullList.values()) {
                if (targetCard != card) {
                    for (SocialLink link : card.getSocialLinks().values()) {
                        if (link.getLinkType() == targetCard.getPersonLink().getLinkType()
                                && link.getId().equalsIgnoreCase(targetCard.getPersonLink().getId())) {
                            if (!card.isLinkedWith(targetCard)) {
                                card.linkWith(targetCard);
                                targetCard.linkWith(card);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ContentProvider cp = new ContentProvider(null);
        cp.request(new PersonCard(), false);
    }
}
