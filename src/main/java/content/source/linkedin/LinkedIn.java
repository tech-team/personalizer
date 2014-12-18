package content.source.linkedin;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;
import util.net.HttpResponse;

import java.util.LinkedList;
import java.util.List;

public class LinkedIn implements ContentSource {


    @Override
    public void retrieve(PersonCard card, PersonList dest) {
    }

    @Override
    public Type getType() {
        return Type.LINKED_IN;
    }
}
