import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messages.MyMessage;
import messages.SignUpMessage;

public class SignUpController extends LoginController {

    @FXML TextField nameField;
    @FXML TextField ageField;
    @FXML TextField loginField;
    @FXML TextField passwordField;
    @FXML Button confirm;

    public void signUp(ActionEvent event) {
        if (nameField.getLength() > 0 && ageField.getLength() > 0 && loginField.getLength() > 0 && passwordField.getLength() > 0) {

            Network.sendMsg(new SignUpMessage(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    loginField.getText(),
                    passwordField.getText()));

        }
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();
    }
}
