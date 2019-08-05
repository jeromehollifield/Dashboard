/*
Method for database connection (jdbc)
JDBC driver converts the Java data type to the appropriate 
JDBC type, before sending it to the database
Displays DB Connected or DB Failed at the end of the sign up
Replace host, userName, and password when connecting to other databases
 */
package classess;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author User
 */
public class Connectivity {

    // Enter the Database driver url, username, and passsword here
    private String host = "jdbc:mysql://sql3.freesqldatabase.com:3306/sql3285724";
    private String userName = "sql3285724";
    private String password = "ti6lHpxTaG";
    private boolean connectionEstablished = false;
    //arraylist we can use for strings
    ArrayList<String> userNames = new ArrayList<>();

    //Enter the Database name to select from below
    String TableName = "SELECT * FROM User";
    String Display = "SELECT `PK_userID`,`firstName`, `lastName`,`userName` FROM User";
    
    //need a Connection object here
    Connection dbConnection;
    //hashmap helps us with organizing all the data
    HashMap map;
    
    
    ResultSet rst = null;      //Row & column data  
    ResultSetMetaData mymeta = null;
    int columnNo;
    
    //arrayList to hold userNames selected by department
    ArrayList<String> departmentUsers;

    public boolean ConnectDB() {
        
        //this method is going to create a connection to the database
        try{
            dbConnection = DriverManager.getConnection(host,userName,password);
            //if we get here, we know the database has been connected
            this.connectionEstablished = true;
        }catch(SQLException e){
            //System.out.println(e);
            this.connectionEstablished = false;
        }
        return this.connectionEstablished;
    }
    //getter methods
    
    //this method returns an arrayList of all usernames in the db
    //before using this, you have to call the populateUserList() method in this class
    public ArrayList<String> getUserNames(){
        return this.userNames;
    }
    //you have to connect to the database before using this method
    public boolean addNewUser(String f,String l,String e,String p,String d,String r,String log){
       /*
        f-> first name
        l-> last name
        e-> email
        p-> password
        d-> department
        r-> role
        log-> login id
        */
        boolean success = false;
        Statement statement ;
        String query = "INSERT INTO `User` (`firstName`, `lastName`,`userName`,`password`,`role`,`department`,`email`) VALUES ('"+f+"','"+l+"','"+log+"','"+p+"','"+r+"','"+d+"','"+e+"');";
                 
        try{
            statement = this.dbConnection.createStatement();
            int executed = statement.executeUpdate(query);
            if(executed ==1){
                success = true;
                
                
                 /**
                 * **********************DISPLAYS USER TABLE********************
                 */
                rst = statement.executeQuery(Display);
                mymeta = rst.getMetaData();
                columnNo = mymeta.getColumnCount();

                try {
                    //Displays Table Names
                    for (int i = 1; i <= columnNo; i++) {
                        System.out.print(mymeta.getColumnName(i) + "\t\t");
                    }
                    System.out.println();

                    //Displays Table data
                    while (rst.next()) {

                        for (int i = 1; i <= columnNo; i++) {
                            System.out.print(rst.getObject(i) + "\t\t\t");
                        }
                        System.out.println();
                    }

                } catch (SQLException sa) {
                    sa.getMessage();
                }

                /**
                 * **********************************************************************
                 */
            }
            statement.close();
            
        }catch(SQLException error){
            //this tested Good
            success = false;
        }finally{
            try{
                this.dbConnection.close();
                this.connectionEstablished = false;
            }catch(SQLException err){
                success = false;
                }
        }
        
        return success;
    }
    
    /*
    This function will be handy to find out if an email or a userName already
    exits in the database.  To check for the username, just call the function and 
    pass the parameter userName and second parameter "LoginID".  For email, pass
    the email and then pass the second flag, which is "email"
    */
    public boolean valueExists(String val,String valFlag){
        boolean exists = false;
        //get all the values in the userName colum of the User table
        String query = "";
        String columnName="";
        
        switch(valFlag){
            case "Email": query = "SELECT email FROM User";
                          columnName = "email";
                          break;
                
            case "LoginID": query = "Select userName FROM User";
                            columnName = "userName";
                            break;
                            
            //add any other case you need here
            default:
                    break;
        }
        
        if(this.ConnectDB()){
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                while(rst.next()){
                    if(rst.getString(columnName).equals(val)){
                        exists = true;
                    }
                }
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
        //iterate through and see if there is a match, set the flag accordingly
        
        return exists;
        
    }


    
    //Verify Password from Database
    public boolean verifyPassword(String userName, String password){
        boolean exists = false;
        //get all the values in the userName colum of the User table
        if(this.ConnectDB()){
            String query = "SELECT password FROM User WHERE userName = '"
                    + userName + "';";
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                while(rst.next()){
                    if(rst.getString("password").equals(password)){
                        exists = true;
                    }
                }
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
        //iterate through and see if there is a match, set the flag accordingly
        
        return exists;
        
    }
    
    public boolean isEmployee(String userName){
        boolean isEmp = false;
        //get all the values in the userName colum of the User table
        if(this.ConnectDB()){
            String query = "SELECT role FROM User WHERE userName = '"
                    + userName + "';";
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                while(rst.next()){
                    if(rst.getString("role").equals("Employee")){
                        isEmp = true;
                    }
                }
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
        //iterate through and see if there is a match, set the flag accordingly
        
        return isEmp;
        
    }
    
          //Method to update user
    public boolean UpdateUser(String f) {
 
        
        boolean success = false;
        Statement statement;
//        String query = "UPDATE `User` SET `lastName` = '" + f + "' WHERE `PK_userID` = '" + 93 + "'";
        String query = "UPDATE `User` SET `lastName` = '" + f + "' WHERE `firstName` = '" + f + "'";



        try {
            statement = this.dbConnection.createStatement();
            int executed = statement.executeUpdate(query);
            if (executed == 1) {
                success = true;

                /**
                 * **********************DISPLAYS USER TABLE********************
                 */
                rst = statement.executeQuery(Display);
                mymeta = rst.getMetaData();
                columnNo = mymeta.getColumnCount();

                try {
                    //Displays Table Names
                    for (int i = 1; i <= columnNo; i++) {
                        System.out.print(mymeta.getColumnName(i) + "\t\t");
                    }
                    System.out.println();

                    //Displays Table data
                    while (rst.next()) {

                        for (int i = 1; i <= columnNo; i++) {
                            System.out.print(rst.getObject(i) + "\t\t\t");
                        }
                        System.out.println();
                    }

                } catch (SQLException sa) {
                    sa.getMessage();
                }

                /**
                 * **********************************************************************
                 */
            }

            statement.close();

        } catch (SQLException error) {
            //this tested Good
            success = false;
        } finally {
            try {
                this.dbConnection.close();
                this.connectionEstablished = false;
            } catch (SQLException err) {
                success = false;
            }
        }

        return success;
    }
    
    
    
    //removing user from database
    public boolean RemoveUser(String f) {
        System.out.println(f);
        
        boolean success = false;
        Statement statement;
        String query = "DELETE FROM `User` WHERE `firstName` = '" + f + "'";
        

        try {
            statement = this.dbConnection.createStatement();
            int executed = statement.executeUpdate(query);
            if (executed == 1) {
                success = true;

                /**
                 * **********************DISPLAYS USER TABLE********************
                 */
                rst = statement.executeQuery(Display);
                mymeta = rst.getMetaData();
                columnNo = mymeta.getColumnCount();

                try {
                    //Displays Table Names
                    for (int i = 1; i <= columnNo; i++) {
                        System.out.print(mymeta.getColumnName(i) + "\t\t");
                    }
                    System.out.println();

                    //Displays Table data
                    while (rst.next()) {

                        for (int i = 1; i <= columnNo; i++) {
                            System.out.print(rst.getObject(i) + "\t\t\t");
                        }
                        System.out.println();
                    }

                } catch (SQLException sa) {
                    sa.getMessage();
                }

                /**
                 * **********************************************************************
                 */
            }

            statement.close();

        } catch (SQLException error) {
            //this tested Good
            success = false;
        } finally {
            try {
                this.dbConnection.close();
                this.connectionEstablished = false;
            } catch (SQLException err) {
                success = false;
            }
        }

        return success;
    }
    
//    public boolean removeUserFromDb(String userName){
//        
//        boolean deleted=false;
//        String deleteQuery = "DELETE FROM `User` WHERE `userName` = '"+userName+"'";
//        
//        
//        if(this.ConnectDB()){
//            //connected to the database
//           try{
//               Statement st=this.dbConnection.createStatement();
//               boolean deletedResult =st.execute(deleteQuery);
//               
//               if(deletedResult){
//                   
//                   deleted = true;
//               }
//               
//           }catch(SQLException e){
//               System.out.println(e);
//           }finally{
//               try{
//                  this.dbConnection.close();
//               }catch(SQLException e2){
//                   System.out.println(e2);
//               }
//           }
//        }
//        return deleted;
//        
//    }
    
    
    //method to add a turnback to the database
    public boolean addTurnbackToDB(int userID, int departmentID, 
            int turnbackCategoryID, int turnbackTypeID, String turnbackText){
         
        boolean success = false;
        Statement statement ;
      
       
       
        if(this.ConnectDB()){
            
               
            String query = "INSERT INTO TurnbackData (FK_userID, FK_departmentID, "
                    + "FK_turnbackCategoryID, FK_turnbackTypeID, turnbackText)"
                    + " VALUES (" + userID + ", " + departmentID + 
                    ", " + turnbackCategoryID + ", " + turnbackTypeID + ", '"
                    + turnbackText + "')";
           
                    
            try{
                statement = this.dbConnection.createStatement();
            int executed = statement.executeUpdate(query);
            if(executed ==1){
                success = true;
            }
           statement.close();
  
                
            }catch(SQLException e1){
                System.out.println(e1);
            }           
            
        }else{
            System.out.println("Could not connect to the database");
        }  
        return success;
    } 
    
     //Return Password from Database
    public String returnPassword(String email){
        boolean exists = false;
        String userpassword = "";
        //get all the values in the email colum of the User table
        if(this.ConnectDB()){
            String query = "SELECT password FROM User WHERE email = '"
                    + email + "';";
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                if(rst.next()){
                    exists = true;
                      userpassword = rst.getString("password");
                    }
                
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
       
       //iterate through and see if there is a match, set the flag accordingly  
   return userpassword;
    }
    
    public int getTurnbackCategoryID(String category){
        int turnbackCategoryID= 0;
       
        if(this.ConnectDB()){
            String query = "SELECT PK_turnbackCategoryID FROM TurnbackCategory WHERE" 
                + " turnbackCategoryName = '" + category + "';";
            
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                while(rst.next()){
                    
                      turnbackCategoryID = rst.getInt("PK_turnbackCategoryID");
                    }
                
                
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
      
        return turnbackCategoryID;
    }
    
    public int getTurnbackTypeID(String type){
        int turnbackTypeID= 0;
       
        if(this.ConnectDB()){
            String query = "SELECT PK_turnbackTypeID FROM TurnbackType WHERE" 
                + " turnbackTypename = '" + type + "';";
            
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                while(rst.next()){
                    
                      turnbackTypeID = rst.getInt("PK_turnbackTypeID");
                    }
                
                
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
       
        return turnbackTypeID;
    }
        public int getUserID(String login){
        int userID= 0;
       
        if(this.ConnectDB()){
            String query = "SELECT PK_userID FROM User WHERE" 
                + " userName = '" + login + "';";
            
            try{
                
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(query);
                
                
                while(rst.next()){
                    
                      userID = rst.getInt("PK_userID");
                      //System.out.println("REsult = " + userID);
                    }
                
                
                
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
       //System.out.println("User ID:" + userID);
        return userID;
    }
        
    public String getDepartmentName(String login){
        String departmentName= "";
        
       
        if(this.ConnectDB()){
            String queryName = "SELECT department FROM User WHERE" 
                + " userName = '" + login + "';";
            
                    
            try{
                Statement  st = this.dbConnection.createStatement();
                ResultSet rst = st.executeQuery(queryName);
                
                while(rst.next()){
                    
                      departmentName = rst.getString("department");
                    }
                
            }catch(SQLException e1){
                System.out.println(e1);
            }           
            
        }else{
            System.out.println("Could not connect to the database");
        }
       
       
        return departmentName;
    }  
    
        public int getDepartmentID(String departmentName){
        
        int departmentID = 0;
       
        if(this.ConnectDB()){
            
            String queryID = "SELECT PK_departmentID FROM Department WHERE" 
                    + " departmentName = '" + departmentName + "';";
                    
            
            try{
                
                Statement  st = this.dbConnection.createStatement();
              
                ResultSet rst = st.executeQuery(queryID);
                
                while(rst.next()){
                        
                      departmentID = rst.getInt("PK_departmentID");
                    }
                
            }catch(SQLException e1){
                System.out.println(e1);
            }
        }else{
            System.out.println("Could not connect to the database");
        }
//       System.out.println(departmentName);
//       System.out.println(departmentID);
        return departmentID;
    }
        
         //alert message for email not in DB
     public void alertInvalidEmail(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setHeaderText("Email is incorrect");

          alert.showAndWait();
       
 }
          //alert message for password not in DB
     public void alertInvalidPassword(){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setHeaderText("Password is incorrect");

          alert.showAndWait();
       
 }
     public boolean populateUserList(){
        
         //flag to let us know that everything went fine
         boolean isGood = false;
         //connect to the database
         if(this.ConnectDB()){
             //the connection is good
             //get all the users that are not locked out
             String query = "Select userName FROM User WHERE locked=0";
             
             try{
                 Statement st = this.dbConnection.createStatement();
                 ResultSet rst = st.executeQuery(query);

                 //iterate through the rst set and add all the usernames to the arraylist
                 while(rst.next()){
                     this.userNames.add(rst.getString("userName"));
                 }
                 
                 //if it gets this far, it means we got everything we are looking for
                 //flag to know that we can send return the arrayList
                 if(userNames.size()>0){
                     isGood = true;
                 }
             }catch(SQLException e){
                 isGood=false;
             }finally{
                 //close everything out
                 try{
                     this.dbConnection.close();
                     this.connectionEstablished = false;
                 }catch(SQLException err){
                     System.out.println(err);
                 }
             }
             
         }else{
             //the connection failed
             isGood=false;
         }
         
         return isGood;
         
     }
     
     public String[] getUserInformation(String userName){
         String[] infoArray = new String[5];
         if(this.ConnectDB()){
             //query selects all user information except for the password
             String query = "SELECT firstName,lastName,role,department,email FROM User WHere userName = '"+userName+"'";
             try{
                 Statement st = this.dbConnection.createStatement();
                 ResultSet r = st.executeQuery(query);
                 
                 //get the information out of teh database
                 while(r.next()){
                     for(int i = 1;i<=5;i++){
                         infoArray[i-1]= r.getString(i);
                     }
                 }
             }catch(SQLException e){
                 System.out.println(e);
             }finally{
                 try{
                     this.dbConnection.close();
                     this.connectionEstablished=false;
                 }catch(SQLException event){
                     System.out.println("There was an issue closing things");
                 }
             }
             
         }
         return infoArray;
     }
     
     public boolean updateUser(String fn,String ln,String em,String dept,String role,String userName){
         boolean updated = false;
         System.out.println("Updating user"+ userName);
         
         String query = String.format("UPDATE User SET firstName = \'%s\',lastName =\'%s\',email=\'%s\',department=\'%s\',role=\'%s\' WHERE userName =\'%s\'", fn,ln,em,dept,role,userName);
         //connect to the database
         if(this.ConnectDB()){
             //we connected to the database
             //create the statement
             try{
             Statement tempSt = this.dbConnection.createStatement();
             int result = tempSt.executeUpdate(query);
             
             if(result == 1){
                 updated = true;
             }
         }catch(SQLException e){
                 System.out.println(e);
                 }finally{
                 try{
                     this.dbConnection.close();
                     this.connectionEstablished=false;
                 }catch(SQLException e2){
                     System.out.println(e2);
                 }
             }
         }
         return updated;
     }
     //function locks out the user from the system
     public boolean lockUser(String userName){
         boolean isLocked = false;
         
         //connect to the database
        if(this.ConnectDB()){
         //connected to the database
         String query = String.format("UPDATE User SET locked=%d WHERE userName =\'%s\'",1, userName);
         try{
             Statement lockSt = this.dbConnection.createStatement();
             int lockResult = lockSt.executeUpdate(query);
             
             if(lockResult ==1){
                 System.out.println("User "+ userName+" was locked out");
                 isLocked = true;
             }
         }catch(SQLException e){
             System.out.println(e);
         }finally{
             try{
                 this.dbConnection.close();
                 this.connectionEstablished = false;
             }catch(SQLException e2){
                 System.out.println(e2);
             }
         }
        }
        
        return isLocked;
     }
     
     public boolean populateUsersByDepartment(String department){
         boolean listSet = false;
         this.departmentUsers = new ArrayList<String>();
         //establish a connection
         if(this.ConnectDB()){
             String query = String.format("SELECT userName FROM User WHERE department=\'%s\' AND locked =0",department);
             //run the query
             try{
                 Statement deptUsers = this.dbConnection.createStatement();
                 //execute the statement
                 boolean userRst = deptUsers.execute(query);
                 
                 
                 if(userRst){
                     ResultSet rst = deptUsers.getResultSet();
                     //iterate through the Result set obj
                     while(rst.next()){
                         this.departmentUsers.add(rst.getString("userName"));
                     }
                     
                     //check that the Arraylist is populated 
                     if(this.departmentUsers.size()>0){
                         listSet = true;
                     }else{
                         listSet = false;
                     }
                     
                 }else{
                     System.out.println("There was a problem retrieving the users from the database");
                 }
             }catch(SQLException e){
                 System.out.println(e);
             }finally{
                 try{
                     this.dbConnection.close();
                     this.connectionEstablished = false;
                 }catch(SQLException e2){
                     System.out.println(e2);
                 }
             }
         }
         return listSet;
         
     }
     //this will return the list that was set by the populateUsersByDepartment method
     public ArrayList<String> getDepartmentUsers(){
         return this.departmentUsers;
     }
     
     public boolean isLocked(String user){
         //connect to the database
         boolean isLocked=false;
         if(this.ConnectDB()){
             //we are connected so we need to query the column where
             //build the query
             String query = String.format("SELECT locked FROM User WHERE userName=\'%s\'",user);
             
             try{
                 //create the statement
                 Statement st= this.dbConnection.createStatement();
                 boolean rst = st.execute(query);
                 
                 //get the result
                 if(rst){
                     
                     ResultSet set = st.getResultSet();
                     int colVal =0;
                     while(set.next()){
                         colVal = Integer.parseInt(set.getString("locked"));
                     }
                     //check the value
                     if(colVal==1){
                         isLocked = true;
                         
                     }else{
                         
                         isLocked =false;
                     }
                     
                    
                }

             }catch(SQLException e){
                   System.out.println(e);
             }
         }else{
             System.out.println("Could not connect to database");
         }
         
         return isLocked;
     }

}  

