package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import statistic.PearsonsCalculator;
import tools.Probe;

/**
 * This class is designed to create any container of a formated matrix
 * Notice: the class member vector is a special kind of line,
 * which contains only numbers. 
 * @author hyao
 * @version 0.1.0
 * @date 2014.12
 */
public class FormatedMatrix{
	
	private String matrix_name;
	private HashMap<String, ArrayList<String>> matrix_info;
	private HashMap<String, HashMap<String, ArrayList<String[]>>> matrix_data_source_pool;
	private HashMap<String, ArrayList<String[]>> matrix_data_source;
	private HashMap<String, ArrayList<String[]>> matrix_data;
	
	public FormatedMatrix(){
		matrix_name = new String();
		matrix_info = new HashMap<String, ArrayList<String>>();
		matrix_data_source_pool= new HashMap<String, HashMap<String, ArrayList<String[]>>>();
		matrix_data_source = new HashMap<String, ArrayList<String[]>>();
		matrix_data = new HashMap<String, ArrayList<String[]>>();
	}
	
	public void initialize(
			String matrix_name,
			HashMap<String, ArrayList<String>> matrix_info,
			HashMap<String, HashMap<String, ArrayList<String[]>>> matrix_data_source_pool
			){
		System.out.println("\t\t>Initializing " + matrix_name + "...");
		setMatrix_name(matrix_name);
		setMatrix_info(matrix_info);
		setMatrix_data_source_pool(matrix_data_source_pool);
		prepare_Matrix_data_source();
		setMatrix_data(matrix_data_source);
		System.out.println("\t\t\t" + matrix_name + " initialized Sucessfully\n");
	}
	
	//Method[0]
	private void prepare_Matrix_data_source() {
		String matrix_data_source_name = matrix_info.get("matrix_data_source").get(0);
		setMatrix_data_source(matrix_data_source_pool.get(matrix_data_source_name));
		//Probe.probeMap(matrix_data_source_name, matrix_data_source, false);
		Iterator iter = matrix_data_source.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_entry_name = entry.getKey().toString();
			ArrayList<String[]>current_cluster = matrix_data_source.get(current_entry_name);
			for(int i=0; i<current_cluster.size(); i++){
				String[] current_vector = current_cluster.get(i);
				current_vector = prepareMatrix_vector(current_vector);
				current_cluster.set(i, current_vector);
			}
			matrix_data_source.put(current_entry_name, current_cluster);
		}
	} 
	
	//Method[1]
	private String[] prepareMatrix_vector(String[] current_vector) {
		//Choose the non-blank element.
		ArrayList<String> vector_array = new ArrayList<String>();
		for(int i=0; i<current_vector.length; i++){
			if(!current_vector[i].equalsIgnoreCase("")){
				vector_array.add(current_vector[i]);
			}
		}
		//Rearrange the vector.
		String[] vector = new String[vector_array.size()];
		for(int i=0; i<vector.length; i++){
			vector[i] = vector_array.get(i);
		}
		return vector;
	}

	//Method[G&S]
	public String getMatix_name() {
		return matrix_name;
	}
	private void setMatrix_name(String matrix_name) {
		this.matrix_name = matrix_name;
	}
	
	public HashMap<String, ArrayList<String>> getMatrix_info() {
		return matrix_info;
	}
	private void setMatrix_info(HashMap<String, ArrayList<String>> matrix_info) {
		this.matrix_info = matrix_info;
	}
	
	public HashMap<String, HashMap<String, ArrayList<String[]>>> getMatrix_data_source_pool(){
		return this.matrix_data_source_pool;
	}
	private void setMatrix_data_source_pool(HashMap<String, HashMap<String, ArrayList<String[]>>> matrix_data_source_pool) {
		this.matrix_data_source_pool = matrix_data_source_pool;
	}
	
	private void setMatrix_data_source(HashMap<String, ArrayList<String[]>> matrix_data_source) {
		this.matrix_data_source = matrix_data_source;
	}
	
	public HashMap<String, ArrayList<String[]>> getMatrix_data() {
		return this.matrix_data;
	}
	public void setMatrix_data(HashMap<String, ArrayList<String[]>> matrix_data){
		this.matrix_data = matrix_data;
	}
}