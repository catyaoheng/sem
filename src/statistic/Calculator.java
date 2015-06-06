package statistic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sem.SEMDataPool;
import tools.Idmap;
import tools.Probe;
import util.FileUtil;

public abstract class Calculator {
	
	protected String calculator_name;
	protected String feature_name;
	protected HashMap<String, ArrayList<String>> calculator_info;
	protected HashMap<String, HashMap<String, ArrayList<String[]>>> calculator_data_source_pool;
	protected HashMap<String, HashMap<String, ArrayList<String[]>>> calculator_data_refer_pool;
	protected HashMap<String, ArrayList<String>> id_need_cal_pool;
	//protected HashMap<String, HashMap<String[], ArrayList<Object>>> value_pool;
	
	Calculator(){
		calculator_name = new String();
		feature_name = new String();
		calculator_info = new HashMap<String, ArrayList<String>>();
		calculator_data_source_pool = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
		calculator_data_refer_pool = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
		id_need_cal_pool = new HashMap<String, ArrayList<String>>();
		//value_pool = new HashMap<String, HashMap<String[], ArrayList<Object>>>();
	}
	
	public void initialize(
			String feature_name, 
			String calculator_name,
			HashMap<String, ArrayList<String>> calculator_info
			) {
		System.out.println("\t\t>Initializing " + calculator_name + "...");
		setCalculator_name(calculator_name);
		setFeature_name(feature_name);
		setCalculator_info(calculator_info);
		prepareCalculator_data_source_pool();
		prepareId_pool();
		operateDataRefer();
		System.out.println("\t\t\t" + calculator_name + " initialized Sucessfully\n");
	}

	//Method[0]
	public void calculateValue() {
		System.out.println("\t\t\t " + calculator_name + " Calculator is calculating ...");
		Iterator iter = calculator_data_source_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_entry_name = entry.getKey().toString();
			System.out.println("\t\t\t Calculating " + current_entry_name);
			HashMap<String, ArrayList<String[]>> current_data = calculator_data_source_pool.get(current_entry_name);
			//System.out.println("$$$$\t"+current_data.get("10473").get(0)[0]);
			ArrayList<String> current_id_need_cal = id_need_cal_pool.get(current_entry_name);
			/*/test
			for(int i=0; i<current_id_need_cal.size(); i++){
				System.out.println(current_id_need_cal.get(i));
			}
			//test*/
			//Prepare Container.
			ArrayList<ArrayList<String[]>> current_value_cluster_pool_10k = new ArrayList<ArrayList<String[]>>();
			ArrayList<String[]> current_value_pool_10k = new ArrayList<String[]>();
			int value_block_cutter = 0;
			int id_pool_depth = current_id_need_cal.size();
			//System.out.println("id_pool_depth = " + id_pool_depth);
			int id_matrix_size = (id_pool_depth*(id_pool_depth-1))/2;
			//System.out.println("$$$$\t"+((id_pool_depth*(id_pool_depth-1))/2));
			//System.out.println("$$$$\t" + id_pool_depth);
			for (int i=0; i<id_pool_depth; i++) {
				for (int j=i+1; j<id_pool_depth; j++) {

					String[] current_id_pair = new String[2];
					current_id_pair[0] = current_id_need_cal.get(i);
					current_id_pair[1] = current_id_need_cal.get(j);
					//System.out.println(value_block_cutter+"\t$$$$\t"+current_id_pair[0]+ "\t"+current_id_pair[1]);
					Object transformed_vector_cluster_0 = transformDataFormat(current_data.get(current_id_pair[0]));
					Object transformed_vector_cluster_1 = transformDataFormat(current_data.get(current_id_pair[1]));
					ArrayList<String[]> dotproduct = calculateDotProduct(current_id_pair ,transformed_vector_cluster_0, transformed_vector_cluster_1);
					//Probe.probeStringArray(dotproduct);
					String[] merged_pearsons_dotproduct = mergeValue(dotproduct);
					//Probe.probeStrings(merged_pearsons_dotproduct);
					current_value_cluster_pool_10k.add(dotproduct);
					current_value_pool_10k.add(merged_pearsons_dotproduct);
					value_block_cutter++;
					//Cut block & write file
					//System.out.println("$$$$\t"+value_block_cutter);
					if(value_block_cutter%(id_matrix_size/10) == 0){
						System.out.println("\t\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
					}
					if(value_block_cutter%100000 == 0){
						//distributed_value[0] is the cluster.
						ArrayList<Object> distributed_value_n_cluster = distributeValue(current_value_cluster_pool_10k, current_value_pool_10k);
						//ArrayList<Object> distributed_value_array = arrayDistributed_value(distributed_value);
						writeValue(distributed_value_n_cluster, current_entry_name);
						current_value_cluster_pool_10k = new ArrayList<ArrayList<String[]>>();
						current_value_pool_10k = new ArrayList<String[]>();
						distributed_value_n_cluster = new ArrayList<Object>();
					}
					if(((id_pool_depth*(id_pool_depth-1))/2-value_block_cutter) == 0){
						//System.out.println("\t\t\t ########### "+ current_value_pool_10k.size());
						//distributed_value[0] is the cluster.
						ArrayList<Object> distributed_value = distributeValue(current_value_cluster_pool_10k, current_value_pool_10k);
						writeValue(distributed_value, current_entry_name);
						distributed_value = new ArrayList<Object>();
					}
				}
			}
		System.out.println("\t\t\t\t " + calculator_name + " Calculation is finished.");
		}
	}
	
	

	private void prepareCalculator_data_source_pool(){
		setCalculator_data_source_pool(SEMDataPool.getData_pool(feature_name));
		if(calculator_info.containsKey("calculator_refer_source")){
			ArrayList<String> calculatorReferNamePool = calculator_info.get("calculator_refer_source");
			for(int i=0; i<calculatorReferNamePool.size(); i++){
				String currentCalclatorReferName = calculatorReferNamePool.get(i);
				calculator_data_refer_pool.put(currentCalclatorReferName, calculator_data_source_pool.get(currentCalclatorReferName));
				calculator_data_source_pool.remove(currentCalclatorReferName);
			}
		}
	}
	
	private void prepareId_pool() {
		Iterator iter =  calculator_data_source_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_entry_name = entry.getKey().toString();
			HashMap<String, ArrayList<String[]>> current_data = calculator_data_source_pool.get(current_entry_name);
			Iterator sub_iter = current_data.entrySet().iterator();
			ArrayList<String> current_id_not_empty = new ArrayList<String>();
			while(sub_iter.hasNext()){
				Map.Entry sub_entry = (Map.Entry)sub_iter.next();
				String current_sub_entry_name = sub_entry.getKey().toString();
				ArrayList<String[]> current_gene_cluster = current_data.get(current_sub_entry_name);
				if(!current_gene_cluster.isEmpty()){
					current_id_not_empty.add(current_sub_entry_name);
				}
			}
			id_need_cal_pool.put(current_entry_name, current_id_not_empty);
		}
	}
	
	/*
	private void prepareId_pool() {
		id_pool = SEMDataPool.getId_pair_pool();
	}
	//test*/
	
	//Method[1]
		//sub_method of calculateValue
	protected abstract void operateDataRefer();
	protected abstract Object transformDataFormat(ArrayList<String[]> cluster);
	protected abstract ArrayList<String[]> calculateDotProduct(String[] cluster_name, Object vector_cluster_0, Object vector_cluster_1);
	protected abstract String[] mergeValue(ArrayList<String[]> value_cluster);
	
	protected ArrayList<Object> distributeValue(
			ArrayList<ArrayList<String[]>> value_cluster_pool_10k,
			ArrayList<String[]> value_pool_10k
			) {
		ArrayList<Object> distributed_value =  new ArrayList<Object>();
		HashMap<Integer, ArrayList<String[]>> value_cluster_writer_pool = new HashMap<Integer, ArrayList<String[]>>();
		HashMap<Integer, ArrayList<String[]>> value_writer_pool = new HashMap<Integer, ArrayList<String[]>>();
		
		for(int i=0; i<value_pool_10k.size(); i++){
			ArrayList<String[]> current_value_str_cluster = value_cluster_pool_10k.get(i);
			String[] current_value_str = value_pool_10k.get(i);
			int n = Idmap.getGene_idmap().size();
			int index_0 = Integer.valueOf(current_value_str[0]).intValue();
			int index_1 = Integer.valueOf(current_value_str[1]).intValue();
			int sum_index = (((2*n-1-index_0)*index_0)/2)+index_1-index_0;
			Integer file_num = sum_index/100000;
			if(value_cluster_writer_pool.containsKey(file_num)){
				value_cluster_writer_pool.get(file_num).addAll(current_value_str_cluster);
				value_writer_pool.get(file_num).add(current_value_str);
				//System.out.println("$$$$\t" + current_value_str[2]);
			}
			else{
				ArrayList<String[]> current_value_cluster_writer = new ArrayList<String[]>();
				ArrayList<String[]> current_value_writer = new ArrayList<String[]>();
				current_value_cluster_writer.addAll(current_value_str_cluster);
				current_value_writer.add(current_value_str);				
				value_cluster_writer_pool.put(file_num, current_value_cluster_writer);
				value_writer_pool.put(file_num, current_value_writer);
			}
			//System.out.println(file_num);
		}
		distributed_value.add(value_cluster_writer_pool);
		distributed_value.add(value_writer_pool);
		return distributed_value;
	}
	
	/*/
	private ArrayList<Object> arrayDistributed_value(ArrayList<Object> distributed_value) {
		ArrayList<Object> distributed_value_array =  new ArrayList<Object>();
		HashMap<Integer, HashMap<String[], ArrayList<Object>>> value_cluster_writer_pool = 
				(HashMap<Integer, HashMap<String[], ArrayList<Object>>>) distributed_value.get(0);
		HashMap<Integer, HashMap<String[], Object>> value_writer_pool = 
				(HashMap<Integer, HashMap<String[], Object>>)distributed_value.get(1);
		Iterator iter = value_cluster_writer_pool.entrySet().
		return distributed_value_array;
	}
	//*/
	
	//distributed_value_n_cluster[0] is the cluster
	protected void writeValue(ArrayList<Object> distributed_value_n_cluster, String current_data_source_name){
		HashMap<Integer, ArrayList<String[]>> value_cluster_writer_pool = (HashMap<Integer, ArrayList<String[]>>) distributed_value_n_cluster.get(0);
		HashMap<Integer, ArrayList<String[]>> value_writer_pool = (HashMap<Integer, ArrayList<String[]>>) distributed_value_n_cluster.get(1);
		Iterator iter = value_cluster_writer_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String current_entry_name = entry.getKey().toString();
			Integer current_entry_num = Integer.valueOf(current_entry_name).intValue();
			FileUtil.writeFile("value/" + feature_name + "/" + current_data_source_name + '_' + calculator_name, current_entry_name, value_writer_pool.get(current_entry_num), true, false);
			FileUtil.writeFile("value_cluster/" + feature_name + "/" + current_data_source_name + '_' + calculator_name , current_entry_name, value_writer_pool.get(current_entry_num), true, false);
		}
	}
	
	//Method[G&S]
	private void setCalculator_name(String calculator_name){
		this.calculator_name = calculator_name;
	}
	
	private void setFeature_name(String feature_name){
		this.feature_name = feature_name;
	}
	
	private void setCalculator_info(HashMap<String, ArrayList<String>> calculator_info){
		this.calculator_info = calculator_info;
	}
	
	private void setCalculator_data_source_pool(HashMap<String, HashMap<String, ArrayList<String[]>>> calculator_data_source_pool){
		this.calculator_data_source_pool = calculator_data_source_pool;
	}
	public HashMap<String, HashMap<String, ArrayList<String[]>>> getCalculator_data_source_pool() {
		return calculator_data_source_pool;
	}
	public HashMap<String, HashMap<String, ArrayList<String[]>>> getCalculator_data_refer_pool() {
		return calculator_data_refer_pool;
	}

	//public HashMap<String, HashMap<String[], ArrayList<Object>>> getValue_pool() {
	//	return value_pool;
	//}
}
