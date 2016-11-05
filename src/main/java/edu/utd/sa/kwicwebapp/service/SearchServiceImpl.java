package edu.utd.sa.kwicwebapp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.utd.sa.kwicwebapp.dao.DatabaseManager;

@Service
public class SearchServiceImpl {

	public void populateIndex(List<String> shifts, String originalString, String url, int priority, boolean delete) {
		List<InputBean> inputBeans = new ArrayList<InputBean>();
		for (String shift : shifts) {
			if (delete) {
				delete(url);
			} else {
				InputBean bean = new InputBean(shift, originalString, url, priority);
				inputBeans.add(bean);
			}
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		dbm.insert(inputBeans);
	}

	public void deleteAll() {
		DatabaseManager dbm = DatabaseManager.getInstance();
		dbm.deleteAll();

	}

	public void delete(String url) {

		DatabaseManager dbm = DatabaseManager.getInstance();
		dbm.delete(url);
	}

	public Set<InputBean> select(String prefix) {
		List<InputBean> list = new LinkedList<InputBean>();
		DatabaseManager dbm = DatabaseManager.getInstance();
		String[] splits = null;
		if (prefix.contains(" AND ")) {
			splits = prefix.split(" AND ");
			Set<InputBean> set1 = new HashSet(dbm.select(splits[0]));
			Set<InputBean> set2 = new HashSet(dbm.select(splits[1]));
			set1.retainAll(set2);
			return set1;
		} else if (prefix.contains(" OR ")) {
			splits = prefix.split(" OR ");
			Set<InputBean> set1 = new HashSet(dbm.select(splits[0]));
			Set<InputBean> set2 = new HashSet(dbm.select(splits[1]));
			set1.addAll(set2);
			return set1;
		}else if (prefix.startsWith("NOT ")) {
		return new HashSet(	dbm.selectNOT( prefix.replaceAll("NOT ","")));
			 
		}
		return new HashSet(dbm.select(prefix));
	}

}
