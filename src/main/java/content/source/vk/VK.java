package content.source.vk;

import content.PersonCard;
import content.PersonList;
import content.SocialLink;
import content.source.ContentSource;
import content.University;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        ArrayList<PersonCard> personCards = searcher.getPersons(data);
        for (PersonCard personCard: personCards){
            dest.addPerson(personCard);
        }
    }

    @Override
    public Type getType() {
        return Type.VK;
    }
}
