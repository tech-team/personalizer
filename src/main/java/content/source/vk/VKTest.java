package content.source.vk;

import content.PersonCard;
import content.PersonList;
import content.source.University;

public class VKTest {
        public static void main(String[] args) throws Exception {
            VK vk = new VK();
            PersonCard personCard = new PersonCard();
            personCard.setName("Игорь");
            personCard.setSurname("Латкин");
            personCard.setCountry("Россия");
            personCard.setCity("Москва");
            //personCard.setUniversities(new University("МГТУ им"))
            vk.retrieve(personCard, new PersonList());
        }
}
