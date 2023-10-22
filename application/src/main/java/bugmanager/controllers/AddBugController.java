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
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBugController {
    public TextField nameTextField;
    public ChoiceBox<Bug.BugStatus> statusChoiceBox;
    public TextArea descriptionTextArea;

    private SuperService service;
    private Stage stage;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initializeController() {
        ObservableList<Bug.BugStatus> choices = FXCollections.observableArrayList();
        choices.add(Bug.BugStatus.WaitingFeedback);
        choices.add(Bug.BugStatus.WaitingRetest);
        statusChoiceBox.setItems(choices);
    }
    public void onSubmitButtonClick() {
        try {
            String name = nameTextField.getText();
            String description = descriptionTextArea.getText();
            Bug.BugStatus status = statusChoiceBox.getValue();
            service.submitBug(name, description, status);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bug submitted successfully;");
            alert.showAndWait();
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackButtonClick() {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("main_specialist.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root));


            SpecialistController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            ctrl.setRole(User.UserRole.Tester);
            ctrl.initializeController();

            newStage.show();
            this.stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
