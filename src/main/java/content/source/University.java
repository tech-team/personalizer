package content.source;


public class University {
    private String name;
    private String graduation;

    public University(String name){
        this.name = name;
        this.graduation = null;
    }

    public University(String name, String graduation){
        this.name = name;
        this.graduation = graduation;
    }

    public University(String name, Integer graduation){
        this.name = name;
        if (graduation != null)
            this.graduation = graduation.toString();
    }
}
