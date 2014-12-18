package content.source;

import content.PersonCard;
import content.PersonList;

public interface ContentSource {
    public static enum Type {
        NONE,
        MERGED,
        VK,
        FB,
        LINKED_IN
    }
    void retrieve(PersonCard card, PersonList dest);
    Type getType();
}
