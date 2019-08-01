/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXListView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import classess.Connectivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author stevenhernandez
 */
public class RemoveUserSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private Text feedbackText;
    @FXML private Text userInformation;
    //informaion labels
    @FXML private Text nameLabel;
    @FXML private Text roleLabel;
    @FXML private Text departmentLabel;
    @FXML private Text emailLabel;
    @FXML private Text nameTitle;
    @FXML private Text roleTitle;
    @FXML private Text departmentTitle;
    @FXML private Text emailTitle;
    //Text controll ArrayList
    private ArrayList<Text> labelList = new ArrayList<>();
    private ArrayList<Text> titleList = new ArrayList<>();
    //Buttons
    @FXML private JFXButton findButton;
    @FXML private JFXButton removeButton;
    @FXML private JFXButton clearButton;
    @FXML private JFXButton reloadListButton;
    //Other
    @FXML private JFXTextField userNameInput;
    @FXML private JFXListView<String> userList=new JFXListView<String>();//to show all the usernames on the scene
//    private ObservableList<String> items =FXCollections.observableArrayList();//collection we add to the ListView
    private Connectivity sceneConnection = new Connectivity();//so we can user the database
    private ArrayList<String> userNames = new ArrayList<String>();//holds the array list of userNames
   
    private boolean userPicked = false;
    private String selectedName = "";
    
    //hyperlink to return to main menu
    @FXML private Hyperlink mainMenuReturn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        labelList.add(this.nameLabel);
        labelList.add(this.roleLabel);
        labelList.add(this.departmentLabel);
        labelList.add(this.emailLabel);
        
        titleList.add(this.nameTitle);
        titleList.add(this.roleTitle);
        titleList.add(this.departmentTitle);
        titleList.add(this.emailTitle);
        
        
        
        //set the onAction events to the button controls
        findButton.setOnAction(e->{findButtonClicked();});
        removeButton.setOnAction(e->{removeButtonClicked();});
        clearButton.setOnAction(e->{clearButtonClicked();});
        reloadListButton.setOnAction(e->{reloadButtonClicked();});
        
        //prepare the scnee
        clearTextLabels();
        
        //populate the listView
        populateUserListView();
        
        //now that the list is populated, add the event listeners
        setViewListListeners();
    } //end of function   
    
    public void showUserInformation(String selected){
        this.userInformation.setText(selected);
//        //this method will show all the information about the user that was selected
//        //declare an Array to hold the value returned by the connectivity class
        String[] tempArr = this.sceneConnection.getUserInformation(selected);
//        //now that we have all the information we can go ahead and set all the information in the scene 
//        
        //create the name label text
        String name = tempArr[0]+" "+tempArr[1];
        String role = tempArr[2];
        String department = tempArr[3];
        String email = tempArr[4];
        //set the title text
        this.nameTitle.setText("Name: ");
        this.roleTitle.setText("Role: ");
        this.departmentTitle.setText("Department: ");
        this.emailTitle.setText("Email: ");
        //set the titles to bold by iterating through the titleList arraylist
        for(Text val:this.titleList){
            val.setFont(Font.font("tahoma", FontWeight.BOLD, 13));
        }
       
        this.nameLabel.setText(name);
        this.roleLabel.setText(role);
        this.departmentLabel.setText(department);
        this.emailLabel.setText(email);
        
        

    }//end of function
    
    public void findButtonClicked(){
        //first check that the input is not empty
        String userInputField = this.userNameInput.getText();
        if(userInputField.isEmpty()){
            //there is nothing in the input fieldt
            this.feedbackText.setText("Enter Something");
        }else{
            //check for the username in the arralyst
            if(this.userNames.contains(userInputField)){
                //this means something was found that matches
                showUserInformation(userInputField);
                //go through the listView and remove everything, but the value found
                for(String val:this.userNames){
                    if(val.equals(userInputField)){
                        continue;
                    }else{
                        this.userList.getItems().remove(val);
                        
                    }
                    //the code belo is used to sor the listview item but throwing an exception
//                    java.util.Collections.sort(this.userList.getItems(), new java.util.Comparator<String>() {
//                        @Override
//                        public int compare(String o1, String o2) {
//                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                        }
//                    });
                }
                this.userInformation.setText(userInputField);
                this.userPicked = true;
                
            }else{
                this.feedbackText.setText("Nothing Found");
            }
        }
    }//end of function
    
    public void reloadButtonClicked(){
       
       if(userPicked){
          //iterate through the arraylist
            for(String name:this.userNames){
                if(this.selectedName.equals(name)){
                    continue;
                }else{
                    this.userList.getItems().add(name);
                }
            }
  
       }

       //clear all the textfields
       this.selectedName ="";
       this.userPicked = false;
       clearTextLabels();
        
    }//end of function
    
    
    /***********************************BUG***************************/
    public void removeButtonClicked(){
        String user = this.userInformation.getText();
        Alert alert = new Alert(AlertType.INFORMATION);
        boolean lockedOut= false;
        System.out.println(this.selectedName);
        //Get the current userName that has been selected
        if(this.sceneConnection.ConnectDB()){
            //we are connected to the database so run the method to delete the user
            if(user.isEmpty()){
                
                alert.setContentText("No user was selected from the list");
                alert.showAndWait();
            }else{
                lockedOut =this.sceneConnection.lockUser(user);
                if(lockedOut){
                    //delete the user from the usernames array
                    this.userNames.remove(user);
                    this.userList.getItems().remove(user);
                    alert.setContentText(String.format("%s was removed from the database", user));
                    alert.showAndWait();
                }
            }
            
        }
    }//end of function
   
    public void clearButtonClicked(){
        clearTextLabels();
    }//end of function
    
    public void populateUserListView(){
        //connect to the database
        if(this.sceneConnection.ConnectDB()){
            if(this.sceneConnection.populateUserList()){
                //the arrayList was created
                this.userNames = this.sceneConnection.getUserNames();
                //iterate through to get the names
                for(String val:this.userNames){
                    this.userList.getItems().add(val);
                }
            }else{
                System.out.println("Could not populate the list");
            }
        }else{
            System.out.println("Connection failed");
        }
        
    }//end of function
    
    public void setViewListListeners(){
        this.userList.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                        //set the new user information to that username
                       userInformation.setText(new_val);
                       //clear the input text field in case
                       userNameInput.clear();
                       //clear the feedback field in case
                       feedbackText.setText("");
                       //call a method to populate the scene with the user information
                       showUserInformation(new_val.toString());
                       selectedName  = new_val;
                }
    
            });
    }//end of function

    
    public void clearTextLabels(){
         //add labels and titles to the array lists
        this.userInformation.setText("");
        this.feedbackText.setText("");
        //iterate through the titleList and clear them
        for(Text title:this.titleList){
            title.setText("");
        }
        
        for(Text label:this.labelList){
            label.setText("");
        }
    }//end of function
    
    //returns the user to the main menu
    public void returnToMainMenu(ActionEvent event)throws IOException{
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/mainMenu.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    
}
