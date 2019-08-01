package main;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author stevenhernandez
 */
public class STTrackerApp extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
        stage.setTitle("Strategic Turnback Tracker");
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

   
}
