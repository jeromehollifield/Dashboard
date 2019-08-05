package classess;

import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. This is a test to see if it updates
 */
/**
 *
 * @author User Description: This class is going to be used to hold
 * data on the new user Once all the data has been set for that object, a method
 * within this class can be used to add the new user to the database
 */
public class NewUser {

    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String role;
    private String password;
    private String loginId;

    //Connectivity class instance
    Connectivity connectivity;
    
    //default constructor
    public NewUser() {
        this("", "", "", "", "");
    }

    public NewUser(String firstName, String lastName, String email,
            String department, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.role = role;

    }

    //setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginID(String loginId) {
        this.loginId = loginId;
    }

    //function to add the new user information to the database
    public boolean addUserToDatabase() {
        boolean success = true;
        //get all the information for the database
        String first = this.getFirstName();
        String last = this.getLastname();
        String email = this.getEmail();
        String pwrd = this.getPassword();
        String dept = this.getDepartment();
        String role = this.getRole();
        String logId = this.getLoginID();
        
        //make an array of all this data and send it to the Connectivity class instance
        Connectivity tempConnection = new Connectivity();
        
        /*if the connection to the database is established, then proceed to adding
        the user data
        */
        if(tempConnection.ConnectDB()){
           /*once connected call the .addNewUser in the Connection class to add
           the information to the database
           */
           //if the user was not adde, then return the false flag
           if(!(tempConnection.addNewUser(first,last,email,pwrd,dept,role,logId))){
               success = false;
           }
            
            
        }else{     
            //this means there was a problem with connecting to the database
            success = false;
        }
       
        return success;
    }

    //depending on the usage, some or all of these methods could be made private
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastname() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getRole() {
        return this.role;
    }

    public String getPassword() {
        return this.password;
    }

    public String getLoginID() {
        return this.loginId;
    }

    private void printReport() {
        System.out.println(this.firstName);
        System.out.println(this.lastName);
        System.out.println(this.department);
        System.out.println(this.email);
        System.out.println(this.role);
    }

}
