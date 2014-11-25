package content.source.linkedin;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;
import util.net.HttpResponse;

import java.util.LinkedList;
import java.util.List;

public class LinkedIn implements ContentSource {


    @Override
    public void retrieve(PersonCard card, PersonList dest) {
        LinkedInRequest request = new LinkedInRequest();
        HttpResponse response = request.makeLoginRequest();
        Cookie cookie = new Cookie(response.getHeaders().getHeader("Set-Cookie"));
        request.makeHeaders(cookie.getCookie());
        List<String> urls = Parser.getPersonUrls(request.makeFindRequest(card.getName(), card.getSurname()));
        List<LinkedInPerson> persons = new LinkedList<>();
        for(String url: urls) {
            System.out.println(url);
            LinkedInPerson person = Parser.getPersonByLink(request.makePersonRequest(url));
            persons.add(person);
        }
    }

    @Override
    public Type getType() {
        return Type.LINKED_IN;
    }
}
