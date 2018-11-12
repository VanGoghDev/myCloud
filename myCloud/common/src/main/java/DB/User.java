package DB;

public class User {

    int id;
    String name;
    int age;
    String login;
    String password;

    public User(int id, String name, int age, String login, String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.login = login;
        this.password = password;
    }

    @Override
    public String toString() {
        return "catalog.User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + login +
                '}';
    }
}
