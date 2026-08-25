package data;

public class UserRequest {
    private String name;
    private String email;

    public UserRequest(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }
}
