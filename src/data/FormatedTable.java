package data;

import java.io.*;
import java.util.*;

import tools.Probe;

/**
 * This class is designed to create any container of a formated table
 * Notice: the class member index is a special kind of column. 
 * @author hyao
 * @version 0.1.1
 * @date 2014.12
 */
public class FormatedTable{
	private String table_name;
	private HashMap<String, ArrayList<String>> table_info;
	private HashMap<String, ArrayList<String[]>> table_data_source;
	private HashMap<String, ArrayList<String[]>> table_data;
	private static String[] tab_pool = {
		"table_select_column_nums",
		"table_select_line_markers"
		};
	
	public FormatedTable(){
		table_name = new String();
		table_info = new HashMap<String, ArrayList<String>>();
		table_data_source = new HashMap<String, ArrayList<String[]>>();
		table_data = new HashMap<String, ArrayList<String[]>>();
	}
	
	public void initialize(
			String table_name, 
			HashMap<String, ArrayList<String>> table_info, 
			HashMap<String, HashMap<String, ArrayList<String[]>>> table_data_source_pool
			){		
		setTable_name(table_name);
		setTable_info(table_info);
		//System.out.println("$$$ " + this.table_info);
		prepareTable_source(table_data_source_pool);
		prepareTable_data();
		setTable_data(table_data);
	}

	//Method[0]
	private void prepareTable_source(HashMap<String, HashMap<String, ArrayList<String[]>>> table_data_source_pool) {
		if(table_info.containsKey("table_data_source")){
			String table_data_source_name = table_info.get("table_data_source").get(0);
			//System.out.println("%%%%"+ table_data_source_name);
			//System.out.println("%%%%"+ table_data_source_pool.get(table_data_source_name));
			Iterator iter = table_data_source_pool.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next();
				String current_entry_name = entry.getKey().toString();
				if(current_entry_name.equalsIgnoreCase(table_data_source_name)){
					table_data_source = table_data_source_pool.get(current_entry_name);
				}
			}
		}
	}
	
	private void prepareTable_data() {
		Iterator iter = table_data_source.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_entry_name = entry.getKey().toString();
			ArrayList<String[]>current_cluster = table_data_source.get(current_entry_name);
			ArrayList<String[]>newCluster = new ArrayList<String[]>();
			for(int i=0; i<current_cluster.size(); i++){
				String[] current_line = current_cluster.get(i);
				if(table_info.containsKey("table_select_line_markers")){
					if(selectLine(current_line) == 1){//	isSelect
						//Deep Copy
						String[] newLine = new String[current_line.length];
						for(int j=0; j<current_line.length; j++){
							newLine[j] = current_line[j];
						}
						newCluster.add(newLine);
					}
				}
				else {
					String[] newLine = new String[current_line.length];
					for(int j=0; j<current_line.length; j++){
						newLine[j] = current_line[j];
					}
					newCluster.add(newLine);
				}
			}
			for(int i=0; i<newCluster.size(); i++){
				String[] current_line = newCluster.get(i);
				if(table_info.containsKey("table_discard_column_nums")){
					discardColumn(current_line);
				}
				if(table_info.containsKey("table_select_column_nums")){
					ArrayList<String> table_select_column_nums = table_info.get("table_select_column_nums");
					if(!table_select_column_nums.get(0).equalsIgnoreCase("NA")){
						selectColumn(current_line);
					}
				}
			}
		table_data.put(current_entry_name, newCluster);
		}
		
	} 
	
	//Method[1]
	private void discardColumn(String[] current_line) {
		ArrayList<String> table_discard_column_nums = table_info.get("table_discard_column_nums");
		String[] this_line = current_line;
		for(int i=0; i< table_discard_column_nums.size(); i++){
			int current_discard_column_num = Integer.valueOf(table_discard_column_nums.get(i)).intValue();
			this_line[current_discard_column_num] = "";
		}
		//Deep Copy
		for(int i=0; i<current_line.length; i++){
			current_line[i] = this_line[i];
		}
	}
	
	private void selectColumn(String[] current_line) {
		ArrayList<String> table_select_column_nums = table_info.get("table_select_column_nums");
		String[] this_line = new String[current_line.length];
		for(int i=0; i< table_select_column_nums.size(); i++){
			int current_select_column_num = Integer.valueOf(table_select_column_nums.get(i)).intValue();
			this_line[current_select_column_num] = current_line[current_select_column_num];
		}
		//Deep Copy
		for(int i=0; i<current_line.length; i++){
			if(this_line[i]==null){
				current_line[i] = "";
			}
			else{
				current_line[i] = this_line[i];
			}
		}
	}
	
	private int selectLine(String[] current_line) {
		int isSelect = 0;
		ArrayList<String> table_select_line_markers = table_info.get("table_select_line_markers");
		int marker_location = Integer.valueOf(table_select_line_markers.get(0)).intValue();
		for(int i=1; i<table_select_line_markers.size(); i++){
			String current_marker = table_select_line_markers.get(i);
			if(current_line[marker_location].equalsIgnoreCase(current_marker)){
				isSelect = 1;
			}
		}
		return isSelect;
	}

	//Method[G&S]
	public String getTable_name() {
		return table_name;
	}
	private void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	
	public HashMap<String, ArrayList<String>> getTable_info() {
		return table_info;
	}
	private void setTable_info(HashMap<String, ArrayList<String>> table_info) {
		this.table_info = table_info;
	}
	
	public HashMap<String, ArrayList<String[]>> getTable_data() {
		return this.table_data;
	}
	public void setTable_data(HashMap<String, ArrayList<String[]>> table_data){
		this.table_data = table_data;
	}
}
