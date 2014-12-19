package content;

import content.source.ContentSource;
import content.source.fb.Facebook;
import content.source.linkedin.LinkedIn;
import content.source.vk.VK;
import server.ContentReceiver;
import util.MyLogger;
import util.ThreadPool;

import java.util.*;
import java.util.logging.Logger;

public class ContentProvider implements IContentProvider {

    private ContentReceiver frontend;
    private ThreadPool threadPool = new ThreadPool();

    private Map<ContentSource, PersonList> sources = new HashMap<>();
    private List<MergedPersonCard> mergedList = new LinkedList<>();
    private Map<SocialLink, MergedPersonCard> linkList = new HashMap<>();
    private PersonList fullList;

    private Logger logger = MyLogger.getLogger(this.getClass().getName());


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
    public void merge(Collection<PersonIdsTuple> tuples) {

        for (PersonIdsTuple tuple : tuples) {

            List<PersonId> ids = tuple.getIds();

            MergedPersonCard merged = new MergedPersonCard();
            for (PersonId id : ids) {
                MergedPersonCard card = findCard(mergedList, id);
                mergedList.remove(card);
                merged.addOnPersonId(card);
            }
            mergedList.add(merged);


        }

        frontend.postResults(mergedList);
        frontend.onFinishedMerge();
    }

    private MergedPersonCard findCard(Collection<MergedPersonCard> collection, PersonId id) {
        MergedPersonCard res = null;
        for (MergedPersonCard card : collection) {
            PersonCard personCard = card.get(id);
            if (personCard != null) {
                res = card;
                break;
            }
        }
        return res;
    }


    private void automaticMerge() {

        for (Map.Entry<PersonId, PersonCard> entry : fullList.getPersons().entrySet()) {
            PersonCard card = entry.getValue();
            linkList.put(card.getPersonLink(), new MergedPersonCard(card));
        }

        for (Map.Entry<SocialLink, MergedPersonCard> entry : linkList.entrySet()) {
            SocialLink link = entry.getKey();
            MergedPersonCard card = entry.getValue();

            Collection<SocialLink> links = card.get(link.getLinkType()).getSocialLinks().values();

            for (SocialLink link1 : links) {
                MergedPersonCard card1 = linkList.get(link1);
                if (card1 != null) {
                    card.add(card1);
                    linkList.remove(link1);
                    linkList.put(link1, card);
                }
            }
        }


        for (Map.Entry<SocialLink, MergedPersonCard> entry : linkList.entrySet()) {
            MergedPersonCard card = entry.getValue();
            if (!mergedList.contains(card)) {
                card.cleanLinkMap();
                mergedList.add(card);
            }
        }

//        for (PersonCard card : fullList.getPersons().values()) {
//
//            PersonCard mergedCard = card.copy();
//
//            for (PersonCard targetCard : fullList.getPersons().values()) {
//                if (targetCard != card) {
//                    for (SocialLink link : card.getSocialLinks().values()) {
//                        if (link.getLinkType() == targetCard.getPersonLink().getLinkType()
//                                && link.getId().equalsIgnoreCase(targetCard.getPersonLink().getId())) {
//                            if (!card.isLinkedWith(targetCard)) {
//                                card.linkWith(targetCard);
//                                targetCard.linkWith(card);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }


    public static void main(String[] args) throws InterruptedException {
        ContentProvider cp = new ContentProvider(null);
        cp.request(new PersonCard(), false);
    }
}
