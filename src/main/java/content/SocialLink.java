package content;


public class SocialLink {
    public static enum LinkType {
        NONE,
        VK,
        FB,
        LINKED_ID,
        Skype,
        Twitter
    }

    private String id;

    private String url;

    public SocialLink(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
