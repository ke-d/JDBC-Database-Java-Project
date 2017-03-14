//package cecs.pkg323.java.term.project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mimi Opkins with some tweaking from Dave Brown
 */
public class CECS323JavaTermProject {
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";
/**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public static void main(String[] args) {
       
        Scanner in = new Scanner(System.in);
        Statement stmt = null;
        databaseInput(in);
        Connection conn = connectToDB();
        String sel = displayOptions(in);
        displayResultSet(executeStatement(createStatement(sel), conn, stmt));
        
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static void databaseInput(Scanner input) {
        System.out.print("Name of the database (not the user account): ");
        DBNAME = input.nextLine();
        System.out.print("Database user name: ");
        USER = input.nextLine();
        System.out.print("Database password: ");
        PASS = input.nextLine();
    }
    
    public static String displayOptions(Scanner input) {
        System.out.println("1. List all writing groups.");
        System.out.println("2. User Input - to be built");
        System.out.println("3. List all publishers.");
        System.out.println("4. User Input - To be built");
        System.out.println("5. List all books.");
        
        String select = input.nextLine();
        return select;
       
    }
    
    public static Connection connectToDB() {
        Connection conn = null;
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;
        
        Statement stmt = null;  //initialize the statement that we're using
        
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            
            System.out.println("Succesfully connected to DB!");
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } 
         
        return conn;
    }
    
    public static String createStatement(String select) {
       String stmt = null;
        switch(select) {
           case "1":
               stmt = "SELECT * FROM WritingGroup";
               break;
           case "2":
               break;
           case "3":
               stmt = "SELECT * FROM Publisher";
               break;
           case "4":
               break;
           case "5":
               stmt = "SELECT * FROM Book";
               break;
           default:
				System.out.println("Invalid Input");
				break;
       }
        return stmt;
    }
    
    public static ResultSet executeStatement(String instr, Connection conn, Statement stmt) {
        ResultSet returnRS = null;
        try {
            stmt = conn.createStatement();
            returnRS = stmt.executeQuery(instr);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return returnRS;
    }
    
    public static void displayResultSet(ResultSet result) {
        ResultSetMetaData data = null;
        try {
            data = result.getMetaData();
            ArrayList<String> colNames = new ArrayList<>();
            
            //get default col size
            int maxSize = data.getColumnDisplaySize(1);
            //get column names
            for (int i = 1; i <= data.getColumnCount(); i++) {
                colNames.add(data.getColumnName(i));
                System.out.println(data.getColumnName(i) + ": " +  data.getColumnDisplaySize(i));
                if (data.getColumnDisplaySize(i)>maxSize) {
                    maxSize = data.getColumnDisplaySize(i);
                    System.out.println(maxSize);
                }
            }
            
            String displayFormat = "%-" + maxSize + "s";
          
            for (int i = 0; i<colNames.size(); i++) {
                System.out.printf(displayFormat, colNames.get(i));
            }
            //display columns
            while (result.next()) {
                for (int i = 0; i < colNames.size(); i++) {
                    System.out.printf(displayFormat, result.getString(colNames.get(i)));
                }
                System.out.println();
             }
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}



