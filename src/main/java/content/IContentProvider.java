package content;

import java.util.List;

public interface IContentProvider {
    void request(PersonCard request, boolean autoMerge) throws InterruptedException;
    void remove(List<PersonId> ids) throws InterruptedException;
    void merge(PersonIdsTuple tuple) throws InterruptedException;
}
