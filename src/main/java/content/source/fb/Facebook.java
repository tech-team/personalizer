package content.source.fb;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;

public class Facebook implements ContentSource {


    @Override
    public void retrieve(PersonCard card, PersonList dest) {

    }

    @Override
    public Type getType() {
        return Type.FB;
    }
}
