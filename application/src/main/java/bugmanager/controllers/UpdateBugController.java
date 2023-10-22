package bugmanager.controllers;

import bugmanager.model.Bug;
import bugmanager.model.User;
import bugmanager.service.ServiceException;
import bugmanager.service.SuperService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateBugController {
    public ChoiceBox<Bug.BugStatus> statusChoiceBox;
    public TextArea descriptionTextArea;
    public TextField nameTextField;
    public TextField idTextField;

    private User.UserRole role;

    private SuperService service;
    private Stage stage;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBug(Bug bug) {
        this.idTextField.setText(bug.getId().toString());
        this.nameTextField.setText(bug.getName());
        this.descriptionTextArea.setText(bug.getDescription());
        this.statusChoiceBox.setValue(bug.getStatus());
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }

    public void initializeController() {
        ObservableList<Bug.BugStatus> choices = FXCollections.observableArrayList();
        choices.add(Bug.BugStatus.WaitingFeedback);
        choices.add(Bug.BugStatus.WaitingRetest);
        statusChoiceBox.setItems(choices);
    }
    public void onUpdateButtonClick() {
        try {
            int bugId;
            try{
                bugId=Integer.parseInt(idTextField.getText());
            }catch(Exception e){
                throw new ServiceException("Bug Id must be an integer;\n");
            }
            String name = nameTextField.getText();
            String description = descriptionTextArea.getText();
            Bug.BugStatus status = statusChoiceBox.getValue();
            service.updateBug(bugId,name,description,status);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bug updated successfully;");
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
            ctrl.setRole(role);
            ctrl.initializeController();

            newStage.show();
            this.stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
