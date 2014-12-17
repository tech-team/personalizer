package content;

import content.source.ContentSource;

import java.util.*;

public class PersonIdsTuple {
    private List<PersonId> ids = new LinkedList<>();

    public PersonIdsTuple(List<PersonId> ids) {
        this.ids = ids;
    }
    public PersonIdsTuple(PersonId[] ids) {
        this.ids = Arrays.asList(ids);
    }


    public PersonIdsTuple() {
    }

    public PersonIdsTuple addId(PersonId id) {
        ids.add(id);
        return this;
    }

    public List<PersonId> getIds() {
        return ids;
    }
}
