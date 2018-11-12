package DB;

public class LaunchDB {

    public static void main(String[] args) {
        BDApp app = new BDApp();
        //app.createDB();
        //app.clearTable();
        //app.insert(new User(1, "Kirill", 421, "ll", "lo"));
        System.out.println(app.isRegistered("ll"));
    }
}
