package content;

import content.source.ContentSource;

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
}
