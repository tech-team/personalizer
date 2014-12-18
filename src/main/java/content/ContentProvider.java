package content;

import content.source.ContentSource;
import content.source.vk.VK;
import server.ContentReceiver;
import util.MyLogger;
import util.ThreadPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ContentProvider implements IContentProvider {

    private ContentReceiver frontend;
    private ThreadPool threadPool = new ThreadPool();

    private Map<ContentSource, PersonList> sources = new HashMap<>();
    private PersonList mergedList = new PersonList(ContentSource.Type.NONE);
    private PersonList fullList;

    private Logger logger = MyLogger.getLogger(this.getClass().getName());


    public ContentProvider(ContentReceiver frontend) {
        this.frontend = frontend;

        ContentSource[] sources = {
                new VK(),
//                new Facebook(),
//                new LinkedIn()
        };
        for (ContentSource s : sources) {
            this.sources.put(s, new PersonList(s.getType()));
        }
    }

    @Override
    public void request(PersonCard request, boolean autoMerge) throws InterruptedException {
        for (Map.Entry<ContentSource, PersonList> source : sources.entrySet()) {
            threadPool.execute(() -> {
                String sourceStr = source.getKey().getType().toString();
                logger.info("Started source: " + sourceStr);
                PersonList list = source.getValue();
                source.getKey().retrieve(request, list);
                logger.info("Finished source: " + sourceStr);

                frontend.postPersonList(list);
            });
        }
        threadPool.waitForFinish();
        logger.info("Finished all sources");

        fullList = new PersonList();
        for (ContentSource cs : sources.keySet()) {
            fullList.addAll(sources.get(cs).getPersons());
        }

        if (autoMerge) {
            logger.info("Started auto merge");
            automaticMerge();
            logger.info("Finished auto merge");
        }

        frontend.onFinishedListsRetrieval();
    }

    @Override
    public void remove(List<PersonId> ids) {
        for (PersonId id : ids) {
            for (ContentSource source : sources.keySet()) {
                if (source.getType() == id.getType()) {
                    sources.get(source).remove(id);
                    break;
                }
            }
            fullList.remove(id);
        }

        frontend.onFinishedRemoval();
    }

    @Override
    public void merge(PersonIdsTuple tuple) {

        List<PersonId> ids = tuple.getIds();
        PersonCard mergedCard = fullList.getPerson(ids.get(0)).copy();
        for (int i = 1; i < tuple.getIds().size(); ++i) {
            PersonId id = ids.get(i);
            PersonCard p = fullList.getPerson(id);
            mergedCard.linkWith(p);
            mergedList.addPerson(mergedCard);
        }

        frontend.postResults(mergedList);
        frontend.onFinishedMerge();
    }


    private void automaticMerge() {
        for (PersonCard card : fullList.getPersons().values()) {

            for (PersonCard targetCard : fullList.getPersons().values()) {
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
