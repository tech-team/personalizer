package content;

import content.source.ContentSource;
import server.Frontend;

import java.util.LinkedList;
import java.util.List;

public class ContentProvider implements IContentProvider {

    private Frontend frontend;
    private List<ContentSource> sources = new LinkedList<>();

    public ContentProvider(Frontend frontend) {
        this.frontend = frontend;
    }

    @Override
    public void request(Request request) {
//        return new PersonCard();
    }

    @Override
    public void remove(PersonId[] ids) {

    }

    @Override
    public void merge(PersonIdsTuple tuple) {

    }
}
