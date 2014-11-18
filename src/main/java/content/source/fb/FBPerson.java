package content.source.fb;

/**
 * Created by Solid on 18.11.2014.
 */
public class FBPerson {
    public static final String fields =
            "photo_160";

    private String firstName;
    private String lastName;
    private String name;
    private String photo;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FBPerson() {

    }
}
