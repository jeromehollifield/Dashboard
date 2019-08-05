/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import classess.InputValidator;
import classess.NewUser;
import classess.Connectivity;

/**
 * FXML Controller class
 *
 * @author User
 */
public class NewUserScene2Controller implements Initializable {

    //class field member controllers 
    private NewUser newUser;
    @FXML private Hyperlink backToBeginingLink;
    @FXML private Text loginIDInputFeedback;
    @FXML private Text passwordInputFeedback;
    @FXML private Text loginInputGuidance;
    @FXML private Text passwordInputGuidance;
    @FXML private JFXTextField loginIDinput;
    @FXML private JFXTextField loginIDinputConfirmation;
    @FXML private JFXTextField passwordInput;
    @FXML private JFXTextField passwordInputConfirmation;
    private ArrayList<JFXTextField> inputGroup = new ArrayList<JFXTextField>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.loginIDInputFeedback.setFill(Paint.valueOf("red"));
        this.loginIDInputFeedback.setText("");
        this.passwordInputFeedback.setFill(Paint.valueOf("red"));
        this.passwordInputFeedback.setText("");
        this.loginIDinput.clear();
        this.loginIDinputConfirmation.clear();
        this.inputGroup.add(loginIDinput);
        this.inputGroup.add(loginIDinputConfirmation);
        this.inputGroup.add(passwordInput);
        this.inputGroup.add(passwordInputConfirmation);
        
    }    
    
    
    /*
    Function: backToNewUserScene1()
    Description: returns to the login screen
    Input:None
    Return: none
    */
    public void backToNewUserScene1(ActionEvent event)throws IOException{
        Parent newUserParent = FXMLLoader.load(getClass().getResource("/scenes/NewUserScene1.fxml"));
        Scene newUserScene = new Scene(newUserParent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(newUserScene);
        window.show();
    }
      /*
    Function: initData()
    Description: grabs the data passed by the previous scene and saves it
    Input:NewUser obj
    Return: none
    */
    public void initData(NewUser newUser){
        this.newUser = newUser;
        
    }
    
    public void inputFieldChanged(){
        for(JFXTextField input: inputGroup){
            if(input.isFocused()){
                changeFieldOpacity(input.getText().length(),input);
            }
        }
    }
    
    //changes opacity for the JFXTextfields
    public void changeFieldOpacity(int len,JFXTextField field){       
        if(field.getText().equals("")){
            field.setStyle("-fx-opacity:.50;");            
        }       
        else{
            field.setStyle("-fx-opacity:1;");
        }
    }
   
    
    public void submitClicked(ActionEvent event)throws IOException{
        //we are going to validate the data and add the username and password to
        //the object class
        InputValidator validator = new InputValidator();
        validator.validateLoginID(this.loginIDinput.getText(), this.loginIDinputConfirmation.getText(), this.loginIDInputFeedback);
        validator.validatePassword(this.passwordInput.getText(),this.passwordInputConfirmation.getText(), this.passwordInputFeedback);
        String message ="";
        String flag="";//this flag will tell the next screen if its G (good) or E (error)
        //loop through all the feedback text objects and make sure they are empty
        if(this.loginIDInputFeedback.getText().length() == 0 && this.passwordInputFeedback.getText().length() ==0){
            
            this.newUser.setLoginID(this.loginIDinput.getText());
            this.newUser.setPassword(this.passwordInput.getText());
            /*-------------------------To DO----------------------------
            ------------------------------------------------------------ 
            ------------------------------------------------------------  
            When adding the user to the database we will need to figure out if
            the user was indeed added to the database.  Basically, in the NewUser
            class method .addUserToDatabase() return a boolean that will tell us
            whether there was a problem adding the user or not.
            ------------------------------------------------------------  
            -------------------------------------------------------------
            ------------------------------------------------------------*/
            /*
            Here we are goin gto check a few things.  First make sure that the login
            doesn't already exists in the database.  then add the user if it doesn't.  
            
            */
            //create an instance of the connectivity class
            Connectivity connection = new Connectivity();
            if(connection.valueExists(this.newUser.getLoginID(), "LoginID")){
                this.loginIDInputFeedback.setText("Login ID is already taken");
            }else{
                //if the login id doesn't exist, proceed to adding the data to the database
                if(this.newUser.addUserToDatabase()){
                    message = this.newUser.getLoginID() +" created successfully";
                    flag = "G";
                    
                    
                }else{
                    message = "Your account could not be created, you will be returned to the login screen";
                    flag = "E";
                }
                //This is code to open a new scene with data passed to it
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/scenes/NewUserConfirmation.fxml"));
                Parent newUserConfirmation = loader.load();
                Scene newUserConfirmationScene = new Scene(newUserConfirmation);
                NewUserConfirmationController controller = loader.getController();
            
                controller.initData(message,flag);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(newUserConfirmationScene);
                window.show();
            }
                   

        }
    }
    
    public void clearClicked(){
        //clear all the fields
        for(JFXTextField val: inputGroup){
            val.clear();
            val.setStyle("-fx-opacity:.50;");
        }
        //finally clear all the feedback labels
        this.loginIDInputFeedback.setText("");
        this.passwordInputFeedback.setText("");
    }
    
    
    
    
    
}
