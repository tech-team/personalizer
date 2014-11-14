package content.source.fb;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;

public class Facebook implements ContentSource {

    @Override
    public PersonList retrieve(PersonCard data) {
        return null;
    }

    @Override
    public Type getType() {
        return Type.FB;
    }
}
