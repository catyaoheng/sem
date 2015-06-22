package tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Config;
import util.FileUtil;

public class Idmap {
	private HashMap<String, ArrayList<String>> idmap_info_pool;
	private ArrayList<String> feature_name_pool;
	private HashMap<String, ArrayList<String>> feature_info_pool;
	private HashMap<String, String[]> file_needmap_info_pool;//file_name - index_num, id_mapped_rate, line_mapped_rate(rates were all *100), ref_info
	private HashMap<String, String[]> file_mapped_info_pool;//same as file_info_pool
	private HashMap<String, String> ref_idmap_pool;//file_name-ref_idmap_name
	//static
	private static ArrayList<String[]> gene_idmap;
	private static ArrayList<String[]> map_result;//file_name, id_mapped_rate, line_mapped_rate(rates were all *100), ref_info

	public Idmap(){
		idmap_info_pool = new HashMap<String,ArrayList<String>>();
		feature_name_pool = new ArrayList<String>();
		feature_info_pool = new HashMap<String, ArrayList<String>>();
		file_needmap_info_pool = new HashMap<String,String[]>();
		file_mapped_info_pool = new HashMap<String,String[]>();
		ref_idmap_pool = new HashMap<String, String>();
	}
	
	public void initialize(){
		System.out.println("Initializing Idmap ...");
		gene_idmap = null;
		gene_idmap = new ArrayList<String[]>();
		setGene_idmap(FileUtil.loadFile("idmap", "gene_idmap"));
		setMap_result(FileUtil.loadFile("idmap", "map_result"));
		prepareIdmap_info();
		System.out.println("\tIdmap Initialized Sucessfully\n");
	}
	
	//Method[0]
	private void prepareIdmap_info() {
		idmap_info_pool = Config.getCONFIG_INFO("idmap").get("idmap");
		//test
//		System.out.println("###" + idmap_info_pool.toString());
		prepareFeature_name();
		prepareFile_info();
		prepareMap_result();
		writeMap_result();
	}

	//Method[1]
	private void prepareFeature_name(){
		ArrayList<String> total_feature_name_pool = Config.getTOTAL_FEATURE_NAME_POOL();
		for(int i=0; i<total_feature_name_pool.size(); i++){
			String current_feature_name = total_feature_name_pool.get(i);
			String current_feature_is_mapped = new String();
			if(idmap_info_pool.containsKey(current_feature_name)){
				current_feature_is_mapped = idmap_info_pool.get(current_feature_name).get(0);
				if(current_feature_is_mapped.equalsIgnoreCase("0")){
					feature_name_pool.add(current_feature_name);
				}
			}
		}
		/*test
		for(int i=0; i<feature_name_pool.size(); i++){
			System.out.println("###" + feature_name_pool.get(i));
		}
		*/
	}
	
	private void prepareFile_info(){
		for(int i=0; i<feature_name_pool.size(); i++){
			String current_feature_name = feature_name_pool.get(i);
			HashMap<String, HashMap<String, ArrayList<String>>>current_feature_info_pool = Config.getCONFIG_INFO(current_feature_name);
			//System.out.println(current_feature_info_pool);
			ArrayList<String> current_file_name_pool = current_feature_info_pool.get(current_feature_name).get("file_names");
			ArrayList<String> current_file_need_map_name_pool = new ArrayList<String>();
			for(int j=0; j<current_file_name_pool.size(); j++){
				String current_file_name = current_file_name_pool.get(j);
				//System.out.println(current_file_name);
				if(current_feature_info_pool.containsKey(current_file_name)){
					HashMap<String, ArrayList<String>> current_file_info_pool = current_feature_info_pool.get(current_file_name);
					//System.out.println("\t" + current_file_info_pool.toString());
					if(current_file_info_pool.containsKey("file_index_column_nums")){
						//Write the current_file_need_map_name into feature_info_pool's current_file_need_map_nam_pool
						current_file_need_map_name_pool.add(current_file_name);
						//Prepare current_file_info
						String[] current_file_info = new String[4];
						current_file_info[3] = "NA";
						String file_index_column_num = current_file_info_pool.get("file_index_column_nums").get(0);
						current_file_info[0] = file_index_column_num;
						//Prepare ref_info.
						String ref_idmap_name = new String();
						if(current_file_info_pool.containsKey("ref_idmap")){
							ref_idmap_name = current_file_info_pool.get("ref_idmap").get(0);
							current_file_info[3] = current_file_info_pool.get("ref_idmap").get(0);
							ref_idmap_pool.put(current_file_name, ref_idmap_name);
						}
						file_needmap_info_pool.put(current_file_name, current_file_info);
					}
					//System.out.println("###" + file_needmap_info_pool.toString());
				}
				feature_info_pool.put(current_feature_name, current_file_need_map_name_pool);
			}
			//for(String s: current_file_info){System.out.print(s+"\t");}
			//System.out.print("\n");
		}
	}
	
	private	void prepareMap_result(){
			for(int i=0; i<map_result.size(); i++){
				String[] current_file_map_result = map_result.get(i);
				String current_file_name = current_file_map_result[0];
//				System.out.println(current_file_name);
				String[] current_file_info = new String[4];
				current_file_info[0] = "NA";
				for(int j=1; j<current_file_info.length; j++){
					current_file_info[j] = current_file_map_result[j];
				}
				/*/test
				System.out.println(current_file_name);
				for(String s: current_file_info){System.out.print("$$$"+s);}
				 */
				file_mapped_info_pool.put(current_file_name, current_file_info);
			}
		/*test
		for(int i=0; i<file_name_pool.size(); i++){
			System.out.println("$$$" + file_name_pool.get(i));
		}
		*/
	}
	
	public void mapAll(){
		/*/
		System.out.println(feature_name_pool.size());
		//*/
		for(int i=0; i<feature_name_pool.size(); i++){
			String current_feature_name = feature_name_pool.get(i);
			mapFeature(current_feature_name);
		}
		writeMap_result();
	}
	
	public static String mapLine(String[] line){
		String[] current_line = line;
		for(int j=0; j<gene_idmap.size(); j++){
			String[] current_id_pool = gene_idmap.get(j);
			String current_id_str = current_id_pool[0];
			String line_id = current_line[1];
			if(current_id_str.equalsIgnoreCase(line_id)){
				return	current_id_str;
			}
		}
		return "NA";
	}
	
	//Method[2]
	private void mapFeature(String feature_name){
		ArrayList<String> current_feature_info  = feature_info_pool.get(feature_name);
		System.out.println(current_feature_info.size());
		for(int i=0; i<current_feature_info.size(); i++){
			String current_file_name = current_feature_info.get(i);
			mapFile(feature_name, current_file_name, true);
		}
	}
	
	public void mapFile(String feature_name, String file_name, boolean is_rewrite_map_result){
		System.out.println("\tMapping Index of " + file_name + " to gene_idmap");
		//Initialize.
		String[] current_file_info = file_needmap_info_pool.get(file_name);
		ArrayList<String[]> current_file = FileUtil.loadFile(feature_name, file_name);
		ArrayList<String[]> mapped_file = new ArrayList<String[]>();
		//See if the file is mapped.
		if(!current_file.get(0)[0].equalsIgnoreCase("Mapped")){
			int mapped_line_num = 0;
			int index_num = Integer.valueOf(current_file_info[0]).intValue();
			//See if the file need an ref_idmap. If yes, replace the index with ref_index.
			if(ref_idmap_pool.containsKey(file_name)){
				refMap(feature_name, file_name, current_file, index_num);
			}
			HashMap<String, Boolean> mapped_id = new HashMap<String, Boolean>();
			System.out.println("\t\tMapping ...");
			for(int i=0; i<current_file.size(); i++){
				if(i%(current_file.size()/10) == 0){System.out.println("\t\t" + (i/(current_file.size()/10))*10 + "% ..." + new Date());}
				//Prepare the parameter and container.
				String[] current_line = current_file.get(i);
				String current_index_str = current_line[index_num];
				String[] mapped_line = new String[current_line.length+2];
				HashMap<String, Integer> mapped_geneid = new HashMap<String, Integer>();
				mapped_line[0] = "Mapped";
				int is_mapped = 0;
				/*/test
				System.out.print("##"+i+"##");
				for(String s: current_line){System.out.print(s+"\t");}
				System.out.print("\n");
				//test*/				
				for(int j=0; j<gene_idmap.size(); j++){
					String[] current_id_pool = gene_idmap.get(j);
					for(int k=1; (k<current_id_pool.length)&&(is_mapped==0); k++){
						String current_id_str = current_id_pool[k];
						//System.out.print("##"+i+"##");
						Pattern map_id = Pattern.compile(current_id_str, Pattern.CASE_INSENSITIVE);
						//System.out.println("$$"+i+"$$");
						Matcher is_in_map = map_id.matcher(current_index_str);
						//System.out.println("$$"+ current_index_str +"$$" + current_id_str);
						if(is_in_map.find()){
							mapped_geneid.put(current_id_pool[0], 1);
							is_mapped = 1;
							mapped_line_num++;
						}	
					}
				}
				if(is_mapped==1){
					Iterator iter = mapped_geneid.entrySet().iterator();
					while(iter.hasNext()){
						Map.Entry entry = (Map.Entry)iter.next();
						String current_id_str = entry.getKey().toString();
						mapped_line[1] = current_id_str;
						for(int l=0;l<current_line.length;l++){
							mapped_line[l+2] = current_line[l];
						}
						mapped_file.add(mapped_line);
						mapped_id.put(current_id_str, true);
					}
					
				}
				else {
					//System.out.print("$$"+i+"$$");
					//for(String s: current_line){System.out.print(s+"\t");}
					//System.out.print("\n");
					mapped_line[1] = "NA";
					for(int l=0;l<current_line.length;l++){
						mapped_line[l+2] = current_line[l];
					}
					mapped_file.add(mapped_line);
				}
			}
			//Rewrite file.
			/*/test
			for(int i=0; i<10; i++){
				for(String s: mapped_file.get(i)){System.out.print(s);}
				System.out.print("\n");
			}
			*/
			//Write mapped file
			FileUtil.writeFile(feature_name, file_name, mapped_file, false, true);
			//Statistics.
			double id_mapped_rate = 0.0;
			double line_mapped_rate = 0.0;
			int total_id_num = gene_idmap.size();
			int total_line_num = current_file.size();
			int mapped_id_num = mapped_id.size();
			id_mapped_rate = (mapped_id_num*1.0) / (total_id_num *1.0);
			line_mapped_rate = (mapped_line_num*1.0) / (total_line_num *1.0);
			System.out.println("\t\t" + String.format("%.2f", id_mapped_rate *100.0) + " % gene_idmap's ids has(ve) been mapped by " + file_name);
			System.out.println("\t\t" + String.format("%.2f", line_mapped_rate *100.0) + " % " + file_name + "'s entry(ies) has(ve) been mapped to gene_idmap");
			//Write map result.
			if(is_rewrite_map_result == true){
				current_file_info[1] = String.format("%.2f", id_mapped_rate *100.0);
				current_file_info[2] = String.format("%.2f", line_mapped_rate *100.0);
				file_mapped_info_pool.put(file_name, current_file_info);
			}
		}
		else{
			String[] current_mapped_file_info = file_mapped_info_pool.get(file_name);
			System.out.println("\t\t" + current_mapped_file_info[1] + " % gene_idmap's ids has(ve) been mapped by " + file_name);
			System.out.println("\t\t" + current_mapped_file_info[2] + " % " + file_name + "'s entry(ies) has(ve) been mapped to gene_idmap");
		}
		//test
		writeMap_result();
	}
	
	//Method[3]
	private void refMap(String feature_name ,String file_name, ArrayList<String[]> current_file, int index_num) {
		String current_ref_idmap_name = ref_idmap_pool.get(file_name);
		System.out.println("\tReferring Index of " + file_name + " by " + current_ref_idmap_name);	
		ArrayList<String[]> current_ref_idmap = FileUtil.loadFile("idmap", current_ref_idmap_name);
		ArrayList<String[]> referred_file = new ArrayList<String[]>();
		int value_block_cutter = 0;
		int id_matrix_size = current_file.size();
		for(int i=0; i<current_file.size(); i++){
			value_block_cutter++;
			//*//
			if(value_block_cutter%(id_matrix_size/10) == 0){
				System.out.println("\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
			}
			//*/
			String[] current_line = current_file.get(i);
			String[] referred_line = current_line;
			String current_original_id = current_line[index_num];
			String referred_id = current_original_id;
			for(int j=0; j<current_ref_idmap.size(); j++){
				String current_id_anchor = current_ref_idmap.get(j)[0];
				String current_ref_id = new String();
				try{
					current_ref_id = "|" +current_ref_idmap.get(j)[1];
				}catch (Exception e){
					current_ref_id = "";
				}
				if(current_id_anchor.equalsIgnoreCase(current_original_id)){
					referred_id = referred_id + current_ref_id;
				}
				if(j == (current_ref_idmap.size()-1)){
				referred_line[index_num] = referred_id;
				}
			}
			referred_file.add(referred_line);
		}
		FileUtil.writeFile(feature_name, file_name, referred_file, false, true);
	}
	
	private void prepareMap_log_head() {
		//Map writer.
		ArrayList<String[]> map_log_rewrite = new ArrayList<String[]>();
		//Prepare map_log_head.
		ArrayList<String> map_log_head = new ArrayList<String>();
		map_log_head.add("gene_id");
			//add feature name
		for(int i=0; i<Config.getTOTAL_FEATURE_NAME_POOL().size(); i++){
			map_log_head.add(Config.getTOTAL_FEATURE_NAME_POOL().get(i));
		}
			//add file name
		for(int i=0; i<Config.getTOTAL_FILE_NAME_POOL().size(); i++){
			map_log_head.add(Config.getTOTAL_FILE_NAME_POOL().get(i));
		}
	}
	private void writeMap_log_head() {
		
	}
	
	private void writeMap_result() {
		ArrayList<String[]> map_result_rewrite = new ArrayList<String[]>();
		Iterator iter = file_mapped_info_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_file_name = entry.getKey().toString();
			String[] current_file_info = file_mapped_info_pool.get(current_file_name);
			//for(String s:current_file_info){System.out.println("##"+s);}
			String[] current_file_info_rewrite = new String[4];
			current_file_info_rewrite[0] = current_file_name;
			for(int i=1; i<current_file_info_rewrite.length; i++){
				current_file_info_rewrite[i] = current_file_info[i];
			}
			map_result_rewrite.add(current_file_info_rewrite);
		}
		FileUtil.writeFile("idmap", "map_result", map_result_rewrite, false, false);
	}
	
	//Method[G&S]
	public static ArrayList<String[]> getGene_idmap() {
		return gene_idmap;
	}
	public static void setGene_idmap(ArrayList<String[]> gene_idmap) {
		Idmap.gene_idmap = gene_idmap;
	}
	public static void setMap_result(ArrayList<String[]> map_result) {
		Idmap.map_result = map_result;
	}
}








