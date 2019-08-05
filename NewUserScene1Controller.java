/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

//jfoenix library

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.paint.Paint;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import classess.InputValidator;
import classess.NewUser;
import classess.Connectivity;


/**
 * FXML Controller class
 *
 * @author User
 */
public class NewUserScene1Controller implements Initializable{
    
    
    //instances for the feedback text fields
    private ArrayList<JFXTextField> fieldGroup= new ArrayList<>();
    @FXML private JFXComboBox<String> roleSelection;
    @FXML private JFXComboBox<String> departmentSelection;
    @FXML private JFXTextField firstNameInput;
    @FXML private JFXTextField lastNameInput;
    @FXML private JFXTextField emailInput;
    @FXML private JFXTextField emailConfirmInput;
    
    //new instance of the newUser class
    private NewUser newUser;
    //instance of connectivity to test database value
    private Connectivity databaseConnection;
    //feedback fields
    private ArrayList<Text> feedbackGroup = new ArrayList<>();
    @FXML private Text firstNameFeedback;
    @FXML private Text lastNameFeedback;
    @FXML private Text emailFeedback;
    @FXML private Text roleSelectionFeedback;
    @FXML private Text departmentFeedback;
    //Hyperlink to return to the login screen
    @FXML private Hyperlink returnToLoginLink;
    //create the Button controls 
    @FXML private JFXButton nextButton;
    @FXML private JFXButton clearButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //add all text input fields to the arraylist
        this.fieldGroup.add(firstNameInput);
        this.fieldGroup.add(lastNameInput);
        this.fieldGroup.add(emailInput);
        this.fieldGroup.add(emailConfirmInput);
        
        //add all feedback labels to the arraylist
        this.feedbackGroup.add(firstNameFeedback);
        this.feedbackGroup.add(lastNameFeedback);
        this.feedbackGroup.add(emailFeedback);
        this.feedbackGroup.add(departmentFeedback);
        this.feedbackGroup.add(roleSelectionFeedback);
        //Iterate through the feedback group and set their text values to ""
        for(Text val: this.feedbackGroup){
            //set the fields to be empty
            val.setText("");
            //set the field colors to red
            val.setFill(Paint.valueOf("red"));
        }
        //iterate through the text input fields and set their focus color
        for(JFXTextField val: this.fieldGroup){
            val.setFocusColor(Paint.valueOf("#5892ef"));
        }
        //set the role items for the Combobox
        this.roleSelection.getItems().addAll("Manager","Employee");
        this.departmentSelection.getItems().addAll("E-Brakes","General Actuations",
                "Legacy", "Process Areas", "Tank Units");
        
        
       
        
                  
    }
    //this function is called by all the text input fields when the user clicks
    //on one.  It then determines whether the opacity needs to be changed or not
    //by calling the changeOpacity() method
    public void fieldsSelected(){
        //check which field is on focus
        //we have to check the first name, last name, email, employ ID, dept ID fields
        for(JFXTextField field: this.fieldGroup){
            if(field.isFocused()){
                changeOpacity(field.getText().length(),field);
            }
        }
    }
    //changes opacity for the JFXTextfields
    public void changeOpacity(int len,JFXTextField field){
        if(len !=0){
            field.setStyle("-fx-opacity: 1;");
        }
        else{
            field.setStyle("-fx-opacity:.50;");
        }
    }
    //changes opacity for the Combo box
    public void changeOpacity(JFXComboBox menu){
        if(menu.getSelectionModel().isEmpty()){
            menu.setStyle("-fx-opacity:.50;");
        }else{
            menu.setStyle("-fx-opacity:1;");
        }
    }
    //clears all the fields in the form
    public void clearButtonPressed(){
        //this will go through all the text input fields and clear them out
        //it will also make sure to change thier opacity accordingly
        this.fieldGroup.stream().map((val) -> {
            val.clear();
            return val;
        }).forEachOrdered((val) -> {
            changeOpacity(val.getText().length(),val);
        });
        //Secondly, reset the selection combobox
        roleSelection.getSelectionModel().clearSelection();
        roleSelection.setStyle("-fx-opacity:.50;");
        departmentSelection.getSelectionModel().clearSelection();
        departmentSelection.setStyle("-fx-opacity:.50;");
        
        //finally, run the loop through the feedback labels to clear them
        this.feedbackGroup.forEach((text) -> {
            text.setText("");
        });
    }
    //changes the opacity if the role menu has changed
    public void selectionChanged(){
        //see if the selection is empty
        if(!(roleSelection.getSelectionModel().isEmpty())){
            changeOpacity(roleSelection);
            this.roleSelectionFeedback.setText("");
        }
        if(!(departmentSelection.getSelectionModel().isEmpty())){
            changeOpacity(departmentSelection);
            this.departmentFeedback.setText("");
        }
    }
    
    
    
    //returns to login
    public void returnToLogin(ActionEvent event)throws IOException{
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    
    public void validateAndContinue(ActionEvent event)throws IOException{
        boolean validFields = true;
        //we are going to use the DataValidator class
        //we got 7 fields to validate so we are going to pass the number 7
        InputValidator dataValidator = new InputValidator();
        //validate the names
        dataValidator.validateName(this.firstNameInput.getText(),this.firstNameFeedback);
        dataValidator.validateName(this.lastNameInput.getText(), this.lastNameFeedback);
        dataValidator.validateEmails(this.emailInput.getText(), this.emailConfirmInput.getText(), this.emailFeedback);
        
        //next check the department dropbox
        if(this.departmentSelection.getSelectionModel().isEmpty()){
            this.departmentFeedback.setText("Required");
        }
        //check the role dropbox
        if(this.roleSelection.getSelectionModel().isEmpty()){
            this.roleSelectionFeedback.setText("Required");
        }
        
        /*
            The final check is to make sure that all the fields are valid. The 
            way we are goin to do this is by check all the feedback labels.  If 
            every feedback label is empty, then we know that the form is valid. We
            are going to use the arraylist of feedback labels created in this class.
        */
        
        for(Text label:this.feedbackGroup){
            if(label.getText().isEmpty()){
                continue;
            }else{
                validFields = false;
                break;
            }
        }
        //if the last iteration set the flag to true, we know all fields are valid
        if(validFields){
            
            /*
            Here we need to check that the email the user entered deosnt' already
            exist in the database.
            */
            this.databaseConnection = new Connectivity();
            if(this.databaseConnection.valueExists(this.emailInput.getText(), "Email")){
               /*
                Email was found in the database, so set feedback accordingly
                */
               this.emailFeedback.setText("Email already exists");
            }else{
            //now that all the fields are valid, we need to save all the values
            //into the newUser object instance created
            //create a new instance of the NewUser class
            newUser = new NewUser(this.firstNameInput.getText(),
            this.lastNameInput.getText(),this.emailInput.getText(),
            this.departmentSelection.getSelectionModel().getSelectedItem(),
            this.roleSelection.getSelectionModel().getSelectedItem());
            
            
            //This is where we change the scene and pass the newUser objec to it
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/scenes/NewUserScene2.fxml"));
            //this will give us access and the ability to pass the object
            Parent newUserScene2Parent = loader.load();
            
            Scene newUserScene2 = new Scene(newUserScene2Parent);
            
            NewUserScene2Controller controller = loader.getController();
            controller.initData(newUser);
            
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newUserScene2);
            window.show();
            }
            
        }
    
    
    
   
    
}
}
