package server;

import content.MergedPersonCard;
import content.PersonCard;
import content.PersonList;

import java.util.Collection;

public interface ContentReceiver {
    void postPersonCard(PersonCard card);
    void postPersonList(PersonList list);
    void postResults(Collection<MergedPersonCard> list);

    void onFinishedListsRetrieval();
    void onFinishedAutomaticMerge();
    void onFinishedRemoval();
    void onFinishedMerge();
}
