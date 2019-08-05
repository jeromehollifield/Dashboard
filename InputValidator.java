/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classess;

import javafx.scene.text.Text;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 *
 * @author User
 * Description:  This is a helper class that will be used to validate input from
 * the user when first creating an account(New User).  This might be a handy class
 * to have throughout the entire program's development.
 */
public class InputValidator {
        
        private int minRange;
        private int maxRange;
        public InputValidator(){
            this.minRange = 8;
            this.maxRange = 16;
        }
        
        
        /*
        Function: validateName()
        Description: Validates names, can also be used to validate that an input
                     is all characters.
        Inputs: String, Text obj
        Return: None -> sets label in the form as feedback
        */
        public void validateName(String name, Text feedback){
            boolean isValid = true;
            String feedbackMessage ="";
            //first check that the name received is not empty
            if(name.length() == 0){
                isValid = false;
                feedbackMessage = "Required";
            }else{
                //go through the entire string and make sure that they are only letters
                for(int i =0;i<name.length();i++){
                    if(Character.isAlphabetic(name.charAt(i))){
                        continue;
                    }else{
                        isValid = false;
                        feedbackMessage = "Invalid";
                        break;
                    }
                }
            }
            if(!(isValid)){
                feedback.setText(feedbackMessage);
            }else{
                feedback.setText("");
            }
            
        }
        
        /*
        Function: validateName()
        Description: validates email fields by comparing them both and using regex 
        Inputs: String(email 1), String (email 2), Text obj
        Return: None -> Sets feedback label passed as parameter
        */
        public void validateEmails(String email,String confirmationEmail,Text feedback){
        //first make sure that they are not empty
        if(email.length() ==0 || confirmationEmail.length() ==0){
            feedback.setText("One of more email fields is empty");
        }else{
            //if the emails are not empty, next step is to make sure that they 
            //are both equal to each other
            if(email.equals(confirmationEmail)){
                /*if both emails are equal, we move to the last step, which 
                *is to verify the emails are valid (check their format)
                */
                if(this.isValidEmailFormat(email)){
                    //this means that the emails are valid
                    feedback.setText("");
                }else{
                    feedback.setText("Email is invalid");
                }
            }
            else{
                //this means that the emails were not equal
                feedback.setText("Emails do not match");
            }
        }
            
       }
          /*
        Function: validateLoginID()
        Description: validates login ID entered by the user
        Inputs: String,String,Text obj
        Return: None -> Sets feedback label passed as parameter
        */
        public void validateLoginID(String loginId,String loginIdConfirmation,Text feedback){
            //check that both fields are not empty
            if(loginId.length() == 0 || loginIdConfirmation.length()==0){
                feedback.setText("One or more fields missing");
            }else{
                //if they are not empty check that they are both equal
                if(loginId.equals(loginIdConfirmation)){
                    //now check that they are within range
                    if(loginId.length()>=this.minRange && loginId.length() <=this.maxRange){
                        /*if within range, now test that the format is acceptable
                        call the isValidLoginFormat(), it will return either
                        an empty string or a message of what went wrong.  Empty
                        string means nothing was found.
                        */
                        
                        //now set the returned string to the feedback label
                        String validatorFeedback = isValidLoginFormat(loginId);
                        feedback.setText(validatorFeedback);
                        
                    }else{
                        if(loginId.length()< minRange){
                            feedback.setText("Too short");
                        }else{
                            feedback.setText("Too long");
                        }
                    }
                }else{
                    feedback.setText("Fields don't match");
                }
            }
           
        }
         /*
        Function: validatePassword()
        Description: checks if password is acceptable and sets the feedback text field accordingly if not
        Inputs: String,String, Text obj
        Return: None
        */
        public void validatePassword(String password,String passwordConfirmation,Text feedback){
            //check that the password fields are not empty
            if(password.isEmpty() || passwordConfirmation.isEmpty()){
                feedback.setText("One or more required fields missing");
            }
            else{
                feedback.setText("");
                //next check that both passwords match
                if(password.equals(passwordConfirmation)){
                    //check that the ranges are good
                    if(password.length() <minRange){
                        feedback.setText("Too short");
                    }
                    else if(password.length() > maxRange){
                        feedback.setText("Too long");
                    }else{
                        //******************************************************
                        //if we get this far, it means good so far
                        //how call the private function to check on the format
                        //String pwdFormatFeedback= this.isValidPasswordFormat(password);
                       // feedback.setText(pwdFormatFeedback);
                       //*****************
                       String formatFeedback = this.isValidPasswordFormat(password);
                       feedback.setText(formatFeedback);
                       
                    }
                }
                else{
                    feedback.setText("Passwords don't match");
                }
                
            }
            
        }
        
        
        //------------------Private methods-------------------------------------
         /*
        Function: isValidEmailFormat()
        Description: Uses regex to check that the patter matches email pattern
        Inputs: String
        Return: Boolean 
        */
        private boolean isValidEmailFormat(String email){
            //using a regex i found online that is commonly used for email validation
            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            boolean isValid= Pattern.compile(regex).matcher(email).matches();  
            
            return isValid;
        }
        
        /*
        Function: isValidLoginFormat()
        Description: Checks the login for the right format
        Inputs: String
        Return: String
        */
        private String isValidLoginFormat(String loginId){
            //use a regext to check that the format matches
            /*the string has to have:
            1.at least one upper case
            2.at least one lower case
            3.at least one number
            Normally a regext would be usefull, but have to brush up on it
            */
            boolean isValid = true;
            int upperCases =0;
            int lowerCases =0;
            int numbers =0;
            String message ="";
            
            //make sure it starts with a character
            //make sure it doesn't have any  symbols
            
            //check that the first character is a letter
            if(Character.isAlphabetic(loginId.charAt(0))){
                /*
                Now check for symbols with a quick iteraction
                */
                for(int i=0;i<loginId.length();i++){
                    //***************************BUG HERE**********************
                    if(Character.isAlphabetic(loginId.charAt(i)) || Character.isDigit(loginId.charAt(i))){
                        //this means that somethign bad was found
                        continue;
                    }else{
                        isValid = false;
                        message = "Special characters not allowed";
                        break;
                    } 
                }
                //check that so far we are valid
                if(isValid){
                    //this means no special characters were found
                        //loop through and get a upper case, lower case, and number count
                        for(int j=0;j<loginId.length();j++){
                            if(Character.isAlphabetic(loginId.charAt(j))){
                                //this is a character so find out if its upper case or lower case
                                if(Character.isUpperCase(loginId.charAt(j))){
                                    upperCases+=1;
                                }else{
                                    lowerCases+=1;
                                }
                            }else if(Character.isDigit(loginId.charAt(j))){
                                numbers+=1;
                            }
                        }
                        //after the loop, now check all three counts
                        //check how may upper cases
                        if(!(upperCases >0)){
                            message = "Must have at least one upper case.";
                        }
                        //check how many lower cases
                        if(!(lowerCases >0)){ 
                            message = "Must have at least one lower case.";
                        }
                        //check how many numbers
                        if(!(numbers >0)){
                            
                            message = "Must have at least a number.";
                        }
                    }
                
            }else{
                //this means that the first character was not a letter
                message ="Login ID must start with a letter.";
            }
                        
            return message;
        }
        /*
        Function: isValidPasswordFormat()
        Description: matches password format
        Inputs: String
        Return: Boolean 
        */
        private String isValidPasswordFormat(String password){
            boolean isValid = true;
            int upCase =0;
            int loCase =0;
            int nums =0;
            int symbols =0;
            String message="";
            /*we are going to make sure that:
            1. password has at least one upper case
            2. password has at least one lower case
            3. password has at least one number
            4. password has at least one symbol
            5. Using the newest NIST guidance on passwords.  Before, there werew
            symbols that were considered invalid.  However, its up to the programmer
            to be able to handle all symbols so that they are not recognized as executable
            code in the database.
            */
            //char[] sumbos = {"?","/","","?","/","","?","/",""};
            //check the first character and make sure its a letter
            if(Character.isAlphabetic(password.charAt(0))){
                //cont all all the numbers
                for(int i = 0;i<password.length();i++){
                    
                    //if number
                    if(Character.isDigit(password.charAt(i))){
                        nums +=1;
                    }
                    
                    //if letter
                    else if(Character.isAlphabetic(password.charAt(i))){
                        if(Character.isUpperCase(password.charAt(i))){
                            upCase +=1;
                        }
                        if(Character.isLowerCase(password.charAt(i))){
                            loCase +=1;
                        }
                        
                    }
                    //symbols
                    else{
                        symbols +=1;
                    }

                    
                }
                
                //check the results and set the message accordingly
                if(upCase == 0){
                    message = "At least one upper case needed";
                }
                if(loCase == 0){
                    message = "At least one lower case needed";
                }
                if(symbols==0){
                    message = "At least one Symbol is needed";
                }

            }
                
            else{
                message = "First character must be a letter";
            }
            
            
           
            return message;
            
        }

    
}
