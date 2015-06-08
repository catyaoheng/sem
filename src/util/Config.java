package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Main;

public class Config{
	//parameter
	private String[] config_splitor = {"= ",", "};
	private int max_indentation_num = 0;
	//container
	private ArrayList<String[]> config_data;
	private HashMap<String, ArrayList<String[]>> onion;
	//static
	private static HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> CONFIG_INFO_POOL;
	private static ArrayList<String> TOTAL_FEATURE_NAME_POOL;
	private static ArrayList<String> TOTAL_FILE_NAME_POOL;
	private static HashMap<String, HashMap<String,String>> PATHWAYS;


	public Config(){
		config_data = new ArrayList<String[]>();
		onion = new HashMap<String, ArrayList<String[]>>();
	}
		
	public void initialize(){
		System.out.println(">Initializing Config ...");
		CONFIG_INFO_POOL = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();
		TOTAL_FEATURE_NAME_POOL = new ArrayList<String>();
		TOTAL_FILE_NAME_POOL = new ArrayList<String>();
		PATHWAYS = new HashMap<String, HashMap<String, String>>();
		setConfig();
		System.out.println("\tConfig Initialized Sucessfully\n");
	}
	
	//Methods[0]
	private void setConfig(){
		prepareConfiginfo();
		preparePathway();
		prepareData();
		prepareOnion();
		prepareConfig();
		preparePathway();
	}
	
	//Methods[1]
	private void prepareConfiginfo(){
		HashMap<String, HashMap<String, ArrayList<String>>>config_section_info =new HashMap<String, HashMap<String, ArrayList<String>>>();
		HashMap<String, ArrayList<String>> config_meta_section_info = new HashMap<String, ArrayList<String>>();
		ArrayList<String> config_meta_line_info = new ArrayList<String>();
		config_meta_line_info.add("config");
		config_meta_section_info.put("feature_name",config_meta_line_info);
		config_meta_section_info.put("folder_name",config_meta_line_info);
		config_meta_section_info.put("file_names",config_meta_line_info);
		config_section_info.put("config", config_meta_section_info);
		CONFIG_INFO_POOL.put("config", config_section_info);
//test		System.out.println(config_info_pool.get("config").toString());
	}
	
	private void prepareData(){
		//Initialize the containers, accept the data, set the parameters.
		ArrayList<String[]> original_config_data = FileUtil.loadFile("config", "config", config_splitor);	
		Pattern annotation = Pattern.compile("#");
		Pattern blank_line = Pattern.compile("^\n*$");
		//Prepare the lines.
		for(int i =0; i<original_config_data.size();i++){
			String[] original_line = original_config_data.get(i);

			//	See if the line is an annotation.
			Matcher is_annotation = annotation.matcher(original_line[0]);
			Matcher is_blank_line = blank_line.matcher(original_line[0]);
			//test
			//System.out.println(i+ "##" + is_annotation.find()+ "##" + is_blank_line.lookingAt());
			//System.out.print(i+" ##");
			//for(String s: original_line){System.out.print(s+ " ");}
			//System.out.print("\n");
			try{
				if ((!(is_annotation.find()))&&(!(is_blank_line.lookingAt()))){
					String[] prepared_line = modifyLine(generateLine(i, original_line));
					config_data.add(prepared_line);
				}
			}catch (Exception informatedLine) {
				System.err.println("InformatedLine");
				System.out.print((i+1) +" ##");
				for(String s: original_line){System.out.print(s+ " ");}
				System.out.print("\n");
				informatedLine.printStackTrace();
			}
		}
	}
	
	private void prepareOnion(){
		//Peel the onion.(i)
		for(int i=0; i<max_indentation_num+1; i++){	
			ArrayList<String[]> current_layer = new ArrayList<String[]>();
			//Set the layer.(j)
			for(int j=0; j<config_data.size(); j++){	
				if(config_data.get(j)[1].equals(i + "")){
					current_layer.add(config_data.get(j));
				}
			}
			onion.put(i+"", current_layer);
		}
//		test
//		for(int i=0; i<onion.get("3").size(); i++){
//			String[] str = onion.get("3").get(i);
//			for(String s:str){System.out.print("=" + s + "\t");}
//			System.out.println("\n");
//		}
	}
	
	private void prepareConfig(){
		//Initializ container.
		ArrayList<String[]> section_head_pool = onion.get("0");
		ArrayList<String[]> sub_section_head_pool = onion.get("2");	
		//Prepare section_head-end pairs, i.e. section_borders& sub_section_borders;
		ArrayList<String[]>section_border_pool = generateBorders(section_head_pool);
		ArrayList<String[]>sub_section_border_pool = generateBorders(sub_section_head_pool);
		setTOTAL_FEATURE_NAME_POOL(section_border_pool);
		setTOTAL_FILE_NAME_POOL(onion.get("1"));
		for(int i=0; i<section_border_pool.size(); i++){
			HashMap<String, HashMap<String, ArrayList<String>>> section_info_pool = new HashMap<String, HashMap<String, ArrayList<String>>>();
			String[] section_border = section_border_pool.get(i);
			String section_name = section_border[2];
			int section_start = Integer.valueOf(section_border[0]).intValue();
			int section_end = Integer.valueOf(section_border[1]).intValue();
			//Set the meta_section_info
				HashMap<String, ArrayList<String>> meta_section_info = new HashMap<String, ArrayList<String>>();
				ArrayList<String[]> meta_section_line_pool = onion.get("1");
				for(int k1=0; k1<meta_section_line_pool.size(); k1++){
					ArrayList<String> meta_section_line_info = new ArrayList<String>();
					String[] meta_section_line = meta_section_line_pool.get(k1);
					String meta_section_line_name = meta_section_line[2];
					int meta_section_line_line_num = Integer.valueOf(meta_section_line[0]).intValue();
					if((meta_section_line_line_num>section_start)&&(meta_section_line_line_num<section_end)){
						for(int l=0; l<meta_section_line.length-3; l++){
							meta_section_line_info.add(meta_section_line[l+3]);
						}
						meta_section_info.put(meta_section_line_name, meta_section_line_info);
					}
				}
				section_info_pool.put(section_name, meta_section_info);
			//Set the section_info
			for(int j=0; j<sub_section_border_pool.size();j++){
				HashMap<String, ArrayList<String>> sub_section_info = new HashMap<String, ArrayList<String>>();
				String[] sub_section_border = sub_section_border_pool.get(j);
				String sub_section_name = sub_section_border[2];
				int sub_section_start = Integer.valueOf(sub_section_border[0]).intValue();
				int sub_section_end = Integer.valueOf(sub_section_border[1]).intValue();
				ArrayList<String[]> sub_section_line_pool = onion.get("3");
				if((sub_section_start>section_start)&&(sub_section_end<section_end)){
					for(int k=0; k<sub_section_line_pool.size(); k++){
						ArrayList<String> sub_section_line_info = new ArrayList<String>();
						String[] sub_section_line = sub_section_line_pool.get(k);
						String sub_section_line_name = sub_section_line[2];			
						int sub_section_line_line_num = Integer.valueOf(sub_section_line[0]).intValue();
						if((sub_section_line_line_num>sub_section_start)&&(sub_section_line_line_num<sub_section_end)){
							for(int l=0; l<sub_section_line.length-3; l++){
								sub_section_line_info.add(sub_section_line[l+3]);
							}
							sub_section_info.put(sub_section_line_name, sub_section_line_info);
						}
					}
					section_info_pool.put(sub_section_name, sub_section_info);
				}
			}
			CONFIG_INFO_POOL.put(section_name, section_info_pool);
		}
	}



	private void preparePathway(){
		PATHWAYS.clear();
		ArrayList<String> section_names = generateSection_name_pool();
		for(int i=0; i<section_names.size(); i++){
			HashMap<String, String> section_pathway = new HashMap<String, String>();
			String section_name = section_names.get(i);
			String folder_name = CONFIG_INFO_POOL.get(section_name).get(section_name).get("folder_name").get(0);
			ArrayList<String> file_names = CONFIG_INFO_POOL.get(section_name).get(section_name).get("file_names");
		    for(int j=0;j<file_names.size() ;j++){
		    	String file_name = file_names.get(j);
		    	String pathway = "include/" + folder_name + "/"+ file_name;
		    	section_pathway.put(file_name, pathway);
		    }
		    PATHWAYS.put(section_name, section_pathway);
		}	
	}
	
	//Methods [2]

	//Method[2]
	private String[] generateLine(int line_num ,String[] line){

		//Initialize the container, accept the data.
		String[] prepared_line = new String[line.length+2];
		prepared_line[0] = line_num + "";	//Line[0] is its "line_num", which always begins from 0.		
		String[] line_head = line[0].split("\t");
		int indentation_num = 0;
		//Check the line_head, count the  max indentation_num.
		for(int i=0; i<line_head.length; i++){
			if(line_head[i].equals("")){
				indentation_num = indentation_num + 1;
			}
			else {
				prepared_line[2] = line_head[i];	//Line[2] is its "line_head".
			}
		}
		prepared_line[1] = indentation_num+"";		//Line[1] is its "indentation level".
		for(int i=0; i<line.length-1; i++){
			prepared_line[i+3] = line[i+1];
		}
		if(indentation_num >= max_indentation_num){
			max_indentation_num = indentation_num;
		}
		return prepared_line;
	}
	
	
	private String[] modifyLine(String[] line){
		String[] modified_line = line;
		for(int i=0; i<line.length; i++){
			modified_line[i] = modified_line[i].replace("<","");
			modified_line[i] = modified_line[i].replace(">","");
		}
		return modified_line;
	}
	
	
	private ArrayList<String[]> generateBorders(ArrayList<String[]> section_head_pool){
		ArrayList<String[]>section_borders = new ArrayList<String[]>();
		String[] section_border = new String[3];
		for(int i=0; i<section_head_pool.size(); i++){
			if(!(section_head_pool.get(i)[2].equals("end"))){
				section_border[0] = section_head_pool.get(i)[0];//Set the lin_num of the start of border
				section_border[2] = section_head_pool.get(i)[2];//Set the section_head_name of the border
			}
			else{
				section_border[1] = section_head_pool.get(i)[0];//Set the lin_num of the end of border
				section_borders.add(section_border);
				section_border = new String[3];
			}
		}
		return section_borders;
	}
	
	
	private ArrayList<String> generateSection_name_pool(String section_intendation){
		ArrayList<String> section_names = new ArrayList<String>();
		for(int i=0; i<onion.get(section_intendation).size(); i++){
			section_names.add(onion.get(section_intendation).get(i)[2]);
		}
		return section_names;
	}
	
	
	private ArrayList<String> generateSection_name_pool(){
		ArrayList<String> section_names = new ArrayList<String>();
		Iterator iter = CONFIG_INFO_POOL.entrySet().iterator();
		while (iter.hasNext()){
			Map.Entry entry =(Map.Entry) iter.next();
			section_names.add((String) entry.getKey());
		}
		return section_names;
	}
	
	//Method[G&S]
	public static HashMap<String, HashMap<String, ArrayList<String>>> getCONFIG_INFO(String feature_name) {
		return CONFIG_INFO_POOL.get(feature_name);
	}
	private void setTOTAL_FILE_NAME_POOL(ArrayList<String[]> section_info_pool) {
		for(int i=0; i<section_info_pool.size(); i++){
			if(section_info_pool.get(i)[2].equalsIgnoreCase("file_names")){
				for(int j=3; j<section_info_pool.get(i).length; j++){
					TOTAL_FILE_NAME_POOL.add(section_info_pool.get(i)[j]);
				}
			}
		}
		/*/test
		/for(int i=0; i<TOTAL_FILE_NAME_POOL.size();i++){
			System.out.println(TOTAL_FILE_NAME_POOL.get(i));
		}*/
	}
	private void setTOTAL_FEATURE_NAME_POOL(ArrayList<String[]> section_border_pool) {
		for(int i=0; i<section_border_pool.size(); i++){
			String current_feature_name = section_border_pool.get(i)[2];
			TOTAL_FEATURE_NAME_POOL.add(current_feature_name);
		}
		/*test
		for(int i=0; i<TOTAL_FEATURE_NAME_POOL.size();i++){
			System.out.println(TOTAL_FEATURE_NAME_POOL.get(i));
		}*/
	}
	public static ArrayList<String> getTOTAL_FEATURE_NAME_POOL() {
		return TOTAL_FEATURE_NAME_POOL;
	}
	public static ArrayList<String> getTOTAL_FILE_NAME_POOL() {
		return TOTAL_FILE_NAME_POOL;
	}
	public static HashMap<String, String> getPATHWAY(String feature_name) {
		return PATHWAYS.get(feature_name);
	}
	//Method[test]
	
	//Methods [test]
	public void testPathways(){
		System.out.println("\t>Testing Pathways");
		ArrayList<String> section_names = generateSection_name_pool();
		for(int i=0; i<section_names.size(); i++){
			String section_name = section_names.get(i);
			HashMap<String, String> section_pathway = PATHWAYS.get(section_name);
			System.out.println("\t\t===" + section_name + " Pathways ===");
			System.out.println("\t\t\tfile_name\tpathway");
			Iterator iter = section_pathway.entrySet().iterator();
			while (iter.hasNext()){
				Map.Entry entry =(Map.Entry) iter.next();
				System.out.println("\t\t\t" + entry.getKey().toString() + "\t" + entry.getValue().toString());
			}
		}
		System.out.println("\tPathways Testing Finished\n");
	}
}

