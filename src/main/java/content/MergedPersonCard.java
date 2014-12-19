package content;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

public class MergedPersonCard {
    private Map<SocialLink.LinkType, PersonCard> linkedPersons = new EnumMap<>(SocialLink.LinkType.class);
    private Map<PersonId, PersonCard> idToPersons = new TreeMap<>(PersonId.COMPARATOR);

    public MergedPersonCard(PersonCard initialCard) {
        add(initialCard);
    }

    public MergedPersonCard() {
    }

    public void add(PersonCard card) {
        linkedPersons.put(card.getPersonLink().getLinkType(), card);
        idToPersons.put(card.getId(), card);
    }

    public void add(MergedPersonCard card) {
        card.linkedPersons.values().forEach(this::add);
    }

    public PersonCard get(SocialLink.LinkType linkType) {
        return linkedPersons.get(linkType);
    }

    public PersonCard get(PersonId personId) {
        return idToPersons.get(personId);
    }

    public void cleanLinkMap() {
        linkedPersons = null;
    }

    public void addOnPersonId(MergedPersonCard card) {
        card.idToPersons.values().forEach(this::add);
    }

    public Collection<PersonCard> getList() {
        return idToPersons.values();
    }
}
