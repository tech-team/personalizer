package content;

import content.source.ContentSource;

import java.util.EnumMap;
import java.util.Map;

public class PersonIdsTuple {
    private Map<ContentSource.Type, PersonId> ids = new EnumMap<>(ContentSource.Type.class);

    public PersonIdsTuple(Map<ContentSource.Type, PersonId> ids) {
        this.ids = ids;
    }

    public PersonIdsTuple() {
    }

    public PersonIdsTuple addId(ContentSource.Type type, PersonId id) {
        ids.put(type, id);
        return this;
    }
}
