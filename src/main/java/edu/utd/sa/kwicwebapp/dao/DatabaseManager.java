package edu.utd.sa.kwicwebapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import edu.utd.sa.kwicwebapp.service.InputBean;

//import edu.utd.sa.kwicwebapp.service.InputBean;

public class DatabaseManager {

	private DatabaseManager() {
		// TODO Auto-generated constructor stub
	}

	private Connection connection;
	private static DatabaseManager dbManager = null;

	public static synchronized DatabaseManager getInstance() {
		if (dbManager == null) {
			dbManager = new DatabaseManager();
			// Open connection to the database and population connection
			// variable

			try {
				// create a mysql database connection
				String myDriver = "com.mysql.jdbc.Driver";
				String myUrl = "jdbc:mysql://localhost/SA";
				Class.forName(myDriver);
				Connection con = DriverManager.getConnection(myUrl, "root", "kanchan");
				dbManager.connection = con;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return dbManager;
	}

	public void insert(List<InputBean> inputBeans) {
		System.out.println("Inserting " + inputBeans);
		try {
			Statement stmt = connection.createStatement();
			for (InputBean bean : inputBeans) {
				// use connection object, fire a insert statement and insert
				// into table

				String query = "Insert into CyberMiner (  shiftedIndex, originalString, url, priority ) "
						+ " values ('" + bean.getShiftedIndex() + "','"
						+ bean.getOriginalString() + "','" + bean.getUrl() + "'," + bean.getPriority() + ");";
				stmt.execute(query);

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void deleteAll() {

		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM CyberMiner;");
		} catch (Exception e) {
			System.err.println(e.getMessage());

		}

	}

	public void delete(String url) {
		// use connection object and execute following query
		// delete * from cyberminer where url="+url

		try {
			Statement stmt = connection.createStatement();
			String query = "DELETE FROM CyberMiner where url like '" + url + "';";
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public List<InputBean> select(String prefix) {
		List<InputBean> list = new LinkedList<InputBean>();

		try {
			Statement stmt = connection.createStatement();
			String query = "Select distinct shiftedIndex, originalString, url, priority from CyberMiner where " + " shiftedIndex like '" + prefix
					+ "%' order by priority desc ;";

			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {
				InputBean bean = new InputBean(result.getString("shiftedIndex"), result.getString("originalString"),
						result.getString("url"), result.getInt("priority"));

				list.add(bean);
			}
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return list;
	}

	/**
	 * This method is called for supporting queries like "NOT ENGINE" - This
	 * should return all results which do not contain "Engine" word
	 * 
	 * @param prefix
	 * @return
	 */
	public List<InputBean> selectNOT(String prefix) {
		List<InputBean> list = new LinkedList<InputBean>();

		try {
			Statement stmt = connection.createStatement();
			String query = "Select distinct  shiftedIndex, originalString, url, priority  from CyberMiner where " + " shiftedIndex Not like '%" + prefix
					+ "%' order by priority desc ;";

			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {
				InputBean bean = new InputBean(result.getString("shiftedIndex"), result.getString("originalString"),
						result.getString("url"), result.getInt("priority"));

				list.add(bean);
			}
			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
System.out.println("resturning"+list);
		return list;
	}

}