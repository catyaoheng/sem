package feature;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import util.Config;

public class State{

	private HashMap<String, HashMap<String, ArrayList<String>>> state_data;
	private String feature_name;
	private String folder_name;
	private String value_num;
	private ArrayList<String> file_name_pool;
	private ArrayList<String> table_name_pool;
	private ArrayList<String> matrix_name_pool;
	private ArrayList<String> calculator_name_pool;
	private HashMap<String, HashMap<String, ArrayList<String>>> file_info_pool;
	private HashMap<String, HashMap<String, ArrayList<String>>> table_info_pool;
	private HashMap<String, HashMap<String, ArrayList<String>>> matrix_info_pool;
	private HashMap<String, HashMap<String, ArrayList<String>>> calculator_info_pool;

	public State(){
		state_data = new HashMap<String, HashMap<String, ArrayList<String>>>();
		feature_name = new String();
		folder_name = new String();
		value_num = new String();
		file_name_pool = new ArrayList<String>();
		table_name_pool = new ArrayList<String>();
		matrix_name_pool = new ArrayList<String>();
		calculator_name_pool = new ArrayList<String>();
		file_info_pool = new HashMap<String, HashMap<String, ArrayList<String>>>();
		table_info_pool = new HashMap<String, HashMap<String, ArrayList<String>>>();
		matrix_info_pool = new HashMap<String, HashMap<String, ArrayList<String>>>();
		calculator_info_pool = new HashMap<String, HashMap<String, ArrayList<String>>>();
	}
	
	public void initialize(String feature_name){
		System.out.println("\t>Initializing State of " + feature_name +" ...");
		setState_data(Config.getCONFIG_INFO(feature_name));
		setFeature_info(feature_name);
		setState();
		testState();
		System.out.println("\t\tState of " + feature_name +" initialized sucessfully");
	}
	
	//Method[0]
	private void setFeature_info(String feature_name){
		setFeature_name(feature_name);
		setFolder_name(state_data.get(feature_name).get("folder_name").get(0));
		setValue_num(state_data.get(feature_name).get("value_num").get(0));
	}
	
	private void setState(){
		prepareFile_info_pool();
		prepareTable_info_pool();
		prepareMatrix_info_pool();
		prepareCalculator_info_pool();
	}
	
	//Method[1]
	private void prepareFile_info_pool() {
		setFile_name_pool(state_data.get(feature_name).get("file_names"));
		for(int i=0; i<file_name_pool.size(); i++){
			String file_name = file_name_pool.get(i);
			//System.out.println("###" + file_name);
			HashMap<String, ArrayList<String>> file_info = state_data.get(file_name);
			file_info_pool.put(file_name, file_info);
			//System.out.println(file_info_pool.get(file_name));
		}
	}
	
	private void prepareTable_info_pool(){
		setTable_name_pool(state_data.get(feature_name).get("table_names"));
		for(int i=0; i<table_name_pool.size(); i++){
			String table_name = table_name_pool.get(i);
			HashMap<String, ArrayList<String>> table_info = state_data.get(table_name);
			table_info_pool.put(table_name, table_info);
		}
	}
	
	private void prepareMatrix_info_pool(){
		setMatrix_name_pool(state_data.get(feature_name).get("matrix_names"));
		for(int i=0; i<matrix_name_pool.size(); i++){
			String matrix_name = matrix_name_pool.get(i);
			HashMap<String, ArrayList<String>> matrix_info = state_data.get(matrix_name);
			matrix_info_pool.put(matrix_name, matrix_info);
		}
	}
	
	private void prepareCalculator_info_pool(){
		setCalculator_name_pool(state_data.get(feature_name).get("calculator_names"));
		for(int i=0; i<calculator_name_pool.size(); i++){
			String calculator_name = calculator_name_pool.get(i);
			HashMap<String, ArrayList<String>> calculator_info = state_data.get(calculator_name);
			calculator_info_pool.put(calculator_name, calculator_info);
		}
	}
	
	//Method[2]
	//Method[G&S]
	public HashMap<String, HashMap<String, ArrayList<String>>> getState_data() {
		return state_data;
	}
	public void setState_data(HashMap<String, HashMap<String, ArrayList<String>>> state_data) {
		this.state_data = state_data;
	}
	
	public String getFeature_name() {
		return feature_name;
	}
	public void setFeature_name(String feature_name) {
		this.feature_name = feature_name;
	}

	public String getFolder_name() {
		return folder_name;
	}
	public void setFolder_name(String folder_name) {
		this.folder_name = folder_name;
	}
	
	public String getValue_num() {
		return value_num;
	}
	public void setValue_num(String value_num) {
		this.value_num = value_num;
	}	
		
	public ArrayList<String> getFile_name_pool() {
		return file_name_pool;
	}
	public void setFile_name_pool(ArrayList<String> file_names) {
		this.file_name_pool = file_names;
	}
	
	public ArrayList<String> getTable_name_pool() {
		return table_name_pool;
	}
	public void setTable_name_pool(ArrayList<String> table_names) {
		this.table_name_pool = table_names;
	}

	public ArrayList<String> getMatrix_name_pool() {
		return matrix_name_pool;
	}
	public void setMatrix_name_pool(ArrayList<String> matrix_names) {
		this.matrix_name_pool = matrix_names;
	}

	public ArrayList<String> getCalculator_name_pool() {
		return calculator_name_pool;
	}
	public void setCalculator_name_pool(ArrayList<String> calculator_names) {
		this.calculator_name_pool = calculator_names;
	}
	
	public HashMap<String, ArrayList<String>> getFile_info(String file_name) {
		return file_info_pool.get(file_name);
	}
	
	public HashMap<String, ArrayList<String>> getTable_info(String table_name) {
		return table_info_pool.get(table_name);
	}
	
	public HashMap<String, ArrayList<String>> getMatrix_info(String matrix_name) {
		return matrix_info_pool.get(matrix_name);
	}
	
	public HashMap<String, ArrayList<String>> getCalculator_info(String calculator_name) {
		return calculator_info_pool.get(calculator_name);
	}
	
	//Method[test]
	public void briefState(){
		System.out.println("\t>Brifing State");
		System.out.println("\t\tThis is the State of "+ feature_name);
		System.out.println("\t\t\t=== Feature Info ===");
		System.out.println("\t\t\t\t=== feature_name =" + " "+ feature_name);
		System.out.println("\t\t\t\t=== folder_name =" + " "+ folder_name);
		System.out.println("\t\t\t\t=== value_num =" + " "+ value_num);
		System.out.println("\t\t\t=== State Data ===");
		//brief file
		System.out.println("\t\t\t\t=== File Num = " + file_info_pool.size());
		
		//brief table
		System.out.println("\t\t\t\t=== Table Num = " + table_info_pool.size());

		//brief matrix
		System.out.println("\t\t\t\t=== Matrix Num = " + matrix_info_pool.size());

		//brief calculator
		System.out.println("\t\t\t\t=== Calculator Num = " + calculator_info_pool.size());
		System.out.println("\t\tState Briefing Finished");
	}
	
	public void testState(){
		System.out.println("\t>Testing State");
		System.out.println("\t\tThis is the State of "+ this.feature_name);
		System.out.println("\t\t\t=== Feature Info ===");
		System.out.println("\t\t\t\t=== feature_name ===\n" + "\t\t\t\t\t"+ this.feature_name);
		System.out.println("\t\t\t\t=== folder_name ===\n" + "\t\t\t\t\t"+ this.folder_name);
		System.out.println("\t\t\t\t=== value_num ===\n" + "\t\t\t\t\t"+ this.value_num);
		System.out.println("\t\t\t=== State Data ===");
		//test file
		System.out.println("\t\t\t\t=== File Info ===");
		System.out.println("\t\t\t\t\tKey\tValuetoString");	
			Iterator file_iter = file_info_pool.entrySet().iterator();
			while (file_iter.hasNext()){
				Map.Entry entry =(Map.Entry) file_iter.next();
				String entryName = entry.getKey().toString();
				if(file_info_pool.get(entryName) == null){
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\tdefualt");
				}
				else{
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\t" + entry.getValue().toString());
				}
			}
		
		//test table
		System.out.println("\t\t\t\t=== Table Info ===");
		System.out.println("\t\t\t\t\tKey\tValuetoString");
			Iterator table_iter = table_info_pool.entrySet().iterator();
			while (table_iter.hasNext()){
				Map.Entry entry =(Map.Entry) table_iter.next();
				String entryName = entry.getKey().toString();
				if(table_info_pool.get(entryName) == null){
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\tdefualt");
				}
				else{
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\t" + entry.getValue().toString());
				}
			}

		//test matrix
		System.out.println("\t\t\t\t=== Matrix Info ===");
		System.out.println("\t\t\t\t\tKey\tValuetoString");
			Iterator matrix_iter = matrix_info_pool.entrySet().iterator();
			while (matrix_iter.hasNext()){
				Map.Entry entry =(Map.Entry) matrix_iter.next();
				String entryName = entry.getKey().toString();
				if(matrix_info_pool.get(entryName) == null){
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\tdefualt");
				}
				else{
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\t" + entry.getValue().toString());
				}
			}
		
		//test calculator
		System.out.println("\t\t\t\t=== Calculator Info ===");
		System.out.println("\t\t\t\t\tKey\tValuetoString");
			Iterator calculator_iter = calculator_info_pool.entrySet().iterator();
			while (calculator_iter.hasNext()){
				Map.Entry entry =(Map.Entry) calculator_iter.next();
				String entryName = entry.getKey().toString();
				if(calculator_info_pool.get(entryName) == null){
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\tdefualt");
				}
				else{
					System.out.println("\t\t\t\t\t" + entry.getKey() + "\t" + entry.getValue().toString());
				}
			}
		System.out.println("\t\tState Testing Finished");
	}
/*	
	public static void state(String[] args){
		System.out.println("State Ran Sucessfully");
		System.out.println(new Date());
	}
*/


}