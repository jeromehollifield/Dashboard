/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import classess.Connectivity;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author User
 */
public class TurnbackEntryController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXComboBox<String> turnbackCategory;
    @FXML
    private JFXTextArea turnbackDescription;
    @FXML
    private JFXComboBox<String> turnbackType;
    @FXML
    private JFXButton submitButton;
    @FXML
    private JFXButton clearButton;
    private ArrayList<JFXComboBox> boxarr = new ArrayList<>();
    private String loginID = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Enter all the turnback categories
        this.turnbackCategory.getItems().addAll("Computer Issues", "Priority Change",
                "Parts Issues", "Document Issue", "Equipment Setup", "Other");

        boxarr.add(this.turnbackCategory);
        boxarr.add(this.turnbackType);
        this.clearButton.setOnAction(e -> {
            clearButtonClicked();
        });
        this.turnbackCategory.setOnAction(e -> {
            menuChanged();
            populateType();
        });
        this.turnbackType.setOnAction(e -> {
            menuChanged();
        });

    }

    public void populateType() {
        if (this.turnbackCategory.getValue() == "Computer Issues") {
            this.turnbackType.getItems().clear();
            this.turnbackType.getItems().addAll("AutoTime Issues", "SAP Issues", "TeamCenter Issues", "Other");
        } else if (this.turnbackCategory.getValue() == "Priority Change") {
            this.turnbackType.getItems().clear();
            this.turnbackType.getItems().addAll("Interrupted", "Plan Change", "Switch Jobs", "Other");
        } else if (this.turnbackCategory.getValue() == "Parts Issues") {
            this.turnbackType.getItems().clear();
            this.turnbackType.getItems().addAll("Damaged Parts", "Missing Parts", "Extra Parts", "Other");
        } else if (this.turnbackCategory.getValue() == "Document Issue") {
            this.turnbackType.getItems().clear();
            this.turnbackType.getItems().addAll("Drawing Issue",
                    "Missing Sign off", "Instruction Ignored", "QN Left Open",
                    "DataSheet Incomplete", "Instruction issue", "Other");
        } else if (this.turnbackCategory.getValue() == "Equipment Setup") {
            this.turnbackType.getItems().clear();
            this.turnbackType.getItems().addAll("Setup Issue", "Testset Error",
                    "Equipment Missing", "Equipment being used",
                    "Equipment Broken", "Other");
        } else {
            this.turnbackType.getItems().clear();
            this.turnbackType.getItems().addAll("Other");
        }
    }

    public void initData(String loginID) {
        this.loginID = loginID;
    }

    //returns to login
    public void returnToLogin(ActionEvent event) throws IOException {
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
        Scene Scene = new Scene(Parent);

        //this is to ge the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }

    //action to take when the menu changes
    public void menuChanged() {
        if (this.turnbackCategory.isFocused()) {
            changeOpacity(this.turnbackCategory);
        } else if (this.turnbackType.isFocused()) {
            changeOpacity(this.turnbackType);
        }
    }

    //changes the opacity of obj 
    public void changeOpacity(JFXComboBox menu) {
        if (menu.getSelectionModel().isEmpty()) {
            menu.setStyle("-fx-opacity:0;");
        } else {
            menu.setStyle("-fx-opacity:1");
        }
    }

    public void submitClicked(ActionEvent event) throws Exception {
        //build a turnback entry object out of the controllers
        Connectivity connection = new Connectivity();
        if (fieldsEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fields empty!");
            alert.setHeaderText("Fields can't be empty!");

            alert.showAndWait();
        } else {
            int turnbackCategoryID = connection.getTurnbackCategoryID(this.turnbackCategory.getValue());
            int turnbackTypeID = connection.getTurnbackTypeID(this.turnbackType.getValue());
            int userID = connection.getUserID(this.loginID);
            String departmentName = connection.getDepartmentName(this.loginID);
            int departmentID = connection.getDepartmentID(departmentName);
            String turnbackText = this.turnbackDescription.getText();

            Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
            Scene Scene = new Scene(Parent);

            //this is to ge the stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(Scene);
            window.show();

            if (connection.addTurnbackToDB(userID, departmentID,
                    turnbackCategoryID, turnbackTypeID, turnbackText)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Turnback Successful");
                alert.setHeaderText("Turnback Submitted");

                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Turnback not submitted");

                alert.showAndWait();
            }

        }

    }

    //checks if any of the fields are emptyCat
    public boolean fieldsEmpty() {
        boolean isEmpty = false;
        //check if any field is empty

        for (JFXComboBox val : this.boxarr) {
            if (val.getItems().isEmpty()) {
                isEmpty = true;
            }
        }

        //now check the textarea
        if (this.turnbackDescription.getText().isEmpty()) {
            isEmpty = true;
        }

        return isEmpty;
    }

    public void clearButtonClicked() {
        //go through all the fields and clear them
        //clear the textarea
        this.turnbackDescription.clear();
        this.turnbackCategory.getSelectionModel().clearSelection();
        this.turnbackType.getSelectionModel().clearSelection();

    }

}
