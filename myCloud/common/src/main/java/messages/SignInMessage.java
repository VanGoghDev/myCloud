package messages;

public class SignInMessage extends AbstractMessage {

    private String login;
    private String password;

    public SignInMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
