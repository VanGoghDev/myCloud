package messages;

public class SignUpMessage extends AbstractMessage{

    private String login;
    private String password;
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public SignUpMessage(String name, int age, String login, String password) {

        this.name = name;
        this.age = age;
        this.login = login;
        this.password = password;
    }
}
