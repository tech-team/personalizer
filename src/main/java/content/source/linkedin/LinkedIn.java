package content.source.linkedin;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;

public class LinkedIn implements ContentSource {

    @Override
    public PersonList retrieve(PersonCard data) {
        return null;
    }

    @Override
    public Type getType() {
        return Type.LINKED_IN;
    }
}
