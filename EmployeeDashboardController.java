/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class EmployeeDashboardController implements Initializable {
    
        public void initData(String login){
        this.loginID = login;
      
    }
        
    private String loginID = "";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        
           }

    public void newTurnback(ActionEvent event) throws IOException {
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/scenes/TurnbackEntry.fxml"));
            //this will give us access and the ability to pass the object
            Parent turnbackEntryParent = loader.load();
            
            Scene newTurnbackEntry = new Scene(turnbackEntryParent);
            
            TurnbackEntryController controller = loader.getController();
            controller.initData(this.loginID);
            
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newTurnbackEntry);
            window.show();
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
    


}
