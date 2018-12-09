import DB.BDApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import messages.AbstractMessage;
import messages.SignInMessage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML TextField loginField;
    @FXML TextField passwordField;
    @FXML Button signInBtn;

    AbstractMessage am;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    am = Network.readObject();
                    if (am instanceof AuthResult) {
                        AuthResult ar = (AuthResult) am;
                        System.out.println(ar.toString());
                        if (ar.loggedIn) {
                            Platform.runLater(() -> {
                                try {
                                    closeSignIn();
                                    runMainApp();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            break;
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void runMainApp() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Client Cloud");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        loadNewWindow(stage, "/MainWindow.fxml");
    }

    public void signIn(ActionEvent event) throws IOException {
        if (loginField.getLength() > 0 && passwordField.getLength() > 0) {
            Network.sendMsg(new SignInMessage(loginField.getText(), passwordField.getText()));
            BDApp bdApp = new BDApp();
        } else {
            System.out.println("No data typed in");
        }
    }

    public void closeSignIn() {
        Stage stage = (Stage) signInBtn.getScene().getWindow();
        stage.close();
    }

    public void openSignUp(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Sign Up");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
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
