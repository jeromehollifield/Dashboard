/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import classess.Connectivity;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
public class EditUserSceneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    //Text 
    @FXML private Text userNameLabel;
    @FXML private Text departmentTextLabel;
    @FXML private Text roleTextLabel;
    
    //buttons
    @FXML private JFXButton clearButton;
    @FXML private JFXButton editButton;
    
    //checkboxes
    @FXML private CheckBox firstNameCheckbox;
    @FXML private CheckBox lastNameCheckbox;
    @FXML private CheckBox emailCheckbox;
    @FXML private CheckBox departmentCheckbox;
    @FXML private CheckBox roleCheckbox;
    //inputFields
    @FXML private JFXTextField firstNameInputField;
    @FXML private JFXTextField lastNameInputField;
    @FXML private JFXTextField emailInputField;
    
    //comboBoxes
    @FXML private ComboBox<String> departmentDropdown= new ComboBox<String>();
    @FXML private ComboBox<String> roleDropdown=new ComboBox<String>();
    
    //listView
    @FXML private JFXListView<String> userListView= new JFXListView<>() ;
    
    //userName Arraylist
    private ArrayList<String> userNames = new ArrayList<String>();
    
    //database connection
    private Connectivity dbConnection = new Connectivity();
    
    //arraylist of all checkboxes
    private ArrayList<CheckBox> boxes = new ArrayList<CheckBox>();
    
    //arraylist holding the values in the dropdown menus
    private ArrayList<String> roleDropdownValues = new ArrayList<String>();
    private ArrayList<String> departmentDropdownValues = new ArrayList<String>();
    
    //hold chosen department
    private String selectedDepartment="";
    //hold role selection
    private String selectedRole ="";
    //track if something has changed, no point in doing a query if something hasnt'
    private boolean somethingChanged = false;
    private String defaultFirstname = "";
    private String defaultLastname = "";
    private String defaultEmail = "";
    private String defaultDepartment ="";
    private String defaultRole="";
            
    private HashMap<String,String> defaultValuesMap = new HashMap<>();
    
    private boolean fieldChanged = false;
    
    //main menu link
    @FXML private Hyperlink mainMenuLink;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //get a list of all the usernames
        getUserNames();
        displayUsers();
        setListListeners();
        
        //checkbox methods
        this.firstNameCheckbox.setOnAction(e->{this.ckbxChanged(this.firstNameCheckbox,this.firstNameInputField);});
        this.lastNameCheckbox.setOnAction(e->{this.ckbxChanged(this.lastNameCheckbox,this.lastNameInputField);});
        this.emailCheckbox.setOnAction(e->{this.ckbxChanged(this.emailCheckbox,this.emailInputField);});
        this.editButton.setOnAction(e->{this.editButtonClicked();});
        this.clearButton.setOnAction(e->{this.clearButtonClicked();});
        this.departmentCheckbox.setOnAction(e->{this.dropdownCheckBoxChanged(this.departmentCheckbox,this.departmentDropdown);});
        this.roleCheckbox.setOnAction(e->{this.dropdownCheckBoxChanged(this.roleCheckbox,this.roleDropdown);});
        this.departmentDropdown.setOnAction(e->{this.dropDownChanged(this.departmentDropdown, "department");});
        this.roleDropdown.setOnAction(e->{this.dropDownChanged(this.roleDropdown, "role");});
        
        //blur the controllers
        blur(this.firstNameInputField);
        blur(this.lastNameInputField);
        blur(this.emailInputField);
        blur(this.roleDropdown);
        blur(this.departmentDropdown);
        //set editables on controllers
        this.firstNameInputField.setEditable(false);
        this.lastNameInputField.setEditable(false);
        this.emailInputField.setEditable(false);
        this.departmentDropdown.disableProperty().set(true);
        this.roleDropdown.disableProperty().set(true);
        
        //label styling
        this.userNameLabel.textProperty().set("User");
        this.userNameLabel.fontProperty().set(Font.font("tahoma",FontWeight.BOLD,18));
        
        this.departmentTextLabel.textProperty().set("Department");
        this.departmentTextLabel.fontProperty().set(Font.font("tahoma", FontWeight.BOLD, 18));
        
        this.roleTextLabel.textProperty().set("Role");
        this.roleTextLabel.fontProperty().set(Font.font("tahoma",FontWeight.BOLD,18));
        
        //populate the boxes arraylist with all the checkboxes
        boxes.add(this.firstNameCheckbox);
        boxes.add(this.lastNameCheckbox);
        boxes.add(this.emailCheckbox);
        boxes.add(this.departmentCheckbox);
        boxes.add(this.roleCheckbox);
        
        //lock all the checkboxes
        this.lockCheckboxes(boxes);
        
        //populate the arraylists holding the values for the dropdown menus
        this.departmentDropdownValues.add("E-Brakes");
        this.departmentDropdownValues.add("General Actuations");
        this.departmentDropdownValues.add("Legacy");
        this.departmentDropdownValues.add("Process Areas");
        this.departmentDropdownValues.add("Tank Units");
        
        //populate the dropdown for department
        for(String dept:this.departmentDropdownValues){
            this.departmentDropdown.getItems().add(dept);
        }
        
        //populate the role dropdown
        this.roleDropdown.getItems().add("Employee");
        this.roleDropdown.getItems().add("Manager");
        
        //populate the hashmap with the default values
        populateMap();
    }
    //function to get usernames from the database
    public void getUserNames(){
        //populate all the usernames in here
        if(dbConnection.ConnectDB()){
            //we are connected to the database
            //get the userList from the database
            if(this.dbConnection.populateUserList()){
                //the list was created by the database method 
                this.userNames = this.dbConnection.getUserNames();
                
            }
        }else{
            System.out.println("Connected to database");
            
        }
    }
    //function to show all the suernames on the listview
    public void displayUsers(){
        //go through the userName arraylist and add them to the listView
        for(String val:this.userNames){
            this.userListView.getItems().add(val);
        }
    }
    
    public void setListListeners(){
        this.userListView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                      
                      populateFields(new_val);
                }
    
            });
    }//end of function
    
    public void populateMap(){
        //get the default class memberfields and insert them into the mpa
        //populate the defaultValueMaps
        this.defaultValuesMap.put("firstName", this.defaultFirstname);
        this.defaultValuesMap.put("lastName",this.defaultLastname);
        this.defaultValuesMap.put("defaultEmail", this.defaultEmail);
        this.defaultValuesMap.put("defaultDepartment", this.defaultDepartment);
        this.defaultValuesMap.put("defaultRole",this.defaultRole);
    }
    
    
    public void populateFields(String userName){
        //take this array and populate all the values on the input fields
        this.userNameLabel.setText(userName);
       
        if(this.dbConnection.ConnectDB()){
            //we connected
            String[] temp = this.dbConnection.getUserInformation(userName);
            //take this array and populate the input fields for firstName, lastName,Email,
            this.firstNameInputField.setText(temp[0]);
            this.lastNameInputField.setText(temp[1]);
            this.emailInputField.setText(temp[4]);
            this.roleDropdown.promptTextProperty().set(temp[2]);
            this.departmentDropdown.promptTextProperty().set(temp[3]);
            //add these default values to the map
            this.defaultFirstname = temp[0];
            this.defaultLastname = temp[1];
            this.defaultEmail = temp[4];
            this.defaultRole = temp[2];
            this.defaultDepartment = temp[3];
            
            //repopulate the map
            populateMap();
            //unlock the checkboxes
            this.unlockCheckboxes(this.boxes);
            
//            //print out the mapfor testing purposes
//            for(Map.Entry<String,String> mapObj: this.defaultValuesMap.entrySet()){
//                System.out.println(mapObj.getKey());
//                System.out.println(mapObj.getValue());
//            }
            
        
        }
    }
    //checkboxes for the input fields
    public void ckbxChanged(CheckBox box,JFXTextField field){
        if(box.isSelected()){
            //unblur and set editable
            unblur(field);
            field.setEditable(true);
        }else{
            //blur and make uneditable
            blur(field);
            field.setEditable(false);
        }
    }
    
    //checkboxes for the dropdown controls
    public void dropdownCheckBoxChanged(CheckBox box,ComboBox dropdown){
        if(box.isSelected()){
            dropdown.disableProperty().set(false);
            unblur(dropdown);
        }else{
            dropdown.disableProperty().set(true);
            blur(dropdown);
        }
    }
    
    public void dropDownChanged(ComboBox dropdown,String flag){
        try{
            if(flag.equals("department")){
            //get the value of the dropdown for the department
            this.selectedDepartment = dropdown.getSelectionModel().getSelectedItem().toString();
            
        }else if(flag.equals("role")){
            this.selectedRole = dropdown.getSelectionModel().getSelectedItem().toString();
            
        }
        }catch(Exception e){}
    }
    
    //locks all the checkboxes
    public void lockCheckboxes(ArrayList<CheckBox> boxes){
        for(CheckBox box: this.boxes){
            box.disableProperty().set(true);
        }
    }
    
    public void unlockCheckboxes(ArrayList<CheckBox> boxes){
        for(CheckBox box: this.boxes){
            box.disableProperty().set(false);
        }
    }
    //blur for the inputfields
    public void blur(JFXTextField field){
        field.setStyle("-fx-opacity:.5;");
    }
    //unblur for the inputfields
    public void unblur(JFXTextField field){
        field.setStyle("-fx-opacity:1;");
    }
    //blur for the checkboxes
    public void blur(ComboBox<String> box){
        box.setStyle("-fx-opacity:.5;");
    }
    //unblur for checkboxes
    public void unblur(ComboBox<String> box){
        box.setStyle("-fx-opacity:1;");
    }
    //this function will clear all the fields
    public void clearButtonClicked(){
        //this function will clear all the fields
        this.userNameLabel.textProperty().set("");
        this.firstNameInputField.setText("First name");
        this.lastNameInputField.setText("Last name");
        this.emailInputField.setText("Email");
        this.userNameLabel.setText("User");
        this.roleDropdown.getSelectionModel().clearSelection();
        this.departmentDropdown.getSelectionModel().clearSelection();
        //return the dropdows to default state
        this.roleDropdown.promptTextProperty().set("Role");
        this.departmentDropdown.promptTextProperty().set("Department");
        
        //lock all the checkboxes
        lockCheckboxes(this.boxes);
        
        for(CheckBox box: this.boxes){
            box.setSelected(false);
        }
        
        //uneditables
        this.firstNameInputField.setEditable(false);
        this.lastNameInputField.setEditable(false);
        this.emailInputField.setEditable(false);
        //blurs
        blur(this.firstNameInputField);
        blur(this.lastNameInputField);
        blur(this.emailInputField);
        blur(this.departmentDropdown);
        blur(this.roleDropdown);
        //uneditable for dropdowns
        this.roleDropdown.disableProperty().set(true);
        this.departmentDropdown.disableProperty().set(true);
        
    }
 
    
    
    public void editButtonClicked(){
        //get all of the values in the form and check them against the hashmap
        //this is where we set the somethingChanged flag
        String first = this.firstNameInputField.getText();
        String last = this.lastNameInputField.getText();
        String email = this.emailInputField.getText();
        String dep = "";
        String role="";
        if(this.departmentDropdown.getSelectionModel().getSelectedItem() == null){
            dep= this.departmentDropdown.promptTextProperty().get();
            //this means that the user did not change the dropdown
            
        }else{
            dep = this.departmentDropdown.getSelectionModel().getSelectedItem();
        }
        
        if(this.roleDropdown.getSelectionModel().getSelectedItem()== null){
            role = this.roleDropdown.promptTextProperty().get();
        }else{
            role = this.roleDropdown.getSelectionModel().getSelectedItem();
        }
        
        if(this.isDefaultValue(first) && 
                this.isDefaultValue(last) && 
                this.isDefaultValue(email) && 
                this.isDefaultValue(role) && 
                this.isDefaultValue(dep)){
            //this is where we alert the user that no need for update is needed
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("User information has not changed...no update performed");
            alert.showAndWait();
            
        }else{
            
            if(this.userNameLabel.getText().equals("User")){
                System.out.println("This is empty");
            }else{
                //create a new instance of the connection class
                this.dbConnection = new Connectivity();
                //this is where we do database operations
                if(this.dbConnection.ConnectDB()){
                    //we connected to the database                  
                    
                    boolean update= this.dbConnection.updateUser(first, last, email, dep,role , this.userNameLabel.getText());
                    
                
                }else{
                    System.out.println("Could not connect to the database");
                }
                
            }
        }
    }
    public void mainMenuClicked(ActionEvent event)throws IOException{
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/mainMenu.fxml"));
        Scene Scene = new Scene(Parent);
        
        //this is to ge the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
      
        
    }
    //compares default hashmap key to the current control value;
    public boolean isDefaultValue(String lookupValue){
        boolean isDefault = false;
        //get the map value and compare
        if(this.defaultValuesMap.containsValue(lookupValue)){
            
            isDefault = true;
        }
        
        
        return isDefault;
    }
    
}
