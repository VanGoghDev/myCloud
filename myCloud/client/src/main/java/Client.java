import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/LoginForm.fxml"));
        primaryStage.setTitle("login");
        primaryStage.setScene(new Scene(root, 500, 150));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public void run(String[] args) {
        launch(args);
    }
}
