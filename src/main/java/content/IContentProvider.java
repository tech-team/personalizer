package content;

public interface IContentProvider {
    void request(Request request);
    void remove(PersonId[] ids);
    void merge(PersonIdsTuple tuple);
}
