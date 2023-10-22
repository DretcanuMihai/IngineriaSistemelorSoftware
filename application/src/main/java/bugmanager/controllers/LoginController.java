package bugmanager.controllers;

import bugmanager.model.User;
import bugmanager.service.ServiceException;
import bugmanager.service.SuperService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    private SuperService service;
    private Stage stage;


    public void setService(SuperService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initializeController(){

    }
    public void onLoginButtonClick() {
        try{
            String username=usernameTextField.getText();
            String password=passwordTextField.getText();
            User user=service.login(username,password);
            User.UserRole role=user.getRole();
            if(role== User.UserRole.Admin){
                Stage newStage=new Stage();
                FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("main_admin.fxml"));
                Parent root=loader.load();
                newStage.setScene(new Scene(root));


                AdminController ctrl = loader.getController();
                ctrl.setService(service);
                ctrl.setStage(newStage);
                ctrl.initializeController();

                newStage.show();
                this.stage.close();
            }else{
                Stage newStage=new Stage();
                FXMLLoader loader = new FXMLLoader(AdminController.class.getResource("main_specialist.fxml"));
                Parent root=loader.load();
                newStage.setScene(new Scene(root));


                SpecialistController ctrl = loader.getController();
                ctrl.setService(service);
                ctrl.setStage(newStage);
                ctrl.setRole(role);
                ctrl.initializeController();

                newStage.show();
                this.stage.close();
            }

        }catch(ServiceException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
