package messages;

import DB.User;

public class UserRequest extends AbstractMessage{
    private User user;

    public UserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
