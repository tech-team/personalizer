package content.source.fb;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;
import java.io.IOException;

public class Facebook implements ContentSource {

    @Override
    public void retrieve(PersonCard personCard, PersonList dest) {

        FBRequest request = new FBRequest();
        try {
            request.search(personCard.getName() + " " + personCard.getSurname(), dest);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Type getType() {
        return Type.FB;
    }

    public static void main(String[] args) {
        PersonCard personCard = new PersonCard();
        personCard.setName("maxim");
        personCard.setSurname("myalkin");
        new Facebook().retrieve(personCard, new PersonList());
    }
}
