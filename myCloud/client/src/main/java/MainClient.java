import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("cloud");
        primaryStage.setScene(new Scene(root, 500, 550));
        primaryStage.show();
    }

    public void run(String[] args) {
        launch(args);
    }
}
