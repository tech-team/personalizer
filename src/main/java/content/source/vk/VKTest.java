package content.source.vk;

import content.PersonCard;
import content.PersonList;

public class VKTest {
        public static void main(String[] args) throws Exception {
            VK vk = new VK();
            PersonCard personCard = new PersonCard();
            personCard.setName("Женя");
            personCard.setSurname("Карташева");
            personCard.setCountry("Россия");
            personCard.setCity("Королев");
            //personCard.setUniversities(new University("МГТУ им"))
            vk.retrieve(personCard, new PersonList());
        }
}
