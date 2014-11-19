package content.source.linkedin;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;

public class LinkedIn implements ContentSource {


    @Override
    public void retrieve(PersonCard card, PersonList dest) {
        String name = card.getName();
        String lastName = card.getSurname();

    }

    @Override
    public Type getType() {
        return Type.LINKED_IN;
    }
}
