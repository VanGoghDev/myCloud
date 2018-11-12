import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import messages.AbstractMessage;
import messages.MyMessage;
import messages.SignInMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML TextField loginField;
    @FXML TextField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof MyMessage) {
                        MyMessage mm = (MyMessage) am;
                        System.out.println(mm.getText());
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void signIn(ActionEvent event) {
        if (loginField.getLength() > 0 && passwordField.getLength() > 0) {
            Network.sendMsg(new SignInMessage(loginField.getText(), passwordField.getText()));
        }
    }

    public void openSignUp(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Sign Up");
        stage.initModality(Modality.APPLICATION_MODAL);
        loadNewWindow(stage, "/SignUpForm.fxml");
    }


    protected void loadNewWindow(Stage stage, String form) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(form));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
