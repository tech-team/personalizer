package content;


public class University {
    private String name;
    private Integer graduation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGraduation() {
        return graduation;
    }

    public void setGraduation(Integer graduation) {
        this.graduation = graduation;
    }



    public University(String name){
        this.name = name;
        this.graduation = null;
    }

    public University(String name, Integer graduation){
        this.name = name;
        this.graduation = graduation;
    }
}
