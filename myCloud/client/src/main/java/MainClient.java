import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        primaryStage.setTitle("cloud");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public void run(String[] args) {
        launch(args);
    }
}
