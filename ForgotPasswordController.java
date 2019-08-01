package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import classess.Connectivity;
import classess.Mail;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import classess.NewUser;


/**
 * FXML Controller class
 *
 * @author ryanhoffman
 */
public class ForgotPasswordController implements Initializable {

   
    @FXML private Text emailinputfeedback;
    @FXML private JFXTextField emailinput;  
    @FXML private JFXButton nextButton;
    @FXML private JFXButton clearButton;
    @FXML private Hyperlink returnToLoginLink;
    
    private NewUser email;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

       
    }
     
        
  //clears all the fields in the form
    public void clearButton(ActionEvent event) throws IOException{
            emailinput.clear();
    }

      //returns to login
    public void returnToLogin(ActionEvent event) throws IOException{
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    
    //alert message for email not in DB
     public void alertInvalid(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setHeaderText("Email not Found");

          alert.showAndWait();
       
 }
     //validate that the email exists and return password
   public void validateEmail(ActionEvent event)throws IOException{
       boolean entry = true;
       
       if(emailinput.getText().isEmpty()){
           
       emailinputfeedback.setText("Please Enter Valid Email");
       
       }
       //create an instance of the connectivity class
        Connectivity connection = new Connectivity();        
        
        //Check if email is provided and flag is true
        //if email is present check if its valid
        if (connection.valueExists(this.emailinput.getText(), "Email")) {
            String email = this.emailinput.getText();   
        //attempt to send email 
        Mail mail = new Mail();
        Mail sendmail;
            mail.sendEmail(email);
           
            //Since the email has been found go to new scene
            
            Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/ForgotPassword2.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
            
        }else{
            alertInvalid();
        }
}
}

