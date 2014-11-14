package content;

import content.source.ContentSource;

import java.util.*;

public class PersonList implements Iterable<PersonCard> {
    private ContentSource.Type type;
//    private PersonCard initialCard = null;
    private List<PersonCard> persons = new LinkedList<>();


    private static class IdGenerator {
        private Map<ContentSource.Type, Integer> currentIds = new EnumMap<>(ContentSource.Type.class);
        public int generate(ContentSource.Type type) {
            Integer id = currentIds.get(type);
            ++id;
            currentIds.put(type, id);
            return id;
        }
    }

    private static IdGenerator generator = new IdGenerator();
    private PersonId generateNewId() {
        return new PersonId(this.type, generator.generate(this.type));
    }

    public PersonList(ContentSource.Type type) {
        this.type = type;
    }

    public PersonList() {
        this.type = ContentSource.Type.NONE;
    }

    public ContentSource.Type getType() {
        return type;
    }

    public PersonCard newPerson() {
//        if (initialCard == null) {
//            throw new RuntimeException("Initial card is not set");
//        }
        PersonCard p = new PersonCard(generateNewId());
        persons.add(p);
        return p;
    }

    public PersonCard addPerson(PersonCard p) {
//        if (initialCard == null) {
//            throw new RuntimeException("Initial card is not set");
//        }
        p.setId(generateNewId());
        persons.add(p);
        return p;
    }

//    public void setInitial(PersonCard p) {
//        initialCard = p;
//    }
//
//    public PersonCard getInitial() {
//        return initialCard;
//    }


    @Override
    public Iterator<PersonCard> iterator() {
        return persons.iterator();
    }
}
