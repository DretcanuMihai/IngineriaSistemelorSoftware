package bugmanager.controllers;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateUserController {
    public TextField passwordTextField;
    public ChoiceBox<User.UserRole> roleChoiceBox;
    public TextField usernameTextField;

    private SuperService service;
    private Stage stage;


    public void setService(SuperService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.usernameTextField.setText(user.getUsername());
        this.passwordTextField.setText(user.getPassword());
        this.roleChoiceBox.setValue(user.getRole());
    }

    public void initializeController() {
        ObservableList<User.UserRole> choices = FXCollections.observableArrayList();
        choices.add(User.UserRole.Tester);
        choices.add(User.UserRole.Programmer);
        roleChoiceBox.setItems(choices);
    }

    public void onUpdateButtonClick() {
        try {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            User.UserRole role = roleChoiceBox.getValue();
            service.updateUser(username, password, role);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "User updated successfully;");
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
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("main_admin.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root));


            AdminController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            ctrl.initializeController();

            newStage.show();
            this.stage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
