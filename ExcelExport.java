/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Rome
 */
public class ExcelExport {
    
        
public static void ExportMyExcel() throws Exception{
      Connection connect = DriverManager.getConnection( 
         "jdbc:mysql://sql3.freesqldatabase.com:3306/sql3285724" , 
         "sql3285724" , 
         "ti6lHpxTaG"
      );

      Statement statement = connect.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM TurnbackData "
              + "INNER JOIN User ON TurnbackData.FK_userID=User.PK_userID "
              + "INNER JOIN TurnbackType ON "
              + "TurnbackType.PK_turnbackTypeID=TurnbackData.FK_turnbackTypeID "
              + "INNER JOIN TurnbackCategory ON "
              + "TurnbackCategory.PK_turnbackCategoryID"
              + "=TurnbackData.FK_turnbackCategoryID "
              + "INNER JOIN Department "
              + "ON Department.PK_departmentID"
              + "=TurnbackData.FK_departmentID "
              + "ORDER BY User.PK_userID");

      XSSFWorkbook workbook = new XSSFWorkbook(); 
      XSSFSheet spreadsheet = workbook.createSheet("STT");

      XSSFRow row = spreadsheet.createRow(1);
      XSSFCell cell;
      cell = row.createCell(0);
      cell.setCellValue("Date");
      cell = row.createCell(1);
      cell.setCellValue("Employee ID");
      cell = row.createCell(2);
      cell.setCellValue("Username");
      cell = row.createCell(3);
      cell.setCellValue("First Name");
      cell = row.createCell(4);
      cell.setCellValue("Last Name");
      cell = row.createCell(5);
      cell.setCellValue("Role");
      cell = row.createCell(6);
      cell.setCellValue("Department");
      cell = row.createCell(7);
      cell.setCellValue("Email");
      cell = row.createCell(8);
      cell.setCellValue("Turnback Text");
      cell = row.createCell(9);
      cell.setCellValue("Turnback Type Name");
      cell = row.createCell(10);
      cell.setCellValue("Turnback Category Name");

      int i = 2;

      while(resultSet.next()) {
         row = spreadsheet.createRow(i);
         cell = row.createCell(0);
         cell.setCellValue(resultSet.getString("date"));
         cell = row.createCell(1);
         cell.setCellValue(resultSet.getString("PK_userID"));
         cell = row.createCell(2);
         cell.setCellValue(resultSet.getString("userName"));
         cell = row.createCell(3);
         cell.setCellValue(resultSet.getString("firstName"));
         cell = row.createCell(4);
         cell.setCellValue(resultSet.getString("lastName"));
         cell = row.createCell(5);
         cell.setCellValue(resultSet.getString("role"));
         cell = row.createCell(6);
         cell.setCellValue(resultSet.getString("departmentName"));
         cell = row.createCell(7);
         cell.setCellValue(resultSet.getString("email"));
         cell = row.createCell(8);
         cell.setCellValue(resultSet.getString("turnbackText"));
         cell = row.createCell(9);
         cell.setCellValue(resultSet.getString("turnbackTypename"));
         cell = row.createCell(10);
         cell.setCellValue(resultSet.getString("turnbackCategoryName"));
       
         i++;
      }

      FileOutputStream out = new FileOutputStream(new File("STTexceldatabase.xlsx"));
      workbook.write(out);
      //workbook2.write(out);
      out.close();
      System.out.println("STTexceldatabase.xlsx written successfully\n"
              + "Check the STT folder!");
   }
}

