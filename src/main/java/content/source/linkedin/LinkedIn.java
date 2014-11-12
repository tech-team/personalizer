package content.source.linkedin;

import content.PersonCard;
import content.Persons;
import content.source.ContentSource;
import org.json.JSONException;
import org.json.JSONObject;
import util.net.Headers;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;

public class LinkedIn implements ContentSource {

    public Persons retrieve(Persons data) {
        return null;
    }

    @Override
    public Persons retrieve(PersonCard data) {
        return null;
    }
}
