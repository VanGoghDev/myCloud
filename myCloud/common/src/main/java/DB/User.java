package DB;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class User implements Serializable {

    int id;
    String name;
    int age;
    public String login;
    String password;
    public String serverStorageDirectory = "storage/server_storage_";
    public String clientStorageDirectory = "storage/client_storage_";

    public User(int id, String name, int age, String login, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.login = login;
        this.password = password;
        this.serverStorageDirectory += this.login + "/";
        this.clientStorageDirectory += this.login + "/";
    }

    @Override
    public String toString() {
        return "catalog.User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", login=" + login +
                ", password= " + password +
                '}';
    }


    public boolean isServerStorageDirectoryExists() {
        return Files.exists(Paths.get(serverStorageDirectory + this.login));
    }

    public boolean isClientStorageDirectoryExists() {
        return Files.exists(Paths.get(serverStorageDirectory + this.login));
    }
}
