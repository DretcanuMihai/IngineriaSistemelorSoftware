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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminController {

    public TableView<User> userTableView;
    public TableColumn<User, String> userUsernameColumn;
    public TableColumn<User, String> userPasswordColumn;
    public TableColumn<User, User.UserRole> userRoleColumn;
    public TableColumn<User, Boolean> userActivationColumn;

    private ObservableList<User> users;

    private SuperService service;
    private Stage stage;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initializeController() {
        users = FXCollections.observableArrayList();
        userTableView.setItems(users);
        userUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userActivationColumn.setCellValueFactory(new PropertyValueFactory<>("activated"));
        users.addAll(service.getAllUsers());
    }

    public void onAddButtonClick() {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("add_user.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root));

            AddUserController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            ctrl.initializeController();

            newStage.show();
            this.stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpdateButtonClick() {
        try {
            User selectedUser=new User("","",true, User.UserRole.Tester);
            int selected = userTableView.getSelectionModel().getSelectedIndex();
            if (selected >= 0)
                selectedUser = users.get(selected);
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("update_user.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root));

            UpdateUserController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            ctrl.setUser(selectedUser);
            ctrl.initializeController();

            newStage.show();
            this.stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivateDeactivateButtonClick() {
        try {
            int selected = userTableView.getSelectionModel().getSelectedIndex();
            if (selected < 0)
                throw new ServiceException("No user selected;");
            User selectedUser = users.get(selected);
            boolean activated = selectedUser.getActivated();
            if (activated)
                service.deactivateUser(selectedUser.getUsername());
            else
                service.activateUser(selectedUser.getUsername());
            selectedUser.setActivated(!activated);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "User updated successfully;");
            alert.showAndWait();
            userTableView.refresh();
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLogoutButtonClick() {
        try {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("login.fxml"));
            Parent root = loader.load();
            newStage.setScene(new Scene(root));

            LoginController ctrl = loader.getController();
            ctrl.setService(service);
            ctrl.setStage(newStage);
            ctrl.initializeController();

            newStage.show();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
