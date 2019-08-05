/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import classess.Connectivity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * FXML Controller class
 *
 * @author User
 */


public class EmployeesSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //text
    @FXML private Text userName;
    @FXML private Text fullName;
    @FXML private Text email;
    @FXML private Text department;
    @FXML private Text role;
    
    //labels
    @FXML private Text nameLabel;
    @FXML private Text emailLabel;
    @FXML private Text departmentLabel;
    @FXML private Text roleLabel;
    
    //ArrayList of all the Text
    private ArrayList<Text> textlabels=new ArrayList<Text>();
    
    //buttons
    @FXML private JFXButton seeAllButton;
    @FXML private JFXButton clearButton;
    
    //combobox
    @FXML private ComboBox departmentDropdown;
    //main menu link
    @FXML private Hyperlink mainMenuReturnLink;
    
    //userlist
    private ArrayList<String> userNames = new ArrayList<String>();
    //listview
    @FXML private JFXListView<String> userListView;
    //connectivity class instance
    private Connectivity connection = new Connectivity();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize action events
        this.clearButton.setOnAction(e->{this.clearButtonClicked();});
        this.seeAllButton.setOnAction(e->{this.seeAllButtonClicked();});
        this.departmentDropdown.setOnAction(e->{this.departmentDropdownChanged();});
        
        
        //get the usernames
        this.getUserNames();
        //POPUlate the userListView
        this.setUserListView();
        //add the listeners to the listView
        this.setListListeners();
        
        //initial values for the text fields
        this.userName.setText("");
        this.userName.fontProperty().set(Font.font("tahoma",FontWeight.BOLD,18));
        this.fullName.setText("");
        this.email.setText("");
        this.role.setText("");
        this.department.setText("");
        
        this.nameLabel.setText("");
        this.emailLabel.setText("");
        this.roleLabel.setText("");
        this.departmentLabel.setText("");
        this.nameLabel.setFont(Font.font("tahoma", FontWeight.BOLD, 14));
        this.emailLabel.setFont(Font.font("tahoma", FontWeight.BOLD, 14));
        this.departmentLabel.setFont(Font.font("tahoma", FontWeight.BOLD, 14));
        this.roleLabel.setFont(Font.font("tahoma", FontWeight.BOLD, 14));
        //add all text to arraylist
        this.textlabels.add(this.userName);
        this.textlabels.add(this.fullName);
        this.textlabels.add(this.email);
        this.textlabels.add(this.role);
        this.textlabels.add(this.department);
        this.textlabels.add(this.nameLabel);
        this.textlabels.add(this.emailLabel);
        this.textlabels.add(this.departmentLabel);
        this.textlabels.add(this.roleLabel);
        
        //populate department dropdown
        this.departmentDropdown.getItems().add("E-Brakes");
        this.departmentDropdown.getItems().add("General Actuations");
        this.departmentDropdown.getItems().add("Legacy");
        this.departmentDropdown.getItems().add("Process Areas");
        this.departmentDropdown.getItems().add("Tank Units");
        this.departmentDropdown.promptTextProperty().set("Select Department");
    } 
    //getters
    public void getUserNames(){
        if(this.connection.ConnectDB()){
            if(this.connection.populateUserList()){
              this.userNames = this.connection.getUserNames();  
            }
            else{
                alert("There was a problem retrieving the users");
            }
            
        }else{
            alert("Could not connect to the database");
        }
        
        
    }
    
    public void setUserListView(){
        //go through the username arraylist and populate the listView
        for(String name:this.userNames){
            this.userListView.getItems().add(name);
        }
    }
    //setListeners for the listView Items
    public void setListListeners(){
        this.userListView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                     nameLabel.setText("Name: ");
                     emailLabel.setText("Email: ");
                     roleLabel.setText("Role: ");
                     departmentLabel.setText("Department: ");
                     
                     if(connection.ConnectDB()){
                         userName.setText(new_val);
                         //connected to database so retrieve user information
                         String[] temp = connection.getUserInformation(new_val);
                         String theFullname = temp[0]+" "+temp[1];
                         fullName.setText(theFullname);
                         email.setText(temp[4]);
                         department.setText(temp[3]);
                         role.setText(temp[2]);
                     }else{
                         alert("There was a problem retrieving this user's information");
                     }
                }
    
            });
    }//end of function
    
    
    
    //action mentods
    //clears all the user information and deselects the listview item
    public void clearButtonClicked(){
        //iterate throug all the Text and clear it
        clearTextLabels();
    }
    
    public void clearTextLabels(){
        for(Text val:this.textlabels){
            val.setText("");
        }
    }
    
    public void seeAllButtonClicked(){
        try{
            //clear the current list view
            this.userListView.getItems().clear();
            //call the metod to populate the list
            this.setUserListView();
        }catch(Exception e){
           System.out.println("Something bad"); 
        }
        this.clearTextLabels();
    }
    
    public void departmentDropdownChanged(){
        this.clearTextLabels();
        ArrayList<String> temp = new ArrayList<>();
        //get the value
        String thisdepartment = this.departmentDropdown.getSelectionModel().getSelectedItem().toString();
        
        //get an arraylist of all users by that department
        if(this.connection.ConnectDB()){
            //call a method to get the users by thier department
            if(this.connection.populateUsersByDepartment(thisdepartment)){
                //this populated the userlist, now retrieve it
                temp = this.connection.getDepartmentUsers();
            }else{
                System.out.println("There was a problem getting the department users");
            }
        }
        try{
            this.userListView.getItems().clear();
            for(String theName:temp){
                this.userListView.getItems().add(theName);
            }
        }
        catch(Exception e){}
        this.clearTextLabels();
    }
    
    public void returnToMainMenu(ActionEvent event)throws Exception{
         Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/mainMenu.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    //alert
    public void alert(String message){
        //alert class instance
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
