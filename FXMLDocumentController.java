/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXPasswordField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
//REquired for changing scenes
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import classess.Connectivity;
import java.sql.Connection;
import javafx.scene.control.Alert;

/**
 *
 * @author Matt, Ryan, Jerome, Steven
 */
public class FXMLDocumentController implements Initializable {

    //create the controls that will be used in the login screen
    @FXML
    private JFXTextField userNameInput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private Hyperlink passwordRecoveryLink;
    @FXML
    private Hyperlink newUserLink;
    @FXML
    private Text feedbaclTextLabel;
    @FXML
    private JFXTextField temp = new JFXTextField();

    //this method will handle all the control settings
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userNameInput.setFocusColor(Paint.valueOf("#5892ef"));
        passwordInput.setFocusColor(Paint.valueOf("#5892ef"));
        feedbaclTextLabel.setText("");

    }

    //ACTION EVENT METHODS
    //if cancelled is clicked, then the password and username fields are cleared
    public void cancellClicked() {
        userNameInput.clear();
        passwordInput.clear();
        this.changeField(this.passwordInput.getText().length(), passwordInput);
        this.changeField(this.userNameInput.getText().length(), userNameInput);
    }

   
    public void loginClicked() {
        //read the values in the password and username fields
        String username = userNameInput.getText();
        String password = passwordInput.getText();

        if (username.length() == 0 || password.length() == 0) {
            feedbaclTextLabel.setText("One or more required fields is empty.");
            feedbaclTextLabel.setFill(Paint.valueOf("red"));
        } else {
            this.feedbaclTextLabel.setText("");

        }

    }

    //this will change the opacity of whatever input field is selected
    public void inputFilled() {
        //Check the focussed fields
        if (passwordInput.isFocused()) {
            changeField(passwordInput.getText().length(), passwordInput);
        }
        if (userNameInput.isFocused()) {
            changeField(userNameInput.getText().length(), userNameInput);
        }

    }

    //Method Overloading to deal with the different entry field objects
    public void changeField(int len, JFXTextField field) {
        if (len != 0) {
            field.setStyle("-fx-opacity:1;");
        } else {
            field.setStyle("-fx-opacity:.50;");
        }
    }

    public void changeField(int len, JFXPasswordField field) {
        if (len != 0) {
            field.setStyle("-fx-opacity:1;");
        } else {
            field.setStyle("-fx-opacity:.50;");
        }
    }

    public void forgotPassword(ActionEvent event) throws IOException {
        Parent newpasswordParent = FXMLLoader.load(getClass().getResource("/scenes/ForgotPassword.fxml"));
        Scene forgotPasswordScene = new Scene(newpasswordParent);

        //this is to get the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(forgotPasswordScene);
        window.show();
    }

    //this is the method that will handle changing the scene
    public void newUserScreen(ActionEvent event) throws IOException {
        Parent newUserParent = FXMLLoader.load(getClass().getResource("/scenes/NewUserScene1.fxml"));
        Scene newUserScene = new Scene(newUserParent);

        //this is to ge the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newUserScene);
        window.show();
    }

    public void newEmployeeScreen(ActionEvent event) throws IOException {
        //create an instance of the connectivity class
        Connectivity connection = new Connectivity();
        Connection dbConnection;
        //Check if User name exists. if it does, validate password
        if (connection.valueExists(this.userNameInput.getText(), "LoginID")) {
            //check if the user is locked out or not in the database
            if(connection.isLocked(this.userNameInput.getText())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This userName does not exist in the database");
                alert.showAndWait();
            }else{
            if (connection.verifyPassword(this.userNameInput.getText(), this.passwordInput.getText())) {
                if (connection.isEmployee(this.userNameInput.getText())) {

                    //This is where we change the scene and pass the newUser objec to it
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/scenes/EmployeeDashboard.fxml"));
                    //this will give us access and the ability to pass the object
                    Parent employeeDashboardParent = loader.load();

                    Scene newEmployeeDashboard = new Scene(employeeDashboardParent);

                    EmployeeDashboardController controller = loader.getController();
                    controller.initData(this.userNameInput.getText());

                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(newEmployeeDashboard);
                    window.show();


                } else {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/scenes/mainMenu.fxml"));
                    //this will give us access and the ability to pass the object
                    Parent loginParent = loader.load();

                    Scene mainMenuDashboard = new Scene(loginParent);

                    MainMenuController controller = loader.getController();
                    controller.initData(this.userNameInput.getText());

                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(mainMenuDashboard);
                    window.show();
                }

            } else {
                this.userNameInput.clear();
                this.passwordInput.clear();
                connection.alertInvalidPassword();
            }
            }

        } else {
            System.out.println("You don't have an account");
            connection.alertInvalidEmail();
        }

    }

}
