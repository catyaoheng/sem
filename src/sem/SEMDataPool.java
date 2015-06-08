package sem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tools.Idmap;
import data.FormatedMatrix;

public class SEMDataPool{
	private static HashMap<String, HashMap<String, HashMap<String, ArrayList<String[]>>>> data_pool;
	//private static HashMap<String, ArrayList<String[]>> id_pair_pool;

	public SEMDataPool(){
		
	}
	
	public void initialize(){
		data_pool = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String[]>>>>();
		//id_pair_pool = new HashMap<String, ArrayList<String[]>>();
		//prepareId_pair_pool();
	}
	
	/*test
	private void prepareId_pair_pool(){
		ArrayList<String> gene_index = new ArrayList<String>();
		for(int i=0; i<Idmap.getGene_idmap().size(); i++){
			gene_index.add(Idmap.getGene_idmap().get(i)[0]);
		}
		int cutter =0;
		ArrayList<String[]> id_pair_pool_block = new ArrayList<String[]>();
		int depth = gene_index.size();
		for (int i=0; i<depth; i++) {
			for (int j = i+1; j <depth; j++) {
				String[] current_pair = new String[2];
				current_pair[0] = gene_index.get(i);
				current_pair[1] = gene_index.get(j);
				cutter = cutter + 1;
				id_pair_pool_block.add(current_pair);
				if(cutter%(((depth*(depth-1))/2)/1000) == 0){
					System.out.println("\t\t" + ((cutter/(depth*(depth-1))/2))*100 + "% ...");
				}
				if(cutter%10000 == 0){
					String current_id_pair_block_name = cutter + "";
					id_pair_pool.put(current_id_pair_block_name, id_pair_pool_block);
					id_pair_pool_block = new ArrayList<String[]>();
				}
				if(((depth*(depth-1))/2-cutter) == 1){
					String current_id_pair_block_name = cutter + "";
					id_pair_pool.put(current_id_pair_block_name, id_pair_pool_block);
				}
			}
		}
	}
	//test*/
	
	public void pushMatrix(String feature_name, HashMap<String, FormatedMatrix> matrix_pool) {
		HashMap<String, HashMap<String, ArrayList<String[]>>> matrix_data_pool = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
		Iterator iter = matrix_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_entry_name = entry.getKey().toString();
			HashMap<String, ArrayList<String[]>> current_matrix_data = matrix_pool.get(current_entry_name).getMatrix_data();
			matrix_data_pool.put(current_entry_name, current_matrix_data);
		}
		setData_pool(feature_name, matrix_data_pool);
	}

	//Method[G&S]
	public static HashMap<String, HashMap<String, ArrayList<String[]>>> getData_pool(String feature_name) {
		return data_pool.get(feature_name);
	}
	private static void setData_pool(String feature_name, HashMap<String, HashMap<String, ArrayList<String[]>>> data) {
		SEMDataPool.data_pool.put(feature_name, data);
	}
	/*
	public static HashMap<String, ArrayList<String[]>> getId_pair_pool() {
		return id_pair_pool;
	}
	//test*/
	
}