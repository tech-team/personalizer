package content;

import content.source.ContentSource;

import java.util.*;

public class PersonList {
    private ContentSource.Type type;
    private Map<PersonId, PersonCard> persons = new TreeMap<>(PersonId.COMPARATOR);


    private static class IdGenerator {
        private static Map<ContentSource.Type, Integer> currentIds = new EnumMap<>(ContentSource.Type.class);
        public static int generate(ContentSource.Type type) {
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

    private PersonId generateNewId() {
        return new PersonId(this.type, IdGenerator.generate(this.type));
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

    public void remove(PersonId id) {
        persons.remove(id);
    }

    public void addAll(Map<PersonId, PersonCard> persons) {
        this.persons.putAll(persons);
    }

    public PersonCard getPerson(PersonId id) {
        return persons.get(id);
    }
}
