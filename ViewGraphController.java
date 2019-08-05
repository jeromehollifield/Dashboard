/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import classess.Connectivity;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 */
public class ViewGraphController implements Initializable {
  
   
    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private Button backBtn;
    @FXML
    private Button refresh;
    private String start = null;
    private String end = null;
    private String dept = null;
    private String tbCategory = null;
    int columnNo;
  
    /**
     * Initializes the controller class.
     */
    public void initData(String start, String end, String dept, String tbCategory){
        this.start = start;
        this.end = end;
        this.dept = dept;
        this.tbCategory = tbCategory;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }   
    
    @FXML
    private void loadChart(ActionEvent event) throws SQLException{
         Connection connect = DriverManager.getConnection( 
         "jdbc:mysql://sql3.freesqldatabase.com:3306/sql3285724" , 
         "sql3285724" , 
         "ti6lHpxTaG"
      );

         
         //if statements here to display the correct parameters from the dates
        // and the combo boxes
      //IF date is only provided   
         if(dept == null && tbCategory == null){     
        String count = "SELECT turnbackTypename, count(*) "
                + "FROM TurnbackData INNER JOIN "
                + "TurnbackType ON TurnbackData.FK_turnbackTypeID=TurnbackType.PK_turnbackTypeID "
                + "WHERE `date` BETWEEN '" + start + "' and '" + end + "' "
                + "GROUP BY TurnbackType.turnbackTypename";
              
        XYChart.Series <String, Double> series = new XYChart.Series<>();

        try{  
              Statement statement = connect.createStatement();
              ResultSet rs = statement.executeQuery(count);

           while (rs.next()) {
          series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
           barChart.setAnimated(false);
           barChart.getData().add(series);
           refresh.setDisable(true);
            
        }
        catch(SQLException e){
           
        }
        }
        
        
         //if dept has value and tbcategory is empty
  if(dept != null && !dept.equals("") && tbCategory == null){     
        String count = "SELECT turnbackTypename, count(*) FROM TurnbackData "
                + "INNER JOIN TurnbackType ON TurnbackData.FK_turnbackTypeID=TurnbackType.PK_turnbackTypeID "
                + "INNER JOIN Department ON Department.PK_departmentID=TurnbackData.FK_departmentID "
                + "WHERE `departmentName` = '" + dept + "' "
                + "and `date` BETWEEN '" + start + "' and '"+ end + "' "
                + "GROUP BY TurnbackType.turnbackTypename";
              
        XYChart.Series <String, Double> series = new XYChart.Series<>();

        try{  
              Statement statement = connect.createStatement();
              ResultSet rs = statement.executeQuery(count);

           while (rs.next()) {
          series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
           barChart.setAnimated(false);
           barChart.getData().add(series);
           refresh.setDisable(true);

        }
        catch(SQLException e){
           
        }
        }
  
  //if tbcategory is provided and the dept is empty new query
  if(tbCategory != null && !tbCategory.equals("") && dept == null){
      String count = "SELECT turnbackTypename, count(*) FROM TurnbackData "
       + "INNER JOIN TurnbackType ON TurnbackData.FK_turnbackTypeID=TurnbackType.PK_turnbackTypeID "
       + "INNER JOIN Department ON Department.PK_departmentID=TurnbackData.FK_departmentID "
       + "INNER JOIN TurnbackCategory ON TurnbackCategory.PK_turnbackCategoryID=TurnbackData.FK_turnbackCategoryID "
       + "WHERE `departmentName` = '" + dept + "' "
       + "AND `turnbackCategoryName` = '" + tbCategory + "' "
       + "and `date` BETWEEN '" + start + "' and '"+ end + "' "
       + "GROUP BY TurnbackType.turnbackTypename";
                      
          
        XYChart.Series <String, Double> series = new XYChart.Series<>();

        try{  
              Statement statement = connect.createStatement();
              ResultSet rs = statement.executeQuery(count);

           while (rs.next()) {
          series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
           barChart.setAnimated(false);
           barChart.getData().add(series);
           refresh.setDisable(true);

        }
        catch(SQLException e){
           
        }
  }
  
  //if tbcategory is provided and the dept is provided new query
  if(tbCategory != null && !tbCategory.equals("") && dept != null && !dept.equals("")){

      String count = "SELECT turnbackTypename, count(*) FROM TurnbackData "
              + "INNER JOIN TurnbackType ON TurnbackData.FK_turnbackTypeID=TurnbackType.PK_turnbackTypeID "
              + "INNER JOIN TurnbackCategory ON TurnbackCategory.PK_turnbackCategoryID=TurnbackData.FK_turnbackCategoryID "
              + "WHERE turnbackCategoryName = '" + tbCategory + "' "
                + "and `date` BETWEEN '" + start + "' and '"+ end + "' "
                + "GROUP BY TurnbackType.turnbackTypename";
                      
          
        XYChart.Series <String, Double> series = new XYChart.Series<>();

        try{  
              Statement statement = connect.createStatement();
              ResultSet rs = statement.executeQuery(count);

           while (rs.next()) {
          series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
           barChart.setAnimated(false);
           barChart.getData().add(series);
           refresh.setDisable(true);

        }
        catch(SQLException e){
           
        }
  }
   }
    
    
       //Display graph with the selected parameters.
    @FXML
        private void exportGraph(ActionEvent event) throws SQLException {
       WritableImage image = barChart.snapshot(new SnapshotParameters(), null);
    
    // TODO: probably use a file chooser here
    File file = new File("Barchart.png");
    
    try {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        alertChartPrint();
    } catch (IOException e) {
        // TODO: handle exception here
    }
    }

        
        
     @FXML
    private void goBack(ActionEvent event) throws IOException {
        Parent Parent = FXMLLoader.load(getClass().getResource("/scenes/TurnbackAnalysis.fxml"));
        Scene Scene = new Scene(Parent);

        //this is to ge the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(Scene);
        window.show();
    }
    //alert message for email not in DB
     public void alertChartPrint(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Chart Exported");
          alert.setHeaderText("Chart is saved as PNG on local Drive");

          alert.showAndWait();
       
 }
    //alert message for email not in DB
     public void alertDate(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setHeaderText("Enter a Date Range to view Graph");

          alert.showAndWait();
       
 }
}
