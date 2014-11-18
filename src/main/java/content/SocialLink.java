package content;


public class SocialLink {
    public static enum LinkType {
        NONE,
        VK,
        FB,
        LINKED_IN,
        SKYPE,
        TWITTER
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
