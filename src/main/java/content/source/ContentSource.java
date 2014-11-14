package content.source;

import content.PersonCard;
import content.PersonList;

public interface ContentSource {
    public static enum Type {
        NONE,
        VK,
        FB,
        LINKED_IN
    }
    PersonList retrieve(PersonCard card);
    Type getType();
}
