import DB.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import messages.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML Button openClientBtn;
    @FXML Button uploadClientBtn;
    @FXML Button deleteClientBtn;
    @FXML ListView<String> lwClient;
    @FXML TextArea taField;
    @FXML ListView<String> lwServer;
    @FXML Button updateBtn;

    private final User[] loggedUser = {null};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread t = new Thread( () -> {
          try {
              while (true) {
                  AbstractMessage am = Network.readObject();
                  if (am instanceof UserRequest) {
                      UserRequest ur = (UserRequest) am;
                      loggedUser[0] = ur.getUser();
                      refreshFilesList(loggedUser[0].clientStorageDirectory, lwClient);
                      refreshFilesList(loggedUser[0].serverStorageDirectory, lwServer);
                  } if (am instanceof FileMessage) {
                      FileMessage fm = (FileMessage) am;

                      Files.write(Paths.get(fm.getUser().clientStorageDirectory + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                      refreshFilesList(fm.getUser().clientStorageDirectory, lwClient);
                      refreshFilesList(loggedUser[0].serverStorageDirectory, lwServer);

                  } if (am instanceof FilePush) {
                      FilePush fp = (FilePush) am;
                      if (fp.isResult()) {
                          taField.setText("file '" + ((FilePush) am).getFilename() + "' has been written");
                      } else {
                          taField.setText("file '" + ((FilePush) am).getFilename() + "' is on cloud already");
                      }
                  }
              }
          } catch (ClassNotFoundException | IOException e) {
              e.printStackTrace();
          } finally {
              Network.stop();
          }
        }
        );
        t.setDaemon(true);
        t.start();
        lwClient.setItems(FXCollections.observableArrayList());
    }

    public void pressOnOpenBtn(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
            Files.write(Paths.get(loggedUser[0].clientStorageDirectory + selectedFile.getName()), fileContent, StandardOpenOption.CREATE);
            lwClient.getItems().add(selectedFile.getName());
        } else {
            taField.setText("file is not valid");
        }

    }

    public void pressOnUploadBtn(ActionEvent actionEvent) throws IOException {
        if (lwClient.getSelectionModel().getSelectedItem() != null) {
            if (Files.exists(Paths.get(loggedUser[0].clientStorageDirectory + lwClient.getSelectionModel().getSelectedItem()))) {

                String fileName = lwClient.getSelectionModel().getSelectedItem();
                if (loggedUser[0] != null) {
                    FilePush fp = new FilePush(Paths.get(loggedUser[0].clientStorageDirectory + fileName), loggedUser[0]);
                    Network.sendMsg(fp);
                } else {
                    taField.setText("User not found");
                }
                taField.setText("File '" + fileName + "' uploaded");
            } else {
                taField.setText("File do not exist");
                refreshFilesList(loggedUser[0].clientStorageDirectory, lwClient);
            }
        } else {
            taField.setText("Choose file from the list view, please...");
        }
    }

    public void pressOnDownloadBtn(ActionEvent actionEvent) {
        if (lwServer.getSelectionModel().getSelectedItem() != null) {
            String fileName = lwServer.getSelectionModel().getSelectedItem();
            if (loggedUser[0] != null) {
                Network.sendMsg(new FileRequest(fileName));
            } else {
                taField.setText("User not found");
            }
        } else {
            taField.setText("Choose file from the list view, please...");
        }
    }

    public void pressOnDeleteBtn(ActionEvent actionEvent) {
        if (lwClient.getSelectionModel().getSelectedItem() != null) {
            String fileName = getSelectionFromListView(lwClient);
            if (loggedUser[0] != null) {
                File file = new File(String.valueOf(Paths.get(loggedUser[0].clientStorageDirectory + fileName)));
                if (file.delete()) {
                    refreshFilesList(loggedUser[0].clientStorageDirectory, lwClient);
                    taField.setText("File deleted...");
                } else {
                    taField.setText("Something went wrong, try again...");
                }
            } else {
                taField.setText("User not found");
            }
        } else {
            taField.setText("Choose file from the list view, please...");
        }
    }

    public void pressOnUpdateBtn(ActionEvent actionEvent) {
        if (loggedUser[0] != null) {
            refreshFilesList(loggedUser[0].clientStorageDirectory, lwClient);
            refreshFilesList(loggedUser[0].serverStorageDirectory, lwServer);
        } else {
            taField.setText("User not found");
        }
    }

    public String getSelectionFromListView(ListView<String> listView) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            return listView.getSelectionModel().getSelectedItem();
        } else {
            return null;
        }

    }

    private void refreshFilesList(String directory, ListView listView) {
        if (Platform.isFxApplicationThread()) {
            try {
                listView.getItems().clear();
                Files.list(Paths.get(directory)).map(p -> p.getFileName().toString()).forEach(o -> listView.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            refresh(directory, listView);
        }
    }

    private void refresh(String path, ListView<String> input) {
        Platform.runLater(() -> {
            try {
                input.getItems().clear();
                Files.list(Paths.get(path)).map(p -> p.getFileName().toString()).forEach(o -> input.getItems().add(o));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
