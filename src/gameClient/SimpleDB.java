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
		//System.out.println("getCountEntersDB: "+getCountEntersDB(id1));
		//System.out.println("getBestScore in level "+level+": "+getBestScore(id1,level));
		//System.out.println("getTopLevel: "+getTopLevel(id1));
		int[][] rali=getRankInAllLevels(id1);
		for (int i = 0; i < rali.length; i++) {
			for (int j = 0; j < rali[i].length; j++) {
				System.out.print(rali[i][j]+",");
			}
			System.out.println();
		}
		
		
		
		//printLog();
		//String kml = getKML(id1,level);
		//System.out.println("** KML file example: ***");
		//System.out.println(kml);
	}
	
	/*
	 * check if the given id is in the list of users
	 * @param id
	 * @return
	 */
	public static boolean isContainsID(int id) {
		boolean bool = false;
		
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				bool = true;
			}
		}
		catch (Exception sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + ((SQLException) sqle).getErrorCode());
		}
		return bool;
	}
	
	
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


	public static int[][] getRankInAllLevels(int idT) {
		int [][] ranks=new int[2][11];
		int i=0;
		for(int lev=0; lev<24 ; lev++) {
			if(lev!=0 && lev!=1 && lev!=3 && lev!=5 && lev!=9 && lev!=11 && lev!=13 && lev!=16 && lev!=19 && lev!=20 && lev!=23)
				continue;
			else {
					ranks[0][i]=lev;
					ranks[1][i]=getRank(lev);
					i++;
			}
		}
		return ranks;
	}
	
	
	public static int getRank(int lev) {
		int myBestScore=getBestScore(204040687,lev);
		int moveBound=getMoveBoudary(lev);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT UserID,MAX(score) FROM Logs WHERE UserID<>0 AND UserID<>999 AND UserID<>204040687 AND moves<="+moveBound+" AND levelID="+lev+" Group by UserID;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			int countBetterThanMe=0;
			int otherPlayerScore;
			while(resultSet.next())
			{
				otherPlayerScore=resultSet.getInt("MAX(score)");
				if(otherPlayerScore>myBestScore) {
					countBetterThanMe++;
				}
			}
			resultSet.close();
			statement.close();		
			connection.close();
			return (countBetterThanMe+1);
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