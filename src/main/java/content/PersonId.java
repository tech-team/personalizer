package content;

import content.source.ContentSource;

import java.util.Comparator;

public class PersonId {
    private ContentSource.Type type;
    private int id;

    public PersonId(ContentSource.Type type, int id) {
        this.type = type;
        this.id = id;
    }

    public ContentSource.Type getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setType(ContentSource.Type type) {
        this.type = type;
    }

    public static final Comparator<PersonId> COMPARATOR = new Comparator<PersonId>() {
        @Override
        public int compare(PersonId o1, PersonId o2) {
            if (o1 == o2) {
                return 0;
            }
            return o1.getType().ordinal() * o1.id - o2.getType().ordinal() * o2.id;
        }
    };
}
