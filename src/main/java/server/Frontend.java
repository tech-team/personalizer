package server;

import content.PersonCard;
import content.PersonList;

public interface Frontend {
    void postPersonCard(PersonCard card);
    void postPersonList(PersonList list);
    void postResults(PersonList list);
}
