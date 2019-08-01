/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author stevenhernandez
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private JFXButton editUserButton;
    @FXML private JFXButton removeUserButton;
    @FXML private JFXButton employeesButton;
    @FXML private JFXButton turnbackAnalysisButton;
    private String userName;
    private Event event;
    
    public void initData(String userName){
        this.userName = userName;
       
    }        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize the controls
        
        
       
    
    }    
    //button functions
    public void editUserButtonClicked(ActionEvent event)throws IOException{
        //we are going to open uo a new scene here
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/editUserScene.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    public void removeUserButtonClicked(ActionEvent event)throws IOException{
        //we are going to open uo a new scene here
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/removeUserScene.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    public void employeesButtonClicked(ActionEvent event)throws Exception{
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/employeesScene.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    public void turnbackAnalysisButtonClicked(ActionEvent event)throws IOException{
        //this is where we are going to change the scene to the turnback analisis scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/scenes/TurnbackAnalysis.fxml"));
                    //this will give us access and the ability to pass the object
        Parent mainMenuParent = loader.load();

        Scene newTurnbackAnalysisScene = new Scene(mainMenuParent);

        TurnbackAnalysisController controller = loader.getController();
        controller.initData(this.userName);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newTurnbackAnalysisScene);
        window.show();
    }
    
}
