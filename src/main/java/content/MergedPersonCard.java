package content;

import java.util.EnumMap;
import java.util.Map;

public class MergedPersonCard {
    private Map<SocialLink.LinkType, PersonCard> linkedPersons = new EnumMap<>(SocialLink.LinkType.class);

    public MergedPersonCard(PersonCard initialCard) {
        add(initialCard);
    }

    public MergedPersonCard() {
    }

    public void add(PersonCard card) {
        linkedPersons.put(card.getPersonLink().getLinkType(), card);
    }

    public void add(MergedPersonCard card) {
        for (PersonCard card2 : card.linkedPersons.values()) {
            linkedPersons.put(card2.getPersonLink().getLinkType(), card2);
        }
    }

    public PersonCard get(SocialLink.LinkType linkType) {
        return linkedPersons.get(linkType);
    }
}
