import DB.User;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import messages.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML ListView<String> listViewClient;
    @FXML ListView<String> listViewServer;
    @FXML TextField tfDownload;
    @FXML TextField tfUpload;
    @FXML Button downloadBtn;
    @FXML TextArea taField;

    private final String CONSOLE = "Console: ";
    final User[] loggedUser = {null};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof UserRequest) {
                        UserRequest ur = (UserRequest) am;
                        loggedUser[0] = ur.getUser();
                        //refreshFilesList(loggedUser[0].clientStorageDirectory, listViewClient);
                        //refreshFilesList(loggedUser[0].serverStorageDirectory, listViewServer);
                    }
                    if (am instanceof FileMessage) {
                        System.out.println("I am controller");
                        FileMessage fm = (FileMessage) am;

                        Files.write(Paths.get(fm.getUser().clientStorageDirectory + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        //refreshFilesList(fm.getUser().clientStorageDirectory, listViewClient);
                        //refreshFilesList(fm.getUser().serverStorageDirectory, listViewServer);
                        taField.setText(CONSOLE + "file '" + fm.getFilename() + "' downloaded");
                    }
                    if (am instanceof FilePush) {
                        FilePush fp = (FilePush) am;
                        if (fp.isResult())
                            taField.setText(CONSOLE + " file '" + ((FilePush) am).getFilename() + "' has been written");
                        else taField.setText(CONSOLE + " file '" + ((FilePush) am).getFilename() + "' is on cloud already");
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
        listViewClient.setItems(FXCollections.observableArrayList());
    }

    public void pressOnUploadBtn(ActionEvent actionEvent) throws IOException {
        if (tfUpload.getLength() > 0) {
            if (Files.exists(Paths.get(loggedUser[0].clientStorageDirectory + tfUpload.getText()))) {
                FilePush fp = new FilePush(Paths.get(loggedUser[0].clientStorageDirectory + tfUpload.getText()), loggedUser[0]);
                Network.sendMsg(fp);
            } else {
                taField.setText(CONSOLE + " file not exist");
            }
            //Network.sendMsg(new FilePush(tfUpload.getText()));
            tfUpload.clear();
        }
    }

    public void pressOnDownloadBtn(ActionEvent actionEvent) {
        if (tfDownload.getLength() > 0) {
            Network.sendMsg(new FileRequest(tfDownload.getText()));
            tfDownload.clear();
        }
    }

    public void pressOnUpdateBtn(ActionEvent actionEvent) {
        //refreshLocalFilesList();
        //refreshServerFilesList();
    }

 /*   public void refreshFilesList(String directory, ListView listView) {
        if (Platform.isFxApplicationThread()) {
            try {
                listViewClient.getItems().clear();
                Files.list(Paths.get(directory)).map(p -> p.getFileName().toString()).forEach(o -> listViewClient.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            refresh(listView, directory);
        }
    }

    private void refresh(ListView<String> input, String path) {
        Platform.runLater(() -> {
            try {
                input.getItems().clear();
                Files.list(Paths.get(path)).map(p -> p.getFileName().toString()).forEach(o -> input.getItems().add(o));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }*/
}
