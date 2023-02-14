package model;

import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String email;
    private final String lastName;
    String validEmail = "^(.+)@(.+).(.+)$";
    Pattern emailPattern = Pattern.compile(validEmail);

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        if(!emailPattern.matcher(email).matches()){
            throw new IllegalArgumentException("Invalid Email Address.");
        } else {
            this.email = email;
        }
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + " Last Name: " + lastName + " Email: " + email;
    }
}
