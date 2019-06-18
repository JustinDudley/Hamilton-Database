
// use boolean "aaronBool" ?
// Add an "Eliza" (or "Burr") object
// Could I make stmt and conn static variables at top, available to all?
// columns:  id, title, author, date, qty, description, link to reviews  (In future: price,  picture/image)

// Hmm, in the python iris program, it looks like there is an array set up, each member of which is itself
// an array of 4 members.  And there is some sort of category to it.  Each of the four members of the subarray
// is an attribute of the flower, and labelled as such somehow.  So... should I create some sort of Hamilton object
// that has my 7 members, organized by attribute?

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;		

import java.util.Scanner;
import javax.swing.JOptionPane;

public class HamiltonDb {
	// set number of milliseconds for time delay used by method rollPrint()
	static int fast = 2;
	static int slow = 10;
	
	/*
	// Column names, pre-existing in Database
	// should I make these "final"?
	// These are helpful when dealing with all the single vs. double quotation marks)
	static String idColumn = "id";
	static String titleColumn = "title";
	static String authorColumn = "author";
	static String pubDateColumn = "pubDate";
	static String qtyColumn = "qty";
	static String infoColumn = "info"; 
	static String reviewColumn = "review";

*/
	
	// Instance variables:  7 variables for the 7 columns in SQL Db table
	private int id;
	private String title;
	private String author;
	private int pubDate;
	private int qty;
	private String info;
	private String review;
	
	// Instance variables:  To store the user's choice of column and entry they want to access  
	private String userColumn;	
	private String userEntry;	
	
	// CONSTRUCTOR
	public HamiltonDb() {		// no parameters... yet
		
		//in future, may use these...
		this.id = id;
		this.title = title;
		this.author = author;
		this.pubDate = pubDate;
		this.qty = qty;
		this.info = info;
		this.review = review;
	}
	
	
	//  SIXTEEN(16+) GETTER/SETTERS
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
	
	public void setPubDate(int pubDate) {
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
		this.info = info;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
	
	public String getUserColumn() {
		return userColumn;
	}
	
	public void setUserColumn(String col) {
		this.userColumn = col;
	}
	
	public String getUserEntry() {
		return userEntry;
	}
	
	public void setUserEntry(String entry) {
		this.userEntry = entry;
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

	
	// called by ensurePosDigit()   Takes String as arg, returns boolean:  Does string have ONLY digits?
	public static boolean checkDigit(String perp) {
		boolean isAllDigit = true;
		if (perp.equals("")) {	// handles case where perp == ""
			isAllDigit = false;
			return isAllDigit;
		}
		// to handle cases where perp == "eggplant" or "123f",  AND to handle negative values:
		for (int i = 0; i < perp.length(); i++) {	
			if (perp.charAt(i) - 48 < 0  || perp.charAt(i) - 48 > 9) {
				isAllDigit = false;
				break;
			}
		}
		return isAllDigit;
	}
	
	// takes user string, calls checkDigit() to check if each char is a digit. If all digit, ensurePosDigit's 
	// loop is skipped, string is cast to int and returned. If NOT all digit, a new string is demanded until 
	// user gives one that IS all int, and then that string is converted to int type, and returned to caller.
	public static int ensurePosDigit(String userStr) {
		while (checkDigit(userStr) == false) {
			rollPrint("'" + userStr + "' is not a valid number.  Please try again: ", fast); 
			Scanner scanObj = new Scanner(System.in);
			userStr = scanObj.nextLine();
		}
		int userInt = Integer.parseInt(userStr);
		return userInt;
	}
	
	
	// user choice 2. The simplest of the 5 methods called by choose()
	// The method does not take input from user, just prints out list of book titles. 
	public void viewAll(Statement stmt) throws SQLException {
		System.out.println("\t\t-- VIEW ALL TITLES -- ");
		
		rollPrint("Here is a list of all books in the database: ", fast);
		ResultSet rset = stmt.executeQuery("SELECT title FROM books");	// SQL query statement
		while (rset.next()) {
			String title = rset.getString("title");
			rollPrint("\n\t" + title, fast);
		}
		System.out.println();
	}
	
	/*
	// SOON TO BE OBSOLETE.  STILL USED BY 2 METHODS, FOR NOW
	public static boolean checkMembership(String column, String userEntry, Statement stmt) throws SQLException {
		boolean inDb = false;
		ResultSet rset = stmt.executeQuery("SELECT " + column + " FROM books");	// SQL query statement
		while (rset.next()) {	// check each name (name of title, or of author) against userEntry
			String name = rset.getString(column);
			if (userEntry.equalsIgnoreCase(name)) {
				inDb = true;	// method will return true only if userEntry finds a match in database
			}
		}
		if (inDb == false) {
			rollPrint(" --Sorry, the database does not contain that item. Please try again--\n\n", fast);
		}
		return inDb;
	} */
	
	
	// Check Db for a match with user's input, then loads one record from database TO a HamiltonDb Object.
	public boolean checkAndRetrieve(Statement stmt) throws SQLException {
		boolean inDb = false;
		ResultSet rsetCheckCol = stmt.executeQuery("SELECT " + getUserColumn() + " FROM books"); 	// SQL query
		
		while (rsetCheckCol.next()) {	// check each item (name of a title or author) against user's proposed name
			String item = rsetCheckCol.getString(getUserColumn());
			if (getUserEntry().equalsIgnoreCase(item)) {
				inDb = true;	// method will return true only if userEntry finds a match in database
				
				// load one record from SQL into a HamiltonDb object.  This object goes unused for some methods, like delete()
				ResultSet rsetFetchRecord = stmt.executeQuery("SELECT * FROM books WHERE " + getUserColumn() 
						+ " = '" + getUserEntry().replace("'",  "''") + "' "  );
				rsetFetchRecord.next();
				// use setter methods to set each instance variable to its corresponding value in ResultSet object
				setId(rsetFetchRecord.getInt("id"));	//automatically points to the next one apparently?
				setTitle(rsetFetchRecord.getString("title"));
				setAuthor(rsetFetchRecord.getString("author"));
				setPubDate(rsetFetchRecord.getInt("pubDate"));
				setQty(rsetFetchRecord.getInt("qty"));
				setInfo(rsetFetchRecord.getString("info"));
				setReview(rsetFetchRecord.getString("review"));
				
				break;
			}
		}
		if (inDb == false) {
			rollPrint(" --Sorry, the database does not contain that item. Please try again--\n\n", fast);
		}
		
		return inDb;
	}
	
	
	public String toString() {
		return "\tID#: " + getId()
		+ "\n\tTitle: " + getTitle()
		+ "\n\tAuthor: " + getAuthor()
		+ "\n\tPublication Date: " + getPubDate()
		+ "\n\tQuantity in Stock: " + getQty()
		+ "\n\tDescription: " + getInfo()
		+ "\n\tWebsite Book Review: " + getReview() ;
	}
	
	
	// search() is user choice 1.  search() and delete() are sister methods.
	public void search(Statement stmt) throws SQLException {
		System.out.println("\t\t-- SELECT BOOK -- ");
		waitForIt(750);
		
		// STEP 1.  Ask user which column (author or title) they wish to search
		String userColumn = "badInput";	// initialize userColumn to an unacceptable value, to let while-loop begin.
		// check: string longer than one character?  ASCII value of 1st character is out of range for the 2 allowable choice digits?
		while (userColumn.length() != 1  ||  userColumn.charAt(0) - 48 < 1  || userColumn.charAt(0) - 48 > 2) {
			userColumn = JOptionPane.showInputDialog("Do you want to search by "
					+ "author or title? \nEnter 1 or 2 \n\n1. author \n2. title ");
			if (userColumn == null) {	 // click "cancel" --> JOptionPane sets userColumn to null --> return, to choose().
				return;
			}
		}
		
		// STEP 2. Get user search choice, then call checkAndRetrieve() to verify book in Db  
		boolean inDb = false;
		while (inDb == false) {		// keep looping as long as user keeps entering books that AREN'T in Db.	
			if (userColumn.equals("1")) {
				setUserColumn("author");
				rollPrint("Type the name of the author using format Lastname, Firstname (or to go back press <ENTER>: ): ", fast);
			}
			else if (userColumn.equals("2")) {
				setUserColumn("title");
				rollPrint("Type the title (to go back press <ENTER> ): ", fast);
			}

			Scanner scanObj = new Scanner(System.in);
			setUserEntry(scanObj.nextLine());
			if (getUserEntry().equals("")) {		// if user hits <ENTER>, return to choose()
				return;
			}	
			
			inDb = checkAndRetrieve(stmt);	// call method to see if requested book is in Db
		}

		// STEP 3. Print message to user
		String record = toString();
		rollPrint(" --The requested record is: \n" + record, fast);
		System.out.println();			
	}
	
	
	// delete() is user choice 5.  delete() and search() are sister methods.
	public void delete(Statement stmt) throws SQLException {
		System.out.println("\t\t-- DELETE BOOK -- ");
	
		// STEP 1.  Get user choice of title to delete, call checkAndRetrieve() to verify book in Db, loop till get good choice.
		setUserColumn("title");	// this method only handles the "title" column of Db.  There is no user choice.
		boolean inDb = false;
		while (inDb == false) {	// keep looping as long as user keeps entering books that AREN'T in the Db 
			rollPrint("To delete a book permanently, type the book's title (to go back press <ENTER>): ", fast);
			Scanner scanObj = new Scanner(System.in);
			setUserEntry(scanObj.nextLine());
			// user's input is of 4 types: 1. correct name, 2. incorrect name, 3. <ENTER>, 4. contains illegal symbol 
			
			if (getUserEntry().equals("")) {	// if user hits <ENTER>, return to caller method choose() 
				return; 	
			}	
			
			inDb = checkAndRetrieve(stmt);	// call method to see if requested book is in Db
		}
		
		// STEP 2.  JDBC sends an SQL query, user is notified of results
		// SQL query.   .replace used in case user input includes single quote '
		stmt.executeUpdate("DELETE FROM books WHERE title = '" + getUserEntry().replace("'", "''") + "' " );
		waitForIt(1000);
		rollPrint(" --The requested record has been deleted.", fast);
		System.out.println();
		waitForIt(1000);
	}

	
	public void overwriteRecord(Statement stmt) throws SQLException {
		// issue SQL update:    .replace used in case user input includes single quote '
		stmt.executeUpdate("UPDATE books SET title = '" + getTitle() + "', "
				+ "author = '" + getAuthor().replace("'", "''") + "', "
				+ "pubDate = '" + getPubDate() + "', "
				+ "qty = '" + getQty() + "', "
				+ "info = '" + getInfo().replace("'", "''") + "', "
				+ "review = '" + getReview().replace("'", "''") + "' "
				+ "WHERE id = " + getId());		
	}
	
	
	
	// update() is user choice 3.  update() and addNew() are sister methods. They make ADDITIONS to the Db.
	public void update(Statement stmt) throws SQLException {
		System.out.println("\t\t-- UPDATE -- ");

		// STEP 1.  Get user choice of title to update, call checkAndRetrieve() to verify book in Db, loop till get good choice.
		setUserColumn("title");	// this method only handles the "title" column of Db.  There is no user choice.
		boolean inDb = false;
		while (inDb == false) {	// keep looping as long as user keeps entering books that AREN'T in the Db.
			rollPrint("Type in the title of the book whose field you wish to update (to go back press <ENTER>): ", fast);
			Scanner scanObj = new Scanner(System.in);
			setUserEntry(scanObj.nextLine());
			// user's input is of 4 types: 1. correct name, 2. incorrect name, 3. <ENTER>, 4. contains illegal symbol 
			
			if (getUserEntry().equals("")) {	// if user hits <ENTER>, return to caller method choose() 
				return; 	
			}	
			
			inDb = checkAndRetrieve(stmt); // call method, see if req'd book is in Db; retrieve record, load into HamiltonDb object.
		}

		
		// STEP 2.  Ask user which column (qty, info, or website) they wish to update
		String userColumn = "badInput";	// initialize fieldString to an unacceptable value, to make while-loop begin.	
		// check: string longer than one character?  ASCII value of 1st character is out of range for the 3 allowable choice digits?
		while (userColumn.length() != 1  ||  userColumn.charAt(0) - 48 < 1  || userColumn.charAt(0) - 48 > 3) {
			userColumn = JOptionPane.showInputDialog("Do you want to change the QUANTITY, DESCRIPTION, or the WEBSITE BOOK REVIEW?"
					+ "\nNote: This change will be *PERMANENT* "
					+ "\nEnter 1, 2, or 3 \n\n1. QUANTITY \n2. DESCRIPTION \n3. WEBSITE BOOK REVIEW");
			if (userColumn == null) {	// click "cancel" --> JOptionPane sets userColumn to null --> return, to choose().
				return; 
			} 
		}
		
		
		// STEP 3: load user entry into the HamiltonDb object alex
		if (userColumn.equals("1")) {
			rollPrint("Current quantity: " + getQty() + "\n", fast); // print existing field to user
			rollPrint("Enter the new quantity in stock for this book: ", fast);
			Scanner scanObj = new Scanner(System.in);	// Scanner class only reads as String
			String newEntryString = scanObj.nextLine();
			int validInt = ensurePosDigit(newEntryString);
			setQty(validInt);	// alex loaded with new user qty			
		}
		else if (userColumn.equals("2")) {
			rollPrint("Current entry: " + getInfo() + "\n", fast);	// print existing entry to user
			// get new value from user for description column
			rollPrint("Type in the new entry: ", fast);	// address 255 (?) char. limit at a later date
			Scanner scanObjString = new Scanner(System.in);
			String newEntryStr = scanObjString.nextLine();
			setInfo(newEntryStr);
		}
		else if (userColumn.equals("3")) {
			rollPrint("Current entry: " + getReview() + "\n", fast);	// print existing entry to user
			// get new value from user for review column
			rollPrint("Type in the new entry: ", fast);	// address 255 (?) char. limit at a later date
			Scanner scanObjString = new Scanner(System.in);
			String newEntryStr = scanObjString.nextLine();
			setReview(newEntryStr);
		}
		
		
		// STEP 4: overwrite SQL Db record with the fields of HamiltonDb Object alex
		overwriteRecord(stmt);	// using id# as reference, overwrites one single record of database with entire contents of alex object
		rollPrint(" --Thank you, the record has been updated.", fast);	// final confirmation message to user
		System.out.println();	
	}
		

	public void insertRecord(Statement stmt) throws SQLException {
		//  .replace used in case user input includes single quote '
		String hamString = "INSERT INTO books VALUES (" + getId() + ", "
				+ "'" + getTitle().replace("'", "''") + "', "
				+ "'" + getAuthor().replace("'", "''") + "', " 
				+ getPubDate() + ", "
				+ getQty() + ", "
				+ "'" + getInfo().replace("'", "''") + "', "
				+ "'" + getReview().replace("'", "''") + "'" + ")";
		int countInserted = stmt.executeUpdate(hamString); // don't really need "countInserted" var...
	}
	
	
	//addNew() is user choice 4.  update() and addNew() are sister methods.  They make ADDITIONS to the Db.
	public void addNew(Statement stmt) throws SQLException {
		System.out.println("\t\t-- ADD NEW BOOK TO DATABASE -- ");
		Scanner strScan = new Scanner(System.in);
		
		//STEP 1: generate unique ID # for new book
		ResultSet rset = stmt.executeQuery("SELECT id FROM books ORDER BY id DESC");	// SQL query statement
		rset.next();	// moves cursor from BEFORE the first row TO the first row, so now we're ready.
		int highId = rset.getInt("id");	// the current highest ID# in Db
		setId(highId + 1);	// new ID# generated for next entry, and loaded into HamiltonDb object
		
		//STEP 2:  get all fields from user
		rollPrint("You will be prompted to enter TITLE, AUTHOR, etc.  If you type <ENTER> in a non-number "
				+ "\ncolumn such as TITLE, the database will record an empty space in that column. \n"
				, fast);
		waitForIt(1000);
		
		System.out.print("TITLE: "); 
		setTitle(strScan.nextLine());
		System.out.print("AUTHOR (use format Lastname, Firstname): ");
		setAuthor(strScan.nextLine());
		int validPubDate = 3001;	// initialize year to an invalid number, to let while-loop begin 
		while (validPubDate < 0  ||  validPubDate > 3000) {	// user is given a generous 3000-year range for publication date (!)
			System.out.print("YEAR OF PUBLICATION: "); 
			String pubString = strScan.nextLine();
			validPubDate = ensurePosDigit(pubString);	// call ensurePosDigit(), which checks for all-digit, and prompts user for correction.
			setPubDate(validPubDate);
		}
		System.out.print("QUANTITY (How many books are in stock?): ");
		String qtyString = strScan.nextLine();
		int validQty = ensurePosDigit(qtyString);	// call ensurePosDigit(), which checks for all-digit, and prompts user for correction.
		setQty(validQty);
		System.out.print("DESCRIPTION (Write a brief summary of the book): "); // how to cut off user at 255 characters?  need if stmt.  Solve at some future time
		setInfo(strScan.nextLine());
		System.out.print("WEBSITE BOOK REVIEW (Please enter the URL of a website containing a review of this book, "
				+ "\nor to leave this field blank press <ENTER>): ");
		setReview(strScan.nextLine());
		// Could add a final message here, asking user to hit <ENTER> (or type "Y" or "yes") to confirm 
		// insert, otherwise printing a message that insert is aborted, and returning to caller
		
		
		//STEP 3.  Insert record, SQL Query   
		insertRecord(stmt);
		waitForIt(1000);
		rollPrint(" --Your book has been added to the database", fast); //is it bad to say this without programmatic confirmation?
		System.out.println();
	}

	
		
		/*
	// addNew() is user choice 4.  update() and addNew() are sister methods.  They make ADDITIONS to the database
	// Each has two(2) errors/checks/outlets: (1) calls ensurePosDigit(),   (2) Return to caller if user types empty string ("")
	public void addNewOBSOLETE(Statement stmt) throws SQLException {
		System.out.println("\t\t-- ADD NEW BOOK TO DATABASE -- ");

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
		while (pubDate < 0  ||  pubDate > 3000) {	// user is given a generous 3000-year range for publication date (!)
			System.out.print("YEAR OF PUBLICATION: "); 
			String pubString = strScan.nextLine();
			pubDate = ensurePosDigit(pubString);	// call ensurePosDigit(), which checks for all-digit, and prompts user for correction. 
		}
		
		int qty = -7; 	// initialize quantity to an invalid number, to let while-loop begin 
		while (qty < 0) {
			System.out.print("QUANTITY (How many books are in stock?): ");
			String qtyString = strScan.nextLine();
			qty = ensurePosDigit(qtyString);	// call ensurePosDigit(), which checks for all-digit, and prompts user for correction.
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
	} */

	
	public int choose() {
		System.out.println("  ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- -----\n");
		rollPrint("    [] [] Please select an option from the dialogue box [] [] \n", fast);

		String userString = "badInput";	// initialize userString to an invalid value, so while-loop begins. 
		// check if string is longer than one character, and if  ASCII value of 1st character is out of 
		// range for the 6 allowable choice digits.
		// in future:  Just check if userString belongs to a given array.  Maybe call a static function that
		// checks a local var or static var.  
		//  - wish I could include "null" in the list of possibilities and do away with the awkwardness
		while (userString.length() != 1  ||  userString.charAt(0) - 48 < 0  || userString.charAt(0) - 48 > 5) {
			userString = JOptionPane.showInputDialog("What would you like to do? Please enter a number (1-5) below. \n Enter 0 to quit."
					+ "\n 1   Search for a book, by title or author, and see its full database information"  
					+ "\n 2   View a list of ALL books in the database"	// returns titles, not the whole table
					+ "\n 3   Update the 'quantity', 'description' or 'reviews' column for an existing book"
					+ "\n 4   Add a new book to the database"
					+ "\n 5   Delete a book from the database"
					+ "\n 0   Quit" );
			if (userString == null) {	// user clicks "cancel" --> return 0 to caller --> program exits.
				return 0; 
			}	
		}
		int userNum = Integer.parseInt(userString);		
		
		
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
				case 4: addNew(stmt);	break;
				case 5: delete(stmt);	break;
			
				case 0: break;	// user chooses "0" (above) --> return 0 to caller --> program exits 
			}
		}  catch(SQLException ex) {
			ex.printStackTrace();
		}		// Close the resources - Done automatically by try-with-resources
		
		return userNum;
	}
	
	
	public static void main(String[] args) {	
		
		HamiltonDb alex = new HamiltonDb(); 
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
			exitWithZero = alex.choose();	// call choose(), the gateway to all other methods
		}		
		
		// print goodbye message
		rollPrint("\n\nWe hope you had a smooth experience using the Hamilton! database today. "
				+ "\nMay Hamilton's life, and the musical about his life, inspire you."
				+ "\n -- Click-boom.  Program terminated.\n\n\n",
				fast);
	}
}
