package content;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Persons implements Iterable<PersonCard> {
    private PersonCard initialCard = null;
    private List<PersonCard> persons = new LinkedList<>();
    private int currentPersonId = 1;


    public PersonCard newPerson() {
        if (initialCard == null) {
            throw new RuntimeException("Initial card is not set");
        }
        PersonCard p = new PersonCard(currentPersonId++);
        persons.add(p);
        return p;
    }

    public PersonCard addPerson(PersonCard p) {
        if (initialCard == null) {
            throw new RuntimeException("Initial card is not set");
        }
        p.setId(currentPersonId++);
        persons.add(p);
        return p;
    }

    public void setInitial(PersonCard p) {
        initialCard = p;
    }

    public PersonCard getInitial() {
        return initialCard;
    }


    @Override
    public Iterator<PersonCard> iterator() {
        return persons.iterator();
    }
}
