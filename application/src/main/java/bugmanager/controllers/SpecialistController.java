package bugmanager.controllers;

import bugmanager.model.Bug;
import bugmanager.model.User;
import bugmanager.service.ServiceException;
import bugmanager.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpecialistController {

    public TableView<Bug> bugTableView;
    public TableColumn<Bug, Integer> bugIdColumn;
    public TableColumn<Bug, String> bugNameColumn;
    public TableColumn<Bug, String> bugDescriptionColumn;
    public TableColumn<Bug, Bug.BugStatus> bugStatusColumn;
    public TextArea descriptionTextArea;
    public Button actionButton;

    private ObservableList<Bug> bugs;
    private User.UserRole role;
    private SuperService service;
    private Stage stage;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }

    public void initializeController() {
        bugs = FXCollections.observableArrayList();
        bugTableView.setItems(bugs);
        bugIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bugNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bugDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        bugStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bugTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                descriptionTextArea.setText(newSelection.getDescription());
            }
        });
        bugs.addAll(service.getAllActiveBugs());
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        if (role == User.UserRole.Tester) {
            actionButton.setText("Submit Bug");
        } else {
            actionButton.setText("Solve Bug");
        }
    }

    public void onActionButtonClick() {
        try {
            if (role == User.UserRole.Programmer) {
                int selected = bugTableView.getSelectionModel().getSelectedIndex();
                if (selected < 0)
                    throw new ServiceException("No bug selected;\n");
                Bug selectedBug = bugs.get(selected);
                service.solveBug(selectedBug.getId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bug solved successfully;");
                alert.showAndWait();
                bugs.remove(selectedBug);
                bugTableView.refresh();
            } else {
                Stage newStage = new Stage();
                FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("add_bug.fxml"));
                Parent root = loader.load();
                newStage.setScene(new Scene(root));

                AddBugController ctrl = loader.getController();
                ctrl.setService(service);
                ctrl.setStage(newStage);
                ctrl.initializeController();

                newStage.show();
                this.stage.close();
            }
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpdateButtonClick() {
        try {
            Bug selectedBug=new Bug(0,"","", Bug.BugStatus.WaitingFeedback);
            int selected = bugTableView.getSelectionModel().getSelectedIndex();
            if (selected >= 0)
                selectedBug = bugs.get(selected);

            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("update_bug.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root));

            UpdateBugController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            ctrl.setBug(selectedBug);
            ctrl.setRole(role);
            ctrl.initializeController();

            newStage.show();
            this.stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLogoutButtonClick() {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("login.fxml"));
            Parent root = loader.load();

            LoginController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            newStage.setScene(new Scene(root));
            newStage.show();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
