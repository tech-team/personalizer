package content.source.vk;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class VK implements ContentSource {

//    public static final String AuthorizeURL =
//            "https://oauth.vk.com/authorize?client_id=4628886&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token";
//    private static Boolean isAuthorized = false;

    private VKUserSearcher searcher;

    public VK(){
        VKDataHelper.initCountries();
        VKConst.countries.put("Россия", 1);
        searcher = new VKUserSearcher();
    }

    @Override
    public void retrieve(PersonCard data, PersonList dest) {
        ArrayList<VKPerson> persons = searcher.getPersons(data);

        for (VKPerson person: persons){
            dest.addPerson(transformVKPersonToPersonCard(person, data));
        }
    }

    @Override
    public Type getType() {
        return Type.VK;
    }

    public PersonCard transformVKPersonToPersonCard(VKPerson person, PersonCard init){
        PersonCard personCard = new PersonCard();
//        if (init.getAgeFrom()) TODO выставить года, как в ините
        return personCard;
    }

}
