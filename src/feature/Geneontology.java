package feature;

import java.util.*;

import tools.Probe;
import main.Main;

/**
 * @class Geneontology This class is designed to complete the process
 * of the calculation of the value of Geneontology feature. 
 * @
 * @author hyao
 *
 */
public class Geneontology extends Feature {

	@Override
	protected void tableOperation(String currentTableName) {
		// TODO Auto-generated method stub
		//System.out.println("## " + currentTableName + " ##");
		if(currentTableName.equalsIgnoreCase("table_gene_association")){//	refer
			ArrayList<String[]> tableTerm = table_pool.get("table_term").getTable_data().get("term");
			//System.out.println(table_pool.get("table_term").getTable_data().get("term"));
			prepareAssociation(state.getTable_info("table_gene_association").get("table_goAcc_num").get(0), currentTableName, tableTerm);
		}
		else if(currentTableName.equalsIgnoreCase("table_term2term")){	//	iterate and add line
			prepareTerm2Term(currentTableName);
		}
		else if((currentTableName.equalsIgnoreCase("table_term_id_pool_fun"))
		||(currentTableName.equalsIgnoreCase("table_term_id_pool_pro"))
		||(currentTableName.equalsIgnoreCase("table_term_id_pool_com"))){	//	add line
			ArrayList<String[]> tableTerm2Term = table_pool.get("table_term2term").getTable_data().get("term2term");
			prepareTermIdPool(state.getTable_info("table_gene_association").get("table_goAcc_num").get(0), currentTableName, tableTerm2Term);
		}
		else if(
		(currentTableName.equalsIgnoreCase("table_gene_profile_fun"))
		||(currentTableName.equalsIgnoreCase("table_gene_profile_pro"))
		||(currentTableName.equalsIgnoreCase("table_gene_profile_com"))){	//	add column
			ArrayList<String[]> geneProductCount = table_pool.get("table_gene_product_count").getTable_data().get("gene_product_count");
			//Probe.probeMap("table_gene_product_count", table_pool.get("table_gene_product_count").getTable_data(), false);
			prepareGeneProfile(state.getTable_info("table_gene_association").get("table_goAcc_num").get(0), currentTableName, geneProductCount);
		}
	}

	@Override
	protected void matrixOperation(String current_matrix_name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void calculatorOperation(String current_matrix_name) {
		// TODO Auto-generated method stub
		
	}

	//Method[1]
	private void prepareAssociation(String goAccNum, String currentTableName, ArrayList<String[]> tableTerm) {
		//System.out.println("prepareAssociation");
		Integer goAccNumInteger = Integer.valueOf(goAccNum);
		HashMap<String, ArrayList<String[]>> currentTableData = table_pool.get(currentTableName).getTable_data();
		//System.out.println(tableTerm.size());
		//Probe.probeMap("tableTerm", table_pool.get("table_term").getTable_data(), false);
		Iterator currentTableDataIter = currentTableData.entrySet().iterator();
		while(currentTableDataIter.hasNext()){
			Map.Entry currentTableDataEntry = (Map.Entry) currentTableDataIter.next();
			String currentTableDataEntryName = currentTableDataEntry.getKey().toString();
			ArrayList<String[]> currentTableDataCluster = currentTableData.get(currentTableDataEntryName);
			ArrayList<String[]> newTableDataCluster = new ArrayList<String[]>();
			newTableDataCluster.addAll(currentTableDataCluster);
			for(int i=0; i<currentTableDataCluster.size(); i++){
				String[] currentLine = currentTableDataCluster.get(i);
				String currentGeneGoAcc = currentLine[goAccNumInteger];
				for(int j=0; j<tableTerm.size(); j++){
					String currentTermId = tableTerm.get(j)[0];
					String currentTermGoAcc = tableTerm.get(j)[3];
					if(currentTermGoAcc.equalsIgnoreCase(currentGeneGoAcc)){
						currentLine[goAccNumInteger] = currentTermId;
						break;
					}
					else{
						currentLine[goAccNumInteger] = "0";
					}
				}
			}
			currentTableData.put(currentTableDataEntryName, newTableDataCluster);
		}
	}
	
	private void prepareTerm2Term(String currentTableName) {
		HashMap<String, ArrayList<String[]>> currentTableData = table_pool.get(currentTableName).getTable_data();
		//Probe.probeMap("table_term2Term", currentTableData, false);
		ArrayList<String[]> initialTreeList = currentTableData.get("term2term");
		HashMap<String, ArrayList<String>> initialTreeMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String[]> iterativeTreeList = new ArrayList<String[]>();
		//Prepare initialTreeMap.
		for(int i=0; i<initialTreeList.size(); i++){
			String currentChildId = initialTreeList.get(i)[3];
			String currentParentId = initialTreeList.get(i)[2];
			if(initialTreeMap.containsKey(currentChildId)){
				initialTreeMap.get(currentChildId).add(currentParentId);
			}
			else{
				ArrayList<String> currentBrance = new ArrayList<String>();
				currentBrance.add(currentParentId);
				initialTreeMap.put(currentChildId, currentBrance);
			}
		}
		System.out.println("\t\t\t>Iterating Term2term...");
		//Iterate treeMap.
		Iterator initialTreeIter = initialTreeMap.entrySet().iterator();
		int value_block_cutter = 0;
		int id_matrix_size = initialTreeMap.size();
		while(initialTreeIter.hasNext()){
			value_block_cutter++;
			//System.out.println(value_block_cutter);
			if(value_block_cutter%(id_matrix_size/10) == 0){
				System.out.println("\t\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
			}
			Map.Entry initialTreeEntry = (Map.Entry) initialTreeIter.next();
			String currentChildId = initialTreeEntry.getKey().toString();
			ArrayList<String> currentInitialBrance = initialTreeMap.get(currentChildId);
			//System.out.println(currentInitialBrance.size());
			ArrayList<String> newBrance = new ArrayList<String>();
			//Iterator.
			newBrance = iterateParentId(newBrance, currentInitialBrance, initialTreeMap);
			for(int i=0; i<newBrance.size(); i++){
				String[] iterativeTreeListEntry = new String[5];
				iterativeTreeListEntry[3] = currentChildId;
				iterativeTreeListEntry[2] = newBrance.get(i);
				iterativeTreeList.add(iterativeTreeListEntry);
			}
		}
		currentTableData.put("term2term", iterativeTreeList);
	}

	private void prepareTermIdPool(String goAccNum, String currentTableName, ArrayList<String[]> tableTerm2Term) {
		// TODO Auto-generated method stub
		Integer goAccNumInteger = Integer.valueOf(goAccNum);
		HashMap<String, ArrayList<String[]>> currentTableData = table_pool.get(currentTableName).getTable_data();
		Iterator currentTableDataIter = currentTableData.entrySet().iterator();
		while(currentTableDataIter.hasNext()){
			Map.Entry currentTableDataEntry = (Map.Entry) currentTableDataIter.next();
			String currentTableDataEntryName = currentTableDataEntry.getKey().toString();
			ArrayList<String[]> currentTableDataCluster = currentTableData.get(currentTableDataEntryName);
			ArrayList<String[]> newTableDataCluster = new ArrayList<String[]>();
			newTableDataCluster.addAll(currentTableDataCluster);
			for(int i=0; i<currentTableDataCluster.size(); i++){
				String[] currentLine = currentTableDataCluster.get(i);
				String currentTermId = currentLine[goAccNumInteger];
				for(int j=0; j<tableTerm2Term.size(); j++){
					String currentChildTermId = tableTerm2Term.get(j)[3];
					String currentParentTermId = tableTerm2Term.get(j)[2];
					if(currentChildTermId.equalsIgnoreCase(currentTermId)){
						String[] currentNewLine = new String[currentLine.length];
						//Change null to "".
						for(int k=0; k<currentNewLine.length; k++){
							currentNewLine[k] = "";
						}
						currentNewLine[goAccNumInteger] = currentParentTermId;
						newTableDataCluster.add(currentNewLine);
					}
				}
			}
			currentTableData.put(currentTableDataEntryName, newTableDataCluster);
		}
	}
	

	private void prepareGeneProfile(String goAccNum, String currentTableName, ArrayList<String[]> geneProductCount) {
		// TODO Auto-generated method stub
		Integer goAccNumInteger = Integer.valueOf(goAccNum);
		HashMap<String, ArrayList<String[]>> currentTableData = table_pool.get(currentTableName).getTable_data();
		Iterator currentTableDataIter = currentTableData.entrySet().iterator();
		while(currentTableDataIter.hasNext()){
			Map.Entry currentTableDataEntry = (Map.Entry) currentTableDataIter.next();
			String currentTableDataEntryName = currentTableDataEntry.getKey().toString();
			ArrayList<String[]> currentTableDataCluster = currentTableData.get(currentTableDataEntryName);
			ArrayList<String[]> newTableDataCluster = new ArrayList<String[]>();
			for(int i=0; i<currentTableDataCluster.size(); i++){
				String[] currentLine = currentTableDataCluster.get(i);
				String currentTermId = currentLine[goAccNumInteger];
				String[] currentNewLine = new String[currentLine.length+1];
				//Change null to "".
				for(int k=0; k<currentNewLine.length; k++){
					currentNewLine[k] = "";
				}
				currentNewLine[goAccNumInteger] = currentLine[goAccNumInteger];
				for(int j=0; j<geneProductCount.size(); j++){
					String currentProductId = geneProductCount.get(j)[0];
					String currentProductCount = geneProductCount.get(j)[1];
					if(currentProductId.equalsIgnoreCase(currentTermId)){
						currentNewLine[currentNewLine.length-1] = currentProductCount;
						break;
					}
					else {
						currentNewLine[currentNewLine.length-1] = 30322 + "";
					}
				}
				newTableDataCluster.add(currentNewLine);
			}
			currentTableData.put(currentTableDataEntryName, newTableDataCluster);
		}
	}
	
	//Method[2]
	private ArrayList<String> iterateParentId(ArrayList<String> newBrance, ArrayList<String> initialBrance, HashMap<String, ArrayList<String>> initialTreeMap) {
		for(int i =0; i<initialBrance.size(); i++){
			String currentChildId = initialBrance.get(i);
			if(initialTreeMap.containsKey(currentChildId)){
				ArrayList<String> currentInitialBrance = initialTreeMap.get(currentChildId);
				for(int j=0; j<initialBrance.size(); j++){
					String currentParentId = initialBrance.get(j); 
					if (!(newBrance.contains(currentParentId))) {
						newBrance.add(currentParentId);
					}
				}
				iterateParentId(newBrance, currentInitialBrance, initialTreeMap);
			}
		}
		return newBrance;
	}
	
}













