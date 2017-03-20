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
    static Scanner INPUT = new Scanner(System.in);
// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL;

    public static void main(String[] args) {
        Connection conn = connectToDB();
        String sel = displayOptions();
        while(!sel.equals("10")) {
            switch(sel) {
                case "1":
                    displayResultSet(executeStatement("SELECT * FROM WritingGroup", conn));
                    break;
                case "2":
                    displayResultSet(executePreparedStatement(conn, "SELECT * FROM WritingGroup WHERE GroupName = ?", getGroupInfo()));
                    break;
                case "3":
                    displayResultSet(executeStatement("SELECT * FROM Publisher", conn));
                    break;
                case "4":
                    displayResultSet(executePreparedStatement(conn, "SELECT * FROM Book WHERE BookTitle = ?", getBookInfo()));
                    break;
                case "5":
                    displayResultSet(executeStatement("SELECT * FROM Book", conn));
                    break;
                case "6":
                    prepareStatementForBookInsert(conn, "INSERT INTO BOOK (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) "
                            + "Values(?,?,?,?,?)");
                    break;
                case "7":
                    prepareStatementForBookRemove(conn, "DELETE FROM BOOK WHERE BookTitle = ?");
                    break;
                case "8":
                    insertNewPublisherAndReplace(conn, "INSERT INTO PUBLISHER (PublisherName, PublisherAddress, PublisherPhone, "
                        + "PublisherEmail) VALUES (?, ?, ?, ?)");
                    break;
                case "9":
                    prepareStatementForGroupInsert (conn, "INSERT INTO WRITINGGROUP(GroupName, HeadWriter, YearFormed, Subject) " +
                            "Values(?,?,?,?)");
                    break;
            }
            
            System.out.println();
            sel = displayOptions();
        
        }
        
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }  
        INPUT.close();
    }
 
    /**
     * Fetches the name and credentials of a database from the user.
     */
    
    public static void databaseInput() {
        System.out.print("Name of the database (not the user account): ");
        DBNAME = INPUT.nextLine();
        System.out.print("Database user name: ");
        USER = INPUT.nextLine();
        System.out.print("Database password: ");
        PASS = INPUT.nextLine();
    }
    /**
     * Displays all options the user has.
     * 
     * @return Returns a string of the selection number.
     */
    public static String displayOptions() {
        System.out.println("1. List all writing groups.");
        System.out.println("2. List all information from a specific writing group.");
        System.out.println("3. List all publishers.");
        System.out.println("4. List all information from a specific book.");
        System.out.println("5. List all books.");
        System.out.println("6. Insert a Book.");
        System.out.println("7. Remove a Book.");
        System.out.println("8. Insert a new publisher.");
        System.out.println("9. Insert a new group.");
        System.out.println("10. Exit");
        System.out.printf("\n%s", "Please enter your selection: ");
        String select = INPUT.nextLine();

        return select;
       
    }
    
    /**
     * Method to handling the connection to the database, dependent on user's input.
     * 
     * @return Returns a connection object to the database.
     */
    public static Connection connectToDB() {
        //Initializes the connection
        Connection conn = null;
        
        boolean exceptionThrown = false;
        
        do {
            
            //gets database information from the user
            databaseInput();
            
            //Constructing the database URL connection string
            DB_URL = "jdbc:derby://localhost:1527/" + DBNAME;
            
            //handles a username and password, if there's one
            if (!USER.isEmpty() && !PASS.isEmpty())
                DB_URL += ";user="+ USER + ";password=" + PASS;
            
            try {
                //STEP 2: Register JDBC driver
                Class.forName("org.apache.derby.jdbc.ClientDriver");

                //STEP 3: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL);

                System.out.println("Succesfully connected to DB!");
                exceptionThrown = false;
            } catch (SQLException se) {
                //Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, se);
                //handles issues with database connection
                System.out.println("Error connecting to database. Please check database connection and credentials.");
                exceptionThrown = true;
            } catch (Exception e) {
                //Handle errors for Class.forName
                System.out.println("Error. Please try again.");
                exceptionThrown = true;
            } finally {
                System.out.println();
            }
        } while (exceptionThrown);
      
        return conn;
    }
    
       
    public static void prepareStatementForBookInsert(Connection conn, String stmt) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean continueEntry = false;
        
        do {
            System.out.println();
            System.out.println("Enter the Group Name: ");
            String groupName = INPUT.nextLine();
            System.out.println("Enter the Book Title: ");
            String bookTitle = INPUT.nextLine();
            System.out.println("Enter the Publisher Name: ");
            String publisherName = INPUT.nextLine();
            System.out.println("Enter the Year Published: ");
            String yearPublished = INPUT.nextLine();
            System.out.println("Enter the Number of Pages: ");
            String numberPages = INPUT.nextLine();
        
            try {
                pstmt.setString(1, groupName);
                pstmt.setString(2, bookTitle);
                pstmt.setString(3, publisherName);
                pstmt.setString(4, yearPublished);
                pstmt.setString(5, numberPages);
                continueEntry = false;
                pstmt.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException ex) {
                //System.out.println(ex.getMessage());
                if(ex.getMessage().contains("BOOK_FK01")) {
                    System.out.println("ERROR: Group does not exist in database.");
                } else if (ex.getMessage().contains("BOOK_FK02")) {
                    System.out.println("ERROR: Publisher does not exist in database.");
                }
                System.out.println("Want to try again? (Y/N)");
                String input = INPUT.nextLine();
                input = input.toLowerCase();
                continueEntry = (input.equals("y"));
            } catch (SQLDataException ex) {
        
                System.out.println("ERROR: Entered not an integer for number of pages or the year. Please enter as an integer.");
                
                System.out.println("Want to try again? (Y/N)");
                String input = INPUT.nextLine();
                input = input.toLowerCase();
                continueEntry = (input.equals("y"));
            } catch (SQLException ex) {
                System.out.println("Encountered error.");
            } finally {
                System.out.println();
            }
        } while(continueEntry); 
    }
    
        public static void prepareStatementForBookRemove(Connection conn, String stmt) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println();
        System.out.println("Enter the Book Title: ");
        String bookTitle = INPUT.nextLine();
            
        try {
            pstmt.setString(1, bookTitle);
            int result = pstmt.executeUpdate();
            if(result == 1) {
                System.out.println(bookTitle + " was removed from the database.");
            } else {
                System.out.println(bookTitle + " does not exist in the database.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Method to handle the insertion and following replacement of a current publisher in its books.
     * 
     * @param conn Connection to the database.
     * @param stmt The prepared statement to be execute.
     */
    public static void insertNewPublisherAndReplace (Connection conn, String stmt) {
        
        //initializes prepared statement object
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(stmt);
        } catch (SQLException ex) {
            System.out.println("Error regarding prepared statement.");
        }
        String name = null;
        try {
            System.out.println("Please enter the publisher name: ");
            name = INPUT.nextLine();
            pstmt.setString(1, name);
            System.out.println("Please enter the publisher address: ");
            String addr = INPUT.nextLine();
            pstmt.setString(2, addr);
            System.out.println("Please enter the publisher phone number: ");
            String phone = INPUT.nextLine();
            pstmt.setString(3, phone);
            System.out.println("Please enter the publisher email: ");
            String email = INPUT.nextLine();
            pstmt.setString(4, email);
        } catch (SQLException ex) {
            System.out.println("Database error.");
        }
        
        try {
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println("Encountered database error while trying to execute instruction.");
        }
        
        displayResultSet(executeStatement("SELECT PublisherName FROM Publisher", conn));
        
        replacePublishers(conn, name);
    }

    /**
     * Replaces publisher in its books.
     * 
     * @param conn Connection to database.
     * @param pubName The name of the publisher to be replaced.
     */
    
        public static void prepareStatementForGroupInsert (Connection conn, String stmt) {
        
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(stmt);
        } catch (SQLException ex) {
            Logger.getLogger(CECS323JavaTermProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        String name = null;
        try {
            System.out.println("Please enter the group name: ");
            name = INPUT.nextLine();
            pstmt.setString(1, name);
            System.out.println("Please enter the head writer: ");
            String writer = INPUT.nextLine();
            pstmt.setString(2, writer);
            System.out.println("Please enter the year formed: ");
            String year = INPUT.nextLine();
            pstmt.setString(3, year);
            System.out.println("Please enter the group subject: ");
            String subject = INPUT.nextLine();
            pstmt.setString(4, subject);
            
            int result = pstmt.executeUpdate();
            if(result == 1) {
                System.out.println(name + " added.");
            } else {
                System.out.println(name + " not added.");
            }
        } catch (SQLDataException ex) {
            System.out.println("Year is not in the right format.");
        } catch (SQLException ex) {
            System.out.println("Database error.");
        } 
    
    }
    

    public static void replacePublishers (Connection conn, String pubName) {
        PreparedStatement pstmt = null;
        
        //create
        try {
            pstmt = conn.prepareStatement("UPDATE BOOK SET PublisherName = ? WHERE PublisherName = ?");
        } catch (SQLException ex) {
            System.out.println("Encountered an error while replacing publisher.");
        }
        
        
        System.out.println("Which publisher should be replaced? (All books will be updated.)");
        String oldPub = INPUT.nextLine();
        
        try {
            pstmt.setString(1, pubName);
            pstmt.setString(2, oldPub);
            pstmt.execute();
            System.out.println("Publisher successfully added and replaced.");
        } catch (SQLException ex) {
            System.out.println("Encountered an error while replacing publisher.");
        }
        
    }
    
    /**
     * Method to fetch the name of the group from the user.
     * 
     * @return Returns the name of the group that the user entered.
     */
    public static String getGroupInfo () {
        System.out.println("Please enter the group name: ");
        String name = INPUT.nextLine();
        return name;      
    }
    
    /**
     * Method to fetch the name of the book from the user.
     * 
     * @return Returns the name of the book the user entered.
     */
    public static String getBookInfo() {
        System.out.println("Please enter the book name: ");
        String name = INPUT.nextLine();
        return name;
    }
    
    /**
     * Executes a prepared statement that has only one String bind variable.
     * 
     * @param conn Connection to database.
     * @param stmt Prepared statement to be executed.
     * @param bindVar Bind variable for prepared statement.
     * @return 
     */
    public static ResultSet executePreparedStatement(Connection conn, String stmt, String bindVar) {
        ResultSet returnSet = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(stmt);
            pstmt.setString(1, bindVar);
            returnSet = pstmt.executeQuery();
        } catch (SQLException ex) {
             System.out.println("Execution error.");
        }
        return returnSet;
    }
    
    /**
     * Executes a regular non-prepared statement.
     * 
     * @param instr String of the statement to be executed.
     * @param conn Database connection that is being acted upon by statement.
     * @return Returns resultSet of that statement on the database.
     */
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
    
    /**
     * Displays a result set.
     * @param result Result set to be displayed.
     */
    
    public static void displayResultSet(ResultSet result) {
        ResultSetMetaData data = null;
        
        try {
            //throws exception if no results
            result.isFirst();
        } catch (SQLException ex) {
            //we know it's because there's no results so just output that fact.
            System.out.println("No result found!");
            return;
        }
        
        
        
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
            System.out.println();
            //display column headings
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
            System.out.println();
        } catch (SQLException ex) {
            System.out.println("Error encountered while displaying results.");
        }

    }

}
    




