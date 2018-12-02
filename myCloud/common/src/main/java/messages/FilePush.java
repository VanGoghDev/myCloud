package messages;

import DB.User;
import javafx.scene.Parent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePush extends AbstractMessage{
    private String filename;
    private byte[] data;
    User user;

    private boolean result = false;

    public FilePush(Path path, User user) throws IOException {
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
        this.user = user;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilename() {
        return filename;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public User getUser() {
        return user;
    }
}
