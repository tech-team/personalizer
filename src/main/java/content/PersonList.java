package content;

import content.source.ContentSource;

import java.util.*;

public class PersonList {
    private ContentSource.Type type;
    private Map<PersonId, PersonCard> persons = new TreeMap<>(PersonId.COMPARATOR);


    private static class IdGenerator {
        private Map<ContentSource.Type, Integer> currentIds = new EnumMap<>(ContentSource.Type.class);
        public int generate(ContentSource.Type type) {
            Integer id = currentIds.get(type);
            if (id != null) {
                ++id;
            } else {
                id = 1;
            }
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
        PersonId id = generateNewId();
        PersonCard p = new PersonCard(id);
        persons.put(id, p);
        return p;
    }

    public PersonCard addPerson(PersonCard p) {
        PersonId id = generateNewId();
        p.setId(id);
        persons.put(id, p);
        return p;
    }

    public Map<PersonId, PersonCard> getPersons() {
        return persons;
    }
}
