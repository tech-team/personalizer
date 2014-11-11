package content;


public class SocialLink {
    public static enum LinkType {
        NONE,
        VK,
        FB,
        LINKED_ID
    }

    private String id;

    public SocialLink(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
