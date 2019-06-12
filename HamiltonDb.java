// Look at Hyperion pdf on refactoring and "bad smells"
// put THIS version on getHub, then update it and put the next version on
// Add an "Alex" object, or an "Alex" and "Eliza" (or "Burr") object, with 
// a variable for each Db column.  Requires a bunch of getters/setters
// Re-think my methods.  They should be based, not on the 5 user choices, but
// on the desired functionality of the program.  

//I need to rent space on a public database, so that users of this program (think GitHub, think employers)
// can just be sent to it by a browser.  This would also show that I can do this further step


// Hmm, in the python iris program, it looks like there is an array set up, each member of which is itself
// an array of 4 members.  And there is some sort of category to it.  Each of the four members of the subarray
// is an attribute of the flower, and labelled as such somehow.  So... should I create some sort of Hamilton object
// that has my 7 members, organized by attribute?

// ?create a HamiltonDb object, with 7 variables corresponding to 7 Db fields?

// columns:  id, title, author, date, qty, description, link to reviews  (In future: price,  picture/image)

// In future:  checkMembership() checks for field AND creates a new object (or updates an existing object) 
// with instance variables taken from the SQL database.  Then this HamiltonDb object ("Alex"?) exists anywhere
// in the program.  Is visible to whole class.  

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;		

import java.util.Scanner;
import javax.swing.JOptionPane;

//import java.sql.*;


public class HamiltonDb {
	// set number of milliseconds for time delay used by method rollPrint()
	static int fast = 2;
	static int slow = 10;

	// Instance variables
	private int id;
	private String title;
	private String author;
	private int pubDate;
	private int qty;
	private String info;
	private String review;
	
	// CONSTRUCTOR
	public HamiltonDb() {		// no variables within parentheses...yet
		this.id = id;
		this.title = title;
		this.author = author;
		this.pubDate = pubDate;
		this.qty = qty;
		this.info = info;
		this.review = review;
	}
	
	//  FOURTEEN(14) GETTER/SETTERS
	public int getId() {
		return id; 	
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getPubDate() {
		return pubDate;
	}
	
	public void setPubDate(int pudDate) {
		this.pubDate = pubDate;
	}
	
	public int getQty() {
		return qty;
	}
	
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		info = info;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview() {
		this.review = review;
	}
	

	// used by rollPrint() to determine speed of rolling
	// also used to create momentary pauses (i.e. half a second) to improve UI
	public static void waitForIt(int millisec) {
		try {
			Thread.sleep(millisec);
		}  catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	// Printed output appears character-by-character (quickly), in a rolling fashion.
	// This is done to improve the user's experience when going back and forth between console and 
	// dialog box. The moving line draws the eye back to the console, shows that something has in fact happened there.
	public static void rollPrint(String msg, int interval) { 	//does not create new line after printing
		//fast = 2; 
		for (int i = 0; i < msg.length(); i++) {
			System.out.print(msg.charAt(i));
			waitForIt(interval);
			//fast += 2;	 // there is potential here for having fun with varying speeds, possibly under user control. 
		}
	}

	
	// called by ensureDigit()
	// takes String as arg, returns boolean:  Does the string have only digits?
	public static boolean checkDigit(String perp) {
		boolean isAllDigit = true;
		if (perp.equals("")) {	// handles case where perp == ""
			isAllDigit = false;
			return isAllDigit;
		}
		for (int i = 0; i < perp.length(); i++) {	// handles case where perp == "eggplant" or "123f" ...
			if (perp.charAt(i) - 48 < 0  || perp.charAt(i) - 48 > 9) {
				isAllDigit = false;
				break;
			}
		}
		return isAllDigit;
	}
	
	// takes a user string, calls checkDigit() to check if each character is a digit.  If all digit, ensureDigit's while-loop 
	// is skipped, string is converted to int and returned.  If NOT all digit, a new string is demanded until user gives one 
	// that IS all int, and then that string is converted to int type, and returned to caller
	public static int ensureDigit(String userStr) {
		while (checkDigit(userStr) == false) {
			rollPrint("'" + userStr + "' is not a valid number.  Please try again: ", fast); 
			Scanner scanObj = new Scanner(System.in);
			userStr = scanObj.nextLine();
		}
		int userInt = Integer.parseInt(userStr);
		return userInt;
	}
	
	// called by methods to check whether a requested item in the database is ACTUALLY in the database.  
	// If not, returns false.  (This method can print message to user, but does not take input)
	// -- column var can refer to either title or author --
	public static boolean checkMembership(String column, String userEntry, Statement stmt) throws SQLException {
		boolean isMember = false;
		ResultSet rset = stmt.executeQuery("SELECT " + column + " FROM books");	// SQL query statement
		while (rset.next()) {	// check each name (name of title, or of author) against userEntry
			String name = rset.getString(column);
			if (userEntry.equalsIgnoreCase(name)) {
				isMember = true;	// method will return true only if userEntry finds a match in database
			}
		}
		if (isMember == false) {
			rollPrint(" --Sorry, the database does not contain that item. Please try again--\n\n", fast);
		}
		return isMember;
	}
	
	
	// user choice 2. The simplest of the 5 methods called by choose()
	// The method does not take input from user, just prints out list of book titles. 
	public static void viewAll(Statement stmt) throws SQLException {
		System.out.println("\t\t-- VIEW ALL TITLES -- ");
		
		// former location of try-catch block
		rollPrint("Here is a list of all books in the database: ", fast);
		ResultSet rset = stmt.executeQuery("SELECT title FROM books");	// SQL query statement
		while (rset.next()) {
			String title = rset.getString("title");
			rollPrint("\n\t" + title, fast);
		}
		System.out.println();
	}
	
	
	// search() is user choice 1.  search() and delete() are sister methods.
	// Each has two(2) ways to handle problems:  (1) calls checkMembership(), (2) Return to caller if user types empty string ("").
	public static void search(Statement stmt) throws SQLException {
		System.out.println("\t\t-- SELECT BOOK -- ");
		waitForIt(750);
		
		
		// STEP 1.  Ask user which field (author or title) they wish to search
		// Results are stored in column variable
		String fieldString = "badInput";	// initialize fieldString to an unacceptable value, to let while-loop begin.
		// check: string longer than one character?  ASCII value of 1st character is out of range for the 2 allowable choice digits?
		while (fieldString.length() != 1  ||  fieldString.charAt(0) - 48 < 1  || fieldString.charAt(0) - 48 > 2) {
			fieldString = JOptionPane.showInputDialog("Do you want to search by author or title? \nEnter 1 or 2 \n\n1. author \n2. title ");		
			if (fieldString == null) {return; } // click "cancel" --> JOptionPane sets fieldstring to null --> return, to choose().	
		}
		int fieldNum = Integer.parseInt(fieldString);	// at this point, fieldNum is guaranteed to be either 1 or 2
		
		// determine column variable
		String column = null;	// initialize column, which will be set below based on fieldNum
		if (fieldNum == 1) {
			column = "author"; // "author" is a pre-defined column of the books database
		}
		else if (fieldNum == 2) {
			column = "title"; // "title" is a pre-defined column of the books database
		}
		
		
		// STEP 2. Get user search choice (author or title), call checkMembership() to verify book in Db, loop till get good choice.
		// Results are stored in toSearch variable
		boolean isMember = false;
		String toSearch = null;	// initialize toSearch, for user entry
		while (isMember == false) {		// keep looping as long as user keeps entering books that AREN'T in the Db	
			// prompt user to type name of author (or title)  
			if (column == "author") {
				rollPrint("Using format Lastname, Firstname... ", fast);
			}
			rollPrint("Type in the " + column + " you wish to search (to go back press <ENTER> ): ", fast);
			Scanner scanObj = new Scanner(System.in);
			toSearch = scanObj.nextLine();
			
			if (toSearch.equals("")) {return; }	// if user hits <ENTER>, return to choose()
			
			isMember = checkMembership(column, toSearch, stmt);  // call checkMembership() to see if requested book (or author) is in Db.			
		}
		
		
		// STEP 3.  JDBC sends an SQL query, user is notified of results
		
		// SQL query.    .replace used in case user input contains single quote '
		ResultSet rset = stmt.executeQuery("SELECT * FROM books WHERE " + column + " = '" + toSearch.replace("'", "''") + "' " );
		rset.next();
		// generate string
		String record = "\tID #: " + rset.getInt("id")
			+ "\n\tTitle: " + rset.getString("title") 
			+ "\n\tAuthor: " + rset.getString("author") 
			+ "\n\tPublication Date: " + rset.getInt("pubDate") 
			+ "\n\tQuantity in Stock: " + rset.getInt("qty") 
			+ "\n\tDescription: " + rset.getString("info") 
			+ "\n\tWebsite Book Review: " + rset.getString("review");
			
		// print message to user
		rollPrint(" --The requested record is: \n" + record, fast);
		System.out.println();	
	}
	
	
	// delete() is user choice 5.  delete() and search() are sister methods.
	// Each has two(2) ways to handle problems: (1) calls checkMembership(),  (2) Return to caller if user types empty string ("").
	public static void delete(Statement stmt) throws SQLException {
		System.out.println("\t\t-- DELETE BOOK -- ");

		// STEP 1.  Get user choice of title to delete, call checkMembership() to verify book in Db, loop till get good choice.
		// Results are stored in toTrash variable		
		boolean isMember = false;
		String toTrash = null;	// initialize variable for user entry
		while (isMember == false) {	// keep looping as long as user keeps entering books that AREN'T in the Db 
			rollPrint("If you're ready to delete a book permanently, type the book's title (press <ENTER> to exit): ", fast);
			Scanner scanObj = new Scanner(System.in);
			toTrash = scanObj.nextLine();
				
			if (toTrash.equals("")) {return; }	// if user hits <ENTER>, return to choose() 

			isMember = checkMembership("title", toTrash, stmt);	// call checkMembership method to see if requested book is in Db
		}
		
		
		// STEP 2.  JDBC sends an SQL query, user is notified of results
		// SQL query.   .replace used in case user input includes single quote '
		stmt.executeUpdate("DELETE FROM books WHERE title = '" + toTrash.replace("'", "''") + "'");
		waitForIt(1000);
		rollPrint(" --The requested record has been deleted.", fast);
		System.out.println();
	}
	
	
	// update() is user choice 3.  update() and insert() are sister methods.  They make ADDITIONS to the database.
	// Each has two(2) ways to handle problems: (1) calls ensureDigit(),   (2) Return to caller if user types empty string ("")
	// AND, update() has a third(3) way to handle problems:  (3) calls checkMembership()
	// 			-- THIS METHOD IS TOO BIG, TOO FULL, NEEDS TO BE BROKEN UP !! --
	public static void update(Statement stmt) throws SQLException {
		System.out.println("\t\t-- UPDATE -- ");

		// STEP 1.  Get user choice of title to update, call checkMembership() to verify book in Db, loop till get good choice.
		// Results are stored in toUpdate variable
		boolean isMember = false;
		String toUpdate = "";	// initialize toUpdate, for user entry
		while (isMember == false) {
			rollPrint("Type in the title of the book whose field you wish to update (or press <RETURN> to exit): ", fast);
			Scanner scanObj = new Scanner(System.in);
			toUpdate = scanObj.nextLine();
			
			if (toUpdate.equals("")) {return; }	// if user hits <ENTER>, return to choose() 

			isMember = checkMembership("title", toUpdate, stmt);  // call checkMembership() to see if requested book (or author) is in Db.			
		}
		
		
		// STEP 2.  Ask user which field (qty, info, or website) they wish to update
		// Results are stored in column variable
		String fieldString = "badInput";	// initialize fieldString to an unacceptable value, to make while-loop begin.	
		// check: string longer than one character?  ASCII value of 1st character is out of range for the 3 allowable choice digits?
		while (fieldString.length() != 1  ||  fieldString.charAt(0) - 48 < 1  || fieldString.charAt(0) - 48 > 3) {
			fieldString = JOptionPane.showInputDialog("Do you want to change the QUANTITY, DESCRIPTION, or the WEBSITE BOOK REVIEW?"
					+ "\nNote: This change will be *PERMANENT* "
					+ "\nEnter 1, 2, or 3 \n\n1. QUANTITY \n2. DESCRIPTION \n3. WEBSITE BOOK REVIEW");
			
			if (fieldString == null) {return; } // click "cancel" --> JOptionPane sets fieldstring to null --> return, to choose().
		}
		int fieldNum = Integer.parseInt(fieldString);	// at this point, fieldNum is guaranteed to be either 1, 2, or 3
		
		// determine column variable
		String column = null;	// initialize column, which will be set below based on fieldNum
		if (fieldNum == 1) {
			column = "qty"; // "qty" is a pre-defined column of the books database
		}
		else if (fieldNum == 2) {
			column = "info"; // "info" is a pre-defined column of the books database
		}
		else if (fieldNum == 3) {
			column = "review"; // "review" is a pre-defined column of the books database
		}
		
		
		// STEP 3.  JDBC sends an SQL query, user prompted if corrections are necessary, user is notified of results
		ResultSet rset = stmt.executeQuery("SELECT " + column + " FROM books WHERE title = '" + toUpdate + "' ");
		rset.next();
			
		// handle the case where user wants to update QUANTITY
		if (fieldNum == 1) {
			int oldFieldInt = rset.getInt(column);
			rollPrint("Current quantity: " + oldFieldInt + "\n", fast); // print existing field to user
				
			// get new value from user for quantity
			int newFieldInt = -7; 	// initialize quantity to invalid number, so while-loop has at least one iteration.
			String newFieldString = "";
			while (newFieldInt < 0) {
				rollPrint("Enter the new quantity in stock for this book: ", fast);
				Scanner scanObj = new Scanner(System.in);	// Scanner class only reads as String
				newFieldString = scanObj.nextLine();
				newFieldInt = ensureDigit(newFieldString);	// call ensureDigit() to ensure string is all digit
			}
			// issue SQL update:    .replace used in case user input includes single quote '
			stmt.executeUpdate("UPDATE books SET " + column + " = '" + newFieldInt + "' WHERE title = '" + toUpdate.replace("'", "''") + "' ");
		}
			
		// handle the case where user wants to update DESCRIPTION or WEBSITE
		else if (fieldNum == 2 || fieldNum == 3) {
			String oldFieldStr = rset.getString(column);
			rollPrint("Current field: " + oldFieldStr + "\n", fast);	// print exiting field to user
				
			// get new value from user for description or website
			rollPrint("Enter the new text for this field: ", fast);	// address 255 (?) char. limit at a later date
			Scanner scanObjText = new Scanner(System.in);
			String newFieldStr = scanObjText.nextLine();
			// issue SQL update.  use .replace  in case user entry contains single quote '
			stmt.executeUpdate("UPDATE books SET " + column + " = '" + newFieldStr.replace("'", "''") + "' WHERE title = '" + toUpdate.replace("'", "''") + "' ");
		}
			
		rollPrint(" --Thank you, the record has been updated.", fast);	// final confirmation message to user
		System.out.println();	
	}
	
	
	// insert() is user choice 4.  update() and insert() are sister methods.  They make ADDITIONS to the database
	// Each has two(2) errors/checks/outlets: (1) calls ensureDigit(),   (2) Return to caller if user types empty string ("")
	public static void insert(Statement stmt) throws SQLException {
		System.out.println("\t\t-- INSERT BOOK INTO DATABASE -- ");

		// For reasons I don't yet understand, when I use a single scanner object to scan an int then one or more Strings, 
		// the FIRST String (and only the first) does not get scanned.  It gets skipped over, as if it took one failed String scan
		// to reset the scanner object, and then the scanner object IS ready for the next String scan.
		// 		SO:  I am using 2 different scanner objects as a work-around (one for ints, one for Strings)
		//		WAIT, NEVER MIND: i've decided ONLY to scan for strings, and convert the necessary strings to int afterward !

		Scanner strScan = new Scanner(System.in);
		
		rollPrint("You will be prompted to enter TITLE, AUTHOR, etc.  If you type <ENTER> in a non-number "
				+ "\ncolumn such as TITLE, the database will record an empty space in that column. \n"
				, fast);
		waitForIt(1000);
		System.out.print("TITLE: "); 
		String title = strScan.nextLine();
		System.out.print("AUTHOR (use format Lastname, Firstname): ");
		String author = strScan.nextLine();
		
		int pubDate = 3001;	// initialize year to an invalid number, to let while-loop begin 
		while (pubDate < -3000  ||  pubDate > 3000) {	// user is given a generous 6000-year range for publication date (!)
			System.out.print("YEAR OF PUBLICATION: "); 
			String pubString = strScan.nextLine();
			pubDate = ensureDigit(pubString);	// call ensureDigit(), which checks for all-digit, and prompts user for correction. 
		}
		
		int qty = -7; 	// initialize quantity to an invalid number, to let while-loop begin 
		while (qty < 0) {
			System.out.print("QUANTITY (How many books are in stock?): ");
			String qtyString = strScan.nextLine();
			qty = ensureDigit(qtyString);	// call ensureDigit(), which checks for all-digit, and prompts user for correction.
		}
		
		// how to cut off user at 255 characters?  need if stmt.  Solve at some future time
		System.out.print("DESCRIPTION (Write a brief summary of the book): ");
		String info = strScan.nextLine();
		System.out.print("WEBSITE BOOK REVIEW (Please enter the URL of a website containing a review of this book, "
				+ "\nor to leave this field blank press <ENTER>): ");
		String review = strScan.nextLine();
		
		// Could add a final message here, asking user to hit <ENTER> (or type "Y" or "yes") to confirm insert, otherwise printing
		// a message that insert is aborted, and returning to caller

		// get current highest book id, then add 1 to it to automatically generate an id for the book currently being inserted.
		ResultSet rset = stmt.executeQuery("SELECT id FROM books ORDER BY id DESC");	// SQL query statement
		rset.next();	// moves cursor from BEFORE the first row TO the first row, so now we're ready.
		int highID = rset.getInt("id");
		int newHigh = highID + 1;
						
		// INSERT a record with SQL query.    .replace used in case user input includes single quote '
		String hamString = "INSERT INTO books VALUES "
				+ "(" + newHigh + ", " + "'" + title.replace("'", "''") + "', '" + author.replace("'", "''") + "', " + 
				pubDate + ", " + qty + ", '" + info.replace("'", "''") + "', '" + review.replace("'", "''") + "')";
		int countInserted = stmt.executeUpdate(hamString); // don't really need "countInserted" var...
			
		waitForIt(1000);
		rollPrint(" --Your book has been added to the database", fast);//is it bad to say this without programmatic confirmation?
		System.out.println();
	}

	
	public static int choose() {
		System.out.println("  ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----\n");
		rollPrint("    [] [] Please select an option from the dialogue box [] [] \n", fast);

		String userString = "badInput";	// initialize userString to an unacceptable value, to let while-loop begin. 
		// check: string longer than one character?  ASCII value of 1st character is out of range for the 6 allowable choice digits?
		while (userString.length() != 1  ||  userString.charAt(0) - 48 < 0  || userString.charAt(0) - 48 > 5) {
			userString = JOptionPane.showInputDialog("What would you like to do? Please enter a number (1-5) below. \n Enter 0 to quit."
					+ "\n 1   Select a book, by title or author, to see its full database information"  
					+ "\n 2   View a list of ALL books in the database"	// returns titles, not the whole table
					+ "\n 3   Update the 'quantity', 'description' or 'reviews' column for an existing book"
					+ "\n 4   Insert a new book into the database"
					+ "\n 5   Delete a book from the database"
					+ "\n 0   Quit" );
			if (userString == null) {	// user clicks "cancel" --> return 0 to caller, causing program to exit
				return 0; 
			}	
		}
		int userNum = Integer.parseInt(userString);		
		
		// Should probably surround the following switch block with the try-catch, and just have that in this ONE place.
		// if an exception is thrown, the program returns to main which causes choose() to be called again.
		
		//parentheses after try:  The try-with-resources construct
		try (
				// Allocate a database 'Connection' object, to communicate with database called ebookstore
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", "myuser", "caryrd");
				// Allocate a 'Statement' object in the Connection
				Statement stmt = conn.createStatement();
		)	{
			switch (userNum) {
				case 1: search(stmt);	break;
				case 2: viewAll(stmt); 	break; 
				case 3: update(stmt);	break;
				case 4: insert(stmt);	break;
				case 5: delete(stmt);	break;
			
				case 0: break;	// user chooses "0" (above) --> return 0 to caller, causing program to exit
			}
		}  catch(SQLException ex) {
			ex.printStackTrace();
		}		// Close the resources - Done automatically by try-with-resources
		
		return userNum;
	}
	
	
	public static void main(String[] args) {
		// print welcome message, request <ENTER>
		rollPrint("\n\nYou are a clerk in a bookstore. Your book database only has seven books, but they are all fabulous."
				+ "\nThey are books about Hamilton! the musical *AND* Hamilton the man. You will be working mostly in this "
				+ "\nscreen (the console), but you will also be prompted to make choices in a dialog box.  Don't worry- Despite "
				+ "\noccasional scary warnings, any changes you make are temporary and can be reset by me, the programmer. "
				+ "\nPlease bear with the primitive User Interface! \tTo begin, press <ENTER>  ", 
				fast);
		Scanner scanObj = new Scanner(System.in); 
		scanObj.nextLine();	// accepts user's <ENTER>, doesn't record anything in a var
		
		int exitWithZero = 42;	// initialize to any non-zero value 
		while (exitWithZero != 0) {
			exitWithZero = choose();	// call choose(), the gateway to all other methods
		}
		
		// print goodbye message
		rollPrint("\n\nWe hope you had a smooth experience using the Hamilton! database today. "
				+ "\nMay Hamilton's life, and the musical about his life, inspire you."
				+ "\n -- Click-boom.  Program terminated.\n\n\n",
				fast);	
	}
}
