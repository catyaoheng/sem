package util;

import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is designed to contain raw file.
 * The file to be load/write is thought to be divided by "/t",
 * @author hyao
 */

public class FileUtil {
		
	public static ArrayList<String[]> loadFile(String feature_name, String file_name){
//		System.out.println(Config.getPATHWAY(feature_name).get(file_name));
//		System.out.println("###");
		File this_file = new File(Config.getPATHWAY(feature_name).get(file_name));
		BufferedReader br = null;
		ArrayList<String[]> file_data = new ArrayList<String[]>();
		try {
			br = new BufferedReader(new FileReader(this_file));
			String line = null;
			while ((line = br.readLine()) != null) {
			file_data.add(line.split("\t"));
			}
			br.close();
		} catch (Exception e) {
			System.err.println("Cann't open file" + this_file.getAbsolutePath());
			e.printStackTrace();
		}
		return file_data;
	}
	
	/**
	 * Overload of method loadFiles
	 * @param file_name
	 * @param splitor
	 * @return
	 */
	public static ArrayList<String[]> loadFile(String feature_name, String file_name, String[] splitor){
		File this_file = new File(Config.getPATHWAY(feature_name).get(file_name));
		BufferedReader br = null;
		ArrayList<String[]> file_data = new ArrayList<String[]>();
		try {
			br = new BufferedReader(new FileReader(this_file));
			String line = null;
			while ((line = br.readLine()) != null) {
				for(int i=0; i<splitor.length; i++){
					line = line.replace(splitor[i], splitor[0]);
				}
			file_data.add(line.split(splitor[0]));
			}
			br.close();
		} catch (Exception e) {
			System.err.println("Cann't open file" + this_file.getAbsolutePath());
			e.printStackTrace();
		}
//		System.out.println(file_data);
		return file_data;
	}
	
	public static void writeFile(String folder_name, String file_name, ArrayList<String[]> lines, boolean is_from_end, boolean is_show_pro){
		String directory = "include/" + folder_name;
		String pathway = "include/" + folder_name + "/"+ file_name;
		File this_directory = new File(directory);
		File this_file = new File(pathway);	 
		if(!this_directory.isDirectory()){       
			this_directory.mkdirs();
		}
		if(!this_file.exists()){
	    	try {
	    		this_file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(this_file, is_from_end));
			for(int i=0; i<lines.size(); i++){
				String[] current_line = lines.get(i);
				String current_assembled_line = "";
				for(int j=0; j<current_line.length; j++){
					if(j==current_line.length-1){
						current_assembled_line = current_assembled_line + current_line[j];
					}
					else {
						current_assembled_line = current_assembled_line + current_line[j] + "\t";
					}
				}
				if(is_from_end){
					bw.write("\n" + current_assembled_line);
				}
				else if(i==0){
					bw.write(current_assembled_line);
				}
				else {
					bw.write("\n" + current_assembled_line);
				}
				//*/
				if(is_show_pro){
					if(i%(lines.size()/10) == 0){System.out.println("\t\t\tWriting..." + (i/(lines.size()/10))*10 + "% ..." + new Date());}
				}
				//*/
			}
			bw.close();
		} catch (Exception e) {
			System.err.println("Cann't write file" + this_file.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
//test	
	public static void main(String[] args){
/*		ArrayList<String[]> file_data = new ArrayList<String[]>();
		file_data = this.loadFiles()
*/		
	}
}