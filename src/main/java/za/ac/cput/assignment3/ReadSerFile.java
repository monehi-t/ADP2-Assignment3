/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3;


import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Monehi Tuoane (219350744)
 */


public class ReadSerFile {
    
    //Create Your Arrays
    private ArrayList<Customer> customerArray = new ArrayList<>();
    private ArrayList<Supplier> supplierArray = new ArrayList<>();
    ObjectInputStream objectInputStream;
    
    //Add Writers
    BufferedWriter bufferW;
    FileWriter fileWriter;

    
    //Opens the file to read from.
    public void openFile() {
        try{
 
             objectInputStream = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
         } 
         catch(IOException e) {
             System.out.println("Error!! Could not open file! 1" + e.getMessage());
         }           
    }
    
    
    //Close file
    public void closeFile(){
        try{
            objectInputStream.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    
    //Read file
     public void readFile(){
        try{
           while(true){
               Object line = objectInputStream.readObject();
               String a ="Customer";
               String b = "Supplier";
               String name = line.getClass().getSimpleName();
               if ( name.equals(a)){
                   customerArray.add((Customer)line);
               } else if ( name.equals(b)){
                   supplierArray.add((Supplier)line);
               } else {
                   System.out.println("Not working. Something went wrong!");
               }
           } 
        }
        catch (EOFException eofe) {
            System.out.println("End of file reached");
        }
        catch (ClassNotFoundException ioe) {
            System.out.println("Class error reading ser file: "+ ioe);
        }
        catch (IOException ioe) {
            System.out.println("Error reading ser file: "+ ioe);
        }
        
        sortCustomer();
        sortSuppliers();
    }
    
    
     //Sort Customer in ascending order
    public void sortCustomer(){
        String[] customerID = new String[customerArray.size()];
        ArrayList<Customer> cArray= new ArrayList<Customer>();
        int count = customerArray.size();
        for (int i = 0; i < count; i++) {
            customerID[i] = customerArray.get(i).getStHolderId();
        }
        Arrays.sort(customerID);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (customerID[i].equals(customerArray.get(j).getStHolderId())){
                    cArray.add(customerArray.get(j));
                }
            }
        }
        customerArray.clear();
        customerArray = cArray;
    }
    
    
    //Calculating age of each customer
    public int determinetAge(String dob){
        String[] seperation = dob.split("-");
        
        LocalDate birthDate = LocalDate.of(Integer.parseInt(seperation[0]), Integer.parseInt(seperation[1]), Integer.parseInt(seperation[2]));
        LocalDate currentDate = LocalDate.now();
        Period difference = Period.between(birthDate, currentDate);
        int age = difference.getYears();
        return age;
    }
    
    
    //Changing the formate of the date of birth
    public String formatDob(Customer dob){
        LocalDate birthdayFormat = LocalDate.parse(dob.getDateOfBirth());
        DateTimeFormatter changeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return birthdayFormat.format(changeFormat);
    }
    
    
    //Display customer information
    public void displayCustomer(){
        try{
            fileWriter = new FileWriter("customerOutFile.txt");
            bufferW = new BufferedWriter(fileWriter);
            bufferW.write(String.format("%s\n","===============================Customers========================================"));
            
            bufferW.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
             bufferW.write(String.format("%s\n","================================================================================"));
            for (int n = 0; n < customerArray.size(); n++) {
                bufferW.write(String.format("%-15s %-15s %-15s %-15s %-15s \n", customerArray.get(n).getStHolderId(), customerArray.get(n).getFirstName(), customerArray.get(n).getSurName(), formatDob(customerArray.get(n)), determinetAge(customerArray.get(n).getDateOfBirth())));
            }
            bufferW.write(String.format("%s\n"," "));
            bufferW.write(String.format("%s\n"," "));
            bufferW.write(String.format("%s\n",rentCustomer()));
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bufferW.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    
    //Determing & printing number of customers that can/cannot rent
    public String rentCustomer(){
        int number = customerArray.size();
        int canRent = 0;
        int cannotRent = 0;
        for (int n = 0; n < number; n++) {
            if (customerArray.get(n).getCanRent()){
                canRent++;
            }else {
                cannotRent++;
            }
        }
        String line = "Number of customers who can rent a car : "+ '\t' + canRent + '\n' + "Number of customers who cannot rent a car : "+ '\t' + cannotRent;
        return line;
    }
    
    
    
    
    
    //Sort Suppliers in ascending order
    public void sortSuppliers(){
        String[] supplierID = new String[supplierArray.size()];
        ArrayList<Supplier> sArray= new ArrayList<Supplier>();
        int number = supplierArray.size();
        for (int n = 0; n < number; n++) {
            supplierID[n] = supplierArray.get(n).getName();
        }
        Arrays.sort(supplierID);
        
        for (int n = 0; n < number; n++) {
            for (int m = 0; m < number; m++) {
                if (supplierID[n].equals(supplierArray.get(m).getName())){
                    sArray.add(supplierArray.get(m));
                }
            }
        }
        supplierArray.clear();
        supplierArray = sArray;
    }
    
    
    //Display supplier information
     public void displaySupplier(){
        try{
            fileWriter = new FileWriter("supplierOutFile.txt");
            bufferW = new BufferedWriter(fileWriter);
            bufferW.write(String.format("%s\n","===========================SUPPLIERS=========================================="));
           
            bufferW.write(String.format("%-15s %-15s \t %-15s %-15s \n", "ID","Name","Prod Type","Description"));
            bufferW.write("==============================================================================\n");
            for (int i = 0; i < supplierArray.size(); i++) {
                bufferW.write(String.format("%-15s %-15s \t %-15s %-15s \n", supplierArray.get(i).getStHolderId(), supplierArray.get(i).getName(), supplierArray.get(i).getProductType(),supplierArray.get(i).getProductDescription()));
            }
            System.out.println("Supplier Text file created and information is displayed.");
            
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try{
            bufferW.close( ); 
        }
        catch (IOException ioe){            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
     
     public static void main(String args[])  {
        ReadSerFile obj=new ReadSerFile(); 
        obj.openFile();
        obj.readFile();
        obj.closeFile();
        obj.displayCustomer();
        obj.displaySupplier();

     } 
    
}

