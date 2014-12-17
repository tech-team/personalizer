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

    private LinkType linkType;
    private String id;
    private String url;

    public SocialLink(LinkType linkType, String id, String url) {
        this.linkType = linkType;
        this.id = id;
        this.url = url;
    }

    public SocialLink(LinkType linkType, String id) {
        this.linkType = linkType;
        this.id = id;
        this.url = ""; //TODO
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
