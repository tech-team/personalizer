package server;

import content.PersonCard;
import content.PersonList;

public interface ContentReceiver {
    void postPersonCard(PersonCard card);
    void postPersonList(PersonList list);
    void postResults(PersonList list);

    void onFinishedListsRetrieval();
    void onFinishedAutomaticMerge();
    void onFinishedRemoval();
    void onFinishedMerge();
}
