import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import messages.AbstractMessage;
import messages.FileMessage;
import messages.FileRequest;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                        refreshServerFilesList();
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
        refreshLocalFilesList();
        refreshServerFilesList();
    }

    public void pressOnUploadBtn(ActionEvent actionEvent) {

    }

    public void pressOnDownloadBtn(ActionEvent actionEvent) {
        if (tfDownload.getLength() > 0) {
            Network.sendMsg(new FileRequest(tfDownload.getText()));
            tfDownload.clear();
        }
    }

    public void refreshLocalFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                listViewClient.getItems().clear();
                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> listViewClient.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            refresh(listViewClient, "client");
        }
    }

    public void refreshServerFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                listViewServer.getItems().clear();
                Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> listViewServer.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            refresh(listViewServer, "server");
        }
    }

    private void refresh(ListView<String> input, String path) {
        Platform.runLater(() -> {
            try {
                input.getItems().clear();
                Files.list(Paths.get(path + "_storage")).map(p -> p.getFileName().toString()).forEach(o -> input.getItems().add(o));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
