package content;

import java.util.Collection;
import java.util.List;

public interface IContentProvider {
    void request(PersonCard request, boolean autoMerge) throws InterruptedException;
    void remove(List<PersonId> ids) throws InterruptedException;
    void merge(Collection<PersonIdsTuple> tuples) throws InterruptedException;
}
