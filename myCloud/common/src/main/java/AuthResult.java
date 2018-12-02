import DB.User;
import messages.AbstractMessage;

public class AuthResult extends AbstractMessage {
    public boolean loggedIn = false;

    public AuthResult(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public String toString() {
        return "AuthResult{" +
                "loggedIn=" + loggedIn +
                '}';
    }
}
