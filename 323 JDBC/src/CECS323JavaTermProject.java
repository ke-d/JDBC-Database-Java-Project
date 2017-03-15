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
    static String BINDVARIABLE;
    static Scanner INPUT = new Scanner(System.in);
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
        databaseInput();
        Connection conn = connectToDB();
        String sel = displayOptions();
        while(sel != "8") {
            String bindVar = null;
            switch(sel) {
                case "2":

                case "4":

                case "5":
                    displayResultSet(executeStatement(createStatement(sel), conn));
                    break;
                case "6":
                    prepareStatementForBookInsert(conn, createStatement(sel));
                    break;
                case "7":
                    prepareStatementForBookRemove(conn, createStatement(sel));
                    break;
            }

            sel = displayOptions();
        }
        
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static void prepareStatementForBookInsert(Connection conn, String stmt) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Enter the Group Name: ");
        String groupName = INPUT.nextLine();
        System.out.println("Enter the Book Title: ");
        String bookTitle = INPUT.nextLine();
        System.out.println("Enter the Publisher Name: ");
        String publisherName = INPUT.nextLine();
        System.out.println("Enter the Year Published: ");
        String yearPublished = INPUT.nextLine();
        System.out.println("Enter the Number of Pages: ");
        int numberPages = INPUT.nextInt();
            
        try {
            pstmt.setString(1, groupName);
            pstmt.setString(2, bookTitle);
            pstmt.setString(3, publisherName);
            pstmt.setString(4, yearPublished);
            pstmt.setInt(5, numberPages);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        public static void prepareStatementForBookRemove(Connection conn, String stmt) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Enter the Book Title: ");
        String bookTitle = INPUT.nextLine();
            
        try {
            pstmt.setString(1, bookTitle);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    

    public static void databaseInput() {

        System.out.print("Name of the database (not the user account): ");
        DBNAME = INPUT.nextLine();
        System.out.print("Database user name: ");
        USER = INPUT.nextLine();
        System.out.print("Database password: ");
        PASS = INPUT.nextLine();
    }
    
    public static String displayOptions() {
        System.out.println("1. List all writing groups.");
        System.out.println("2. User Input - To be built");
        System.out.println("3. List all publishers.");
        System.out.println("4. User Input - To be built");
        System.out.println("5. List all books.");
        System.out.println("6. Insert a Book.");
        System.out.println("7. Remove a Book.");

        String select = INPUT.nextLine();

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
               stmt = "SELECT * FROM WritingGroup WHERE GroupName = ?";
               break;
           case "3":
               stmt = "SELECT * FROM Publisher";
               break;
           case "4":
               stmt = "SELECT * FROM ?";
               break;
           case "5":
               stmt = "SELECT * FROM Book";
               break;
           case "6":
                stmt = "INSERT INTO BOOK (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) Values(?,?,?,?,?)";
                break;
            case "7":
                stmt = "DELETE FROM BOOK WHERE BookTitle = ?";
                break;
           default:
               System.out.println("Invalid Input");
               break;
       }
        return stmt;
    }

    public static ResultSet executeStatement(String instr, Connection conn) {

        ResultSet returnRS = null;
        try {
            
        
                Statement stmt = conn.createStatement();
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
                if (data.getColumnDisplaySize(i)>maxSize) {
                    maxSize = data.getColumnDisplaySize(i);  
                }
            }
            
            String displayFormat = "%-" + maxSize + "s";
          
            for (int i = 0; i<colNames.size(); i++) {
                System.out.printf(displayFormat, colNames.get(i));
            }
            System.out.println();
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
    
    public static String fetchUserSelection(String obj) {
        System.out.println("Please enter the name of the " + obj + ": ");
        String bindvar = INPUT.nextLine();
        return bindvar;
    }
}
    




