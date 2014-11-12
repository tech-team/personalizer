package content.source.vk;

import content.PersonCard;

public class VKTest {
        public static void main(String[] args) throws Exception {
            VK vk = new VK();
            vk.retrieve(new PersonCard());
        }
}
