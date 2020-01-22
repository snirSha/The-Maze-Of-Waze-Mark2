package gameClient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
		int id1 = 204040687;  // "dummy existing ID  
		int level = 23;
		//allUsers();
		System.out.println("getCountEntersDB: "+getCountEntersDB(id1));
		System.out.println("getBestScore in level "+level+": "+getBestScore(id1,level));
		System.out.println("getTopLevel: "+getTopLevel(id1));
		//printLog();
		//String kml = getKML(id1,level);
		//System.out.println("** KML file example: ***");
		//System.out.println(kml);
	}
	///////////////////////snir's shit//////////////////////////////////////////////////////////////////////////
	public static int getCountEntersDB(int idT) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID="+idT+";";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int count=0;
			while(resultSet.next())
			{
				count++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
			return count;
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}


	public static int getBestScore(int idT ,int levelTmp) {
		if(levelTmp!=0 && levelTmp!=1 && levelTmp!=3 && levelTmp!=5 && levelTmp!=9 && levelTmp!=11 && levelTmp!=13 && levelTmp!=16 && levelTmp!=19 && levelTmp!=20 && levelTmp!=23)
			return -1;
		else {
			int mb=getMoveBoudary(levelTmp);
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = 
						DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
				Statement statement = connection.createStatement();
				String allCustomersQuery = "SELECT UserID,score,moves FROM Logs WHERE UserID="+idT+" AND levelID="+levelTmp+" AND moves<="+mb+";";
				ResultSet resultSet = statement.executeQuery(allCustomersQuery);
				int max=0;
				while(resultSet.next())
				{
					if(max<resultSet.getInt("score"))
						max=resultSet.getInt("score");
					//System.out.println(resultSet.getInt("UserID")+", "+resultSet.getInt("score")+", "+resultSet.getInt("moves"));
				}
				resultSet.close();
				statement.close();		
				connection.close();

				return max;
			}

			catch (SQLException sqle) {
				System.out.println("SQLException: " + sqle.getMessage());
				System.out.println("Vendor Error: " + sqle.getErrorCode());
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return -1;

	}

	private static int getMoveBoudary(int le) {
		switch(le){
		case 0:
			return 290;
		case 1:
			return 580;
		case 3:
			return 580;
		case 5:
			return 500;
		case 9:
			return 580;
		case 11:
			return 580;
		case 13:
			return 580;
		case 16:
			return 290;
		case 19:
			return 580;
		case 20:
			return 290;
		case 23:
			return 1140;
		default:
			return -1;
		}
	}


	public static int getTopLevel(int idT) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs WHERE UserID="+idT+" ORDER BY levelID DESC;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int maxLevel=0;
			while(resultSet.next())
			{
				maxLevel=resultSet.getInt("levelID");
				break;
			}
			resultSet.close();
			statement.close();		
			connection.close();
			return maxLevel;
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////omer's shit/////////////////////////////////////////////////////////////////////////////
	//	public static String getStats(int id) {
	//		StringBuilder sb = new StringBuilder();
	//
	//
	//		try {
	//			Class.forName("com.mysql.jdbc.Driver");
	//			Connection connection = 
	//					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
	//			Statement statement = connection.createStatement();
	//			int case0 = 0, case1 = 1, case2 = 3, case3 = 5, case4 = 9, case5 = 11, case6 = 13, case7 = 16, case8 = 19, case9 = 20, case10 = 23;
	//			String case0Query = "SELECT DISTINCT MAX(score) FROM Logs WHERE UserID<>"+id+" AND levelID="+case0+" AND score>=145 AND moves<=290;";
	//			String case1Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case1+" AND score>=450 AND moves<=580;";
	//			String case2Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case2+" AND score>=720 AND moves<=580;";
	//			String case3Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case3+" AND score>=570 AND moves<=500;";
	//			String case4Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case4+" AND score>=510 AND moves<=580;";
	//			String case5Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case5+" AND score>=1050 AND moves<=580;";
	//			String case6Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case6+" AND score>=310 AND moves<=580;";
	//			String case7Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case7+" AND score>=235 AND moves<=290;";
	//			String case8Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case8+" AND score>=250 AND moves<=580;";
	//			String case9Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case9+" AND score>=200 AND moves<=290;";
	//			String case10Query = "SELECT DISTINCT UserID FROM Logs WHERE UserID<>"+id+" AND levelID="+case10+" AND score>=1000 AND moves<=1140;";
	//			String cases[] = new String[11];
	//			cases[0] = case0Query;
	//			cases[1] = case1Query;
	//			cases[2] = case2Query;
	//			cases[3] = case3Query;
	//			cases[4] = case4Query;
	//			cases[5] = case5Query;
	//			cases[6] = case6Query;
	//			cases[7] = case7Query;
	//			cases[8] = case8Query;
	//			cases[9] = case9Query;
	//			cases[10] = case10Query;
	//
	//			ResultSet resultSet = statement.executeQuery(case0Query);
	//			int i = 0;
	//			while(resultSet.next()){
	//
	//				System.out.println("Score: " + resultSet.getInt("MAX(score)"));//+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getInt("score"));//+","+resultSet.getDate("time"));
	//
	//			}
	//
	//			resultSet.close();
	//			statement.close();
	//			connection.close();
	//
	//
	//		}
	//		catch (SQLException sqle) {
	//			System.out.println("SQLException: " + sqle.getMessage());
	//			System.out.println("Vendor Error: " + sqle.getErrorCode());
	//		}
	//		catch (ClassNotFoundException e) {
	//			e.printStackTrace();
	//		}
	//
	//
	//		return sb.toString();
	//	}
	////////////////////////END our shit/////////////////////////////////////////////////////////////////////////

	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next())
			{
				System.out.println("Id: " + resultSet.getInt("UserID")+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getDate("time"));
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}


}