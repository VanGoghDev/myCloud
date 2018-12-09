package messages;

import DB.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage{
    private String filename;
    private byte[] data;
    private User user;

    public FileMessage(String filename, byte[] copyOfRange, User user) {
        this.filename = filename;
        data = copyOfRange;
        this.user = user;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path, User user) throws IOException {
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
