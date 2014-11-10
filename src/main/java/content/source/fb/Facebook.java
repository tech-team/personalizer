package content.source.fb;

import content.PersonCard;
import content.Persons;
import content.source.ContentSource;

public class Facebook implements ContentSource {
    @Override
    public Persons retrieve(Persons data) {
        return null;
    }

    @Override
    public Persons retrieve(PersonCard data) {
        return null;
    }
}
