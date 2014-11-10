package content.source.vk;

import content.Persons;

public class VKTest {
        public static void main(String[] args) throws Exception {
            VK vk = new VK();
            vk.retrieve(new Persons());
        }
}
