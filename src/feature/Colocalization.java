package feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tools.Probe;

public class Colocalization extends Feature {
	@Override
	protected void tableOperation(String currentTableName) {
		/*/
		if(currentTableName.equalsIgnoreCase("table_arathSubaList")){//	refer
			HashMap<String, ArrayList<String[]>> suba = table_pool.get("table_arathSubaProtein").getTable_data();
			//System.out.println(table_pool.get("table_term").getTable_data().get("term"));
			prepareSubaList(currentTableName, suba);
		}
		//*/
	}

	@Override
	protected void matrixOperation(String current_matrix_name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void calculatorOperation(String current_matrix_name) {
		// TODO Auto-generated method stub
		
	}

	//[Methods_1]
	/*/
	private void prepareSubaList(String currentTableName,
			HashMap<String, ArrayList<String[]>> suba) {
		HashMap<String, ArrayList<String[]>> currentRef = suba;
		HashMap<String, ArrayList<String[]>> suba2Protein = new HashMap<String, ArrayList<String[]>>();
		//Generate the SubaNameList
		ArrayList<String> subaNameList = new ArrayList<String>();
		Iterator currentRefIter = currentRef.entrySet().iterator();
		while(currentRefIter.hasNext()){
			Map.Entry currentRefEntry = (Map.Entry) currentRefIter.next();
			String currentRefEntryName = currentRefEntry.getKey().toString();
			ArrayList<String[]> currentGeneSubaList = currentRef.get(currentRefEntryName);
			for(int i=0; i<currentGeneSubaList.size(); i++){
				String[] currentGeneSubaNameStrs = currentGeneSubaList.get(i);
				for(int j=3; j<currentGeneSubaNameStrs.length; j++){
					String currentGeneSubaName = currentGeneSubaNameStrs[j];
					if(!subaNameList.contains(currentGeneSubaName)){
						subaNameList.add(currentGeneSubaName);
					}
				}
			}
		}
		//Prepare the SubaName2ProteinList
		HashMap<String, ArrayList<String>> subaProteinMap = new HashMap<String, ArrayList<String>>();
		for(int i=0; i<subaNameList.size(); i++){
			ArrayList<String> currentArrayList = new ArrayList<String>();
			subaProteinMap.put(subaNameList.get(i), currentArrayList);
		}
		//Fill the SubaName2ProteinList
		for(int l=0; l<subaNameList.size(); l++){
			String currentSubaName = subaNameList.get(l);
			currentRefIter = currentRef.entrySet().iterator();
			while(currentRefIter.hasNext()){
				Map.Entry currentRefEntry = (Map.Entry) currentRefIter.next();
				String currentRefEntryName = currentRefEntry.getKey().toString();
				ArrayList<String[]> currentGeneSubaList = currentRef.get(currentRefEntryName);
				for(int i=0; i<currentGeneSubaList.size(); i++){
					String[] currentGeneSubaNameStrs = currentGeneSubaList.get(i);
					for(int j=3; j<currentGeneSubaNameStrs.length; j++){
						String currentGeneSubaName = currentGeneSubaNameStrs[j];
						if((currentSubaName.equalsIgnoreCase(currentGeneSubaName))&&(!currentGeneSubaName.equalsIgnoreCase(""))){
							subaProteinMap.get(currentSubaName).add(currentGeneSubaNameStrs[2]);
						}
					}
				}
			}
		}
		//rearrange
		for(int j=0; j<subaNameList.size(); j++){
			String currentSubaProteinMapEntryName = subaNameList.get(j);
			ArrayList<String> currentSubaProteinList = subaProteinMap.get(currentSubaProteinMapEntryName);
			ArrayList<String[]> currentSubaProteinStrList = new ArrayList<String[]>();
			for(int i=0; i< currentSubaProteinList.size(); i++){
				String currentProteinName = currentSubaProteinList.get(i);
				String[] currentProteinNameStr = new String[1];
				currentProteinNameStr[0] = currentProteinName;
				currentSubaProteinStrList.add(currentProteinNameStr);
			}
			suba2Protein.put(currentSubaProteinMapEntryName, currentSubaProteinStrList);
		}
		//Probe.probeMap("suba2Protein", suba2Protein, false);
		table_pool.get(currentTableName).getTable_data().putAll(suba2Protein);
	}
	//*/
}
