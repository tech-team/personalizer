package content.source;

import content.ContentData;

public interface ContentSource {
    ContentData retrieve(ContentData data);
}
