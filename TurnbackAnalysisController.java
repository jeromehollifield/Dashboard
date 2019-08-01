/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import classess.ExcelExport;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class TurnbackAnalysisController implements Initializable {
    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXComboBox<String> department;
    
    @FXML
    private JFXComboBox<String> turnbackCategory;
    
    @FXML private JFXButton graphButt;
    @FXML private JFXButton excelButt;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
   
    
    private String userName;
    
    public void initData(String userName){
        this.userName = userName;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // enter departments
        
        this.department.getItems().addAll("E-Brakes", "General Actuations", 
                "Legacy", "Process Areas", "Tank Units");
        //add categories 
        this.turnbackCategory.getItems().addAll("Computer Issues", "Priority Change",
                "Parts Issues", "Document Issue", "Equipment Setup", "Other");
        this.turnbackCategory.setOnAction(e -> {
            menuChanged();
        });
        this.department.setOnAction(e -> {
            menuChanged();
        });
                this.excelButt.setOnAction(e->{try {
            excelButtonClicked();
            } catch (Exception ex) {

            }
});
        
        
    }

    //action to take when the menu changes
    public void menuChanged() {
        if (this.turnbackCategory.isFocused()) {
            changeOpacity(this.turnbackCategory);
        } else if (this.department.isFocused()) {
            changeOpacity(this.department);
        }
    }

    //changes the opacity of obj 
    public void changeOpacity(JFXComboBox menu) {
        if (menu.getSelectionModel().isEmpty()) {
            menu.setStyle("-fx-opacity:0;");
        } else {
            menu.setStyle("-fx-opacity:1");
        }
    }    
    
    public void graphButtonClicked(ActionEvent event) throws IOException, SQLException{
       
        String start = null;
        String end = null;
        
        
        //This will save Manager from accidently viewing all entires from DB
        //not a big deal in our small DB but in a larger environment it would be 
        //bad
        if( startDate.getValue() == null && endDate.getValue() == null){
            alertDate();
        }else{
        
        LocalDate ldStart = startDate.getValue();
        LocalDate ldEnd = endDate.getValue();
        
        start = ldStart.toString();  
        end = ldEnd.toString();
        
        String dept = department.getValue();
        String tbCategory = turnbackCategory.getValue();
        
        
       
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(getClass().getResource("/scenes/ViewGraph.fxml"));
         //this will give us access and the ability to pass the object
         Parent loginParent = loader.load();

         Scene viewGraph = new Scene(loginParent);

         ViewGraphController controller = loader.getController();
         controller.initData(start, end, dept, tbCategory);
         

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(viewGraph);
        window.show();
      
    }
    }
    
    public void excelButtonClicked() throws Exception{
        ExcelExport.ExportMyExcel();
    }
    
     //returns to login
    public void returnToLogin(ActionEvent event) throws IOException {
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/FXMLDocument.fxml"));
        Scene Scene = new Scene(Parent);

        //this is to ge the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    
      //alert message for email not in DB
     public void alertDate(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setHeaderText("Enter a Date Range to view Graph");

          alert.showAndWait();
       
 }
}
