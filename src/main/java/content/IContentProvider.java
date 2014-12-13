package content;

public interface IContentProvider {
    void request(PersonCard request, boolean autoMerge) throws InterruptedException;
    void remove(PersonId[] ids) throws InterruptedException;
    void merge(PersonIdsTuple tuple) throws InterruptedException;
}
