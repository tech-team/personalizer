package content.source.vk;

import content.PersonCard;
import content.PersonList;

public class VKTest {
        public static void main(String[] args) throws Exception {
            VK vk = new VK();
            vk.retrieve(new PersonCard(), new PersonList());
        }
}
