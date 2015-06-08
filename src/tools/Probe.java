package tools;

import java.util.*;
import java.util.*;

public class Probe{
	
//Method[0]	
	public static void probeMap(String mapName, HashMap<String, ArrayList<String[]>> mapNeedTest, boolean isPrintEmpty){
		System.out.println("\n== " + mapName + " ==");
		Iterator mapNeedTestIterator = mapNeedTest.entrySet().iterator();
		while(mapNeedTestIterator.hasNext()){
			Map.Entry mapNeedTestEntry = (Map.Entry)mapNeedTestIterator.next();
			String currentEntryName = mapNeedTestEntry.getKey().toString();
			ArrayList<String[]> currentCluster = mapNeedTest.get(currentEntryName);
			printMap(currentEntryName, currentCluster, isPrintEmpty);
		}
	}
	
	public static void probeMapName(HashMap mapNeedTest){
		Iterator mapNeedTestIterator = mapNeedTest.entrySet().iterator();
		while(mapNeedTestIterator.hasNext()){
			Map.Entry mapNeedTestEntry = (Map.Entry)mapNeedTestIterator.next();
			String currentEntryName = mapNeedTestEntry.getKey().toString();
			System.out.println(currentEntryName + " #Probe#");
		}
	}
	
	public static void probeStringArray(ArrayList<String[]> stringArray){
		System.out.println("\n== " + stringArray.getClass().getName() + " ==");
		for(int i=0; i<stringArray.size(); i++){
			probeStrings(stringArray.get(i));
		}
	}
	
	public static void probeStrings(String[] strings){
		for(String s:strings){System.out.print(s + "\t");}
		System.out.println();
	}
	
//Method[1]
	private static void printMap(String currentEntryName, ArrayList<String[]> currentCluster, boolean isPrintEmpty) {
		// TODO Auto-generated method stub		
		if(isPrintEmpty == true){
			System.out.println(currentEntryName + " #Probe#");
			for (int i = 0; i < currentCluster.size(); i++) {
				String[] currentStrs = currentCluster.get(i);
				System.out.print(currentEntryName + "\t");
				for(String s:currentStrs){System.out.print(s + "\t");}
				System.out.print("\n");
			}
		}
		else {
			if((!currentCluster.isEmpty())){
				System.out.println(currentEntryName + " #Probe#");
				for (int i = 0; i < currentCluster.size(); i++) {
					String[] currentStrs = currentCluster.get(i);
					System.out.print(currentEntryName + "\t");
					for(String s:currentStrs){System.out.print(s + "\t");}
					System.out.print("\n");
				}
			}
		}
	}
}