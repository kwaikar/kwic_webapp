package edu.utd.sa.kwicwebapp.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.utd.sa.kwicwebapp.service.InputBean;

public class DatabaseManager {

	private DatabaseManager() {
		// TODO Auto-generated constructor stub
	}

	private Connection connection;
	private static DatabaseManager dbManager = null;

	List<InputBean> list = new ArrayList<InputBean>();
	public static synchronized DatabaseManager getInstance() {
		if (dbManager == null) {
			dbManager = new DatabaseManager();
			// Open connection to the database and population connection
			// variable
		}
		return dbManager;
	}

	public void insert(List<InputBean> inputBeans) {
		System.out.println("Inserting "+inputBeans);
		list.addAll(inputBeans)
;		for (InputBean bean : inputBeans) {
			// use connection object, fire a insert statement and insert into
			// table.
		}
	}

	public void deleteAll() {
		// use connection object and execute
		// delete * from CyberMiner
		// table.
		list=new ArrayList<InputBean>();

	}

	public void delete(String url) {
		// use connection object and execute following query
		// delete * from cyberminer where url="+url
	}

	public List<InputBean> select(String prefix) {
		//List<InputBean> list = new LinkedList<InputBean>();
		//return list;
		// use connction object, execute following query
		// Select * from cyberminer where
		// shiftedIndex="+prefix+"*" order by priority desc or see if there is a
		// startsWith query in mysql
		// for each result from resultset, create instance of inputBean, store
		// it in list, and return the list.
		return list;
	}
	
	/** 
	 * This method is called for supporting queries like "NOT ENGINE" - This should return all results which do not contain "Engine" word
	 * @param prefix
	 * @return
	 */
	public List<InputBean> selectNOT(String prefix) {
	//	List<InputBean> list = new LinkedList<InputBean>();
		// use connction object, execute following query
		// Select * from cyberminer where
		// shiftedIndex! contains("+prefix+")" order by priority desc or see if there is a
		// startsWith query in mysql
		// for each result from resultset, create instance of inputBean, store
		// it in list, and return the list.
		return list;
	}

}
