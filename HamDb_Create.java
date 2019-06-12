// Hmmm, I don't know how to create a new database programmatically yet
// More specifically, I don't know how to use a Connection object to connect to anything except an existing database.
// So, I will create the database ebookstore through the command line, THEN do the rest programmatically.
// Okay, so I ALSO don't know how to create a table programmatically.  But at LEAST I think I may
// be able to "truncate", i.e. delete all data from, a table.
// So, I will create a table with 7 columns in the command line, THEN fill it with values programmatically!

// Database ebookstore contains a table called books.
// Values for books:
//		id int,
//		title varchar(255)
// 		author varchar(255)		// format:  Lastname, Firstname
//		pubDate int,
//		qty int, 				// qty in stock
//		info varchar(255), 		// brief description of book
//		review varchar(255) 	// link to a review of book

// Hmmm, the value entries are ridiculously long.  Would it be better to have a back-up/source database, with all 12 books,
// and have this "Creation" program read FROM THAT to truncate then re-populate the books database when necessary?

import java.sql.*;	// need 'Connection', 'Statement' and 'ResultSet' classes

public class HamDb_Create {
	
	public static void main(String[] args) {
		
		//parentheses after try:  The try-with-resources construct
		try (
				// Allocate a database 'Connection' object, to communicate with database called ebookstore
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", "myuser", "caryrd");
				// Allocate a 'Statement' object in the Connection
				Statement stmt = conn.createStatement();
		)	{
			
			// Need to issue warning:  Continuing will erase the former table "books"
			// JD  Should create a back-up table with slightly diff. name
			int countRows = stmt.executeUpdate("truncate table books");		// I *think*  this statement is working properly!!
			System.out.println(countRows + " records changed by truncating.\n");
			
			// single quote seems to be the ONLY danger, re SQL statements.  See 6th record below
			String sqlInsert = "insert into books values "
					+ "(1001, 'Alexander Hamilton', 'Chernow, Ron', 2005, 33, 'the Bestseller that inspired Lin-Manuel Miranda to write the hip-hop-infused musical', 'https://www.goodreads.com/book/show/16130.Alexander_Hamilton'), "
					+ "(1002, 'The Hamilton Papers: Historic Documents Referenced in the Broadway Musical (Volume 1)', 'Hamilton, Alexander', 2016, 12, 'a complete resource for the historic documents from the hit Broadway musical, 430 pages.', 'https://www.amazon.com/Hamilton-Papers-Historic-Documents-Referenced/dp/1982032820'), "
					+ "(1003, 'Hamilton the Revolution', 'Miranda, Lin-Manuel', 2016, 8, 'Inside look at the making of Hamilton!, exclusive interviews and photos, written by Miranda with Jeremy McCarter', 'https://www.barnesandnoble.com/w/hamilton-lin-manuel-miranda/1123185769#EditorialReviews' ), "
					+ "(2001, 'Report on Manufacturing', 'Hamilton, Alexander', 1791, 30, 'the third major report, and magnum opus, of the first U.S. Treasury Secretary Alexander Hamilton', 'https://en.wikipedia.org/wiki/Report_on_Manufactures'), "
					+ "(2002, 'The Essential Hamilton', 'Hamilton, Alexander', 2017, 5, 'a portrait of Hamilton in his own words, charting his meteoric rise, his controversial tenure as treasury secretary, and his scandalous final years', 'https://www.amazon.com/Essential-Hamilton-Letters-Writings-Publication/dp/1598535366' ), "
					+ "(2003, 'Who Tells Your Story', 'Frankel, Valerie', 2016, 10, 'the musical''s backstory with many deeper insights', 'https://www.amazon.com/Who-Tells-Your-Story-Phenomenon-ebook/dp/B01NAJ8NUL'), "
					+ "(2004, 'The Hamilton (Musical) Handbook', 'Huber, Madison', 2016, 17, 'countless Hamilton facts right at your fingertips', 'https://www.barnesandnoble.com/w/the-hamilton-madison-huber/1123660242' )";
			System.out.println("The SQL query is: " + sqlInsert);
			//int countInserted = stmt.executeUpdate(sqlInsert.replace("'", "''"));	// .replace, in case of single quote ' problem
			int countInserted = stmt.executeUpdate(sqlInsert);	

			System.out.println(countInserted + " records inserted.\n");
			
			
			// Issue a SELECT to check the changes
			String strSelect = "select * from books";
			System.out.println("The SQL query is: " + strSelect);
			ResultSet rset = stmt.executeQuery(strSelect);
			
			// Move the cursor to the next row
			while(rset.next()) {
				System.out.println(rset.getInt("id") + ", "
						+ rset.getString("title") + ", "
						+ rset.getString("author") + ", "
						+ rset.getInt("pubDate") + ", "
						+ rset.getInt("qty") + ", "
						+ rset.getString("info") + ", "
						+ rset.getString("review") );
			}
			
		}  catch(SQLException ex) {
			ex.printStackTrace();
		}
		// Close the resources - Done automatically by try-with-resources
	}
}
	