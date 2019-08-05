package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author User
 */
public class NewUserConfirmationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private String message ="";
    private String flag ="";
    @FXML private Text header;
    @FXML private Text feedback;
    @FXML private JFXButton okButton;
    private String loginID = "";
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        feedback.setText("");
        okButton.setText("OK");

        
    }    
    
    
    
    public void initData(String message,String flag){
        //check the flag
        /*
        if flag is G, that means it was good and the message should be displayed
        in green.  If the flag is E, that means there was an error and the message
        will be displayed in red
        */
        this.feedback.setText(message);
        if(flag.equals("G")){
            //message in green
            this.feedback.setFill(Paint.valueOf("green"));
            this.header.setFill(Paint.valueOf("green"));
            
        }
        else if(flag.equals("E")){
                this.header.setFill(Paint.valueOf("red"));
                this.header.setText("Problem");
                this.feedback.setFill(Paint.valueOf("red"));
   
        }
        
    }
    
    public void returnToLogin(ActionEvent event)throws IOException{
        //get the fxml resource
        Parent newUserParent = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
        Scene newUserScene = new Scene(newUserParent);

        //this is to ge the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newUserScene);
        window.show();
    }
    
    
}
