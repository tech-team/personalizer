package content.source;

import content.PersonCard;
import content.Persons;

public interface ContentSource {
    Persons retrieve(Persons data);
    Persons retrieve(PersonCard card);
}
