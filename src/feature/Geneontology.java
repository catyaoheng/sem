package feature;

import java.util.*;

import tools.Probe;
import util.FileUtil;
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
		//*/
		else if(currentTableName.equalsIgnoreCase("table_term2term")){	//	iterate and add line
			prepareTerm2Term(currentTableName);
		}
		else if(currentTableName.equalsIgnoreCase("table_gene_product_count")){	//	iterate the gene_product_count
			ArrayList<String[]> tableTerm2TermData = table_pool.get("table_term2term").getTable_data().get("term2term");
			ArrayList<String[]> geneProductCountData = table_pool.get("table_gene_product_count").getTable_data().get("gene_product_count");
			HashMap<String, ArrayList<String[]>> geneAssociationData = table_pool.get("table_gene_association").getTable_data();
			boolean isPrepared = false;
			for(String s: geneProductCountData.get(0)){
				if(s.equalsIgnoreCase("GeneProductCount_Iterated")){
					isPrepared = true;
				}
			}
			if(isPrepared){
				geneProductCountData.remove(0);
			}
			else{
				Integer table_goAcc_num = Integer.valueOf(state.getTable_info("table_gene_association").get("table_goAcc_num").get(0)).intValue();
				prepareGeneProductCount(table_goAcc_num, currentTableName, geneAssociationData, tableTerm2TermData);
			}
		}
		//*/
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
		boolean isPrepared = false;
		for(String s: currentTableData.get("term2term").get(0)){
			if(s.equalsIgnoreCase("Term2Term_Iterated")){
				isPrepared = true;
			}
		}
		if(isPrepared){
			currentTableData.get("term2term").remove(0);
			return;
		}
		else{
		//Probe.probeMap("table_term2Term", currentTableData, false);
		ArrayList<String[]> initialTreeList = currentTableData.get("term2term");
		//System.out.println(initialTreeList.size());
		HashMap<String, ArrayList<String>> initialTreeMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String[]> iterativeTreeList = new ArrayList<String[]>();
		int strsLength = initialTreeList.get(0).length;
		String[] markStr = new String[strsLength];
		for(int i=0; i<strsLength; i++){
			markStr[i] = "Term2Term_Iterated";
		}
		iterativeTreeList.add(markStr);
		
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
			//*/
			if(id_matrix_size>=10){
				if(value_block_cutter%(id_matrix_size/10) == 0){
					System.out.println("\t\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
				}
			}
			//*/
			Map.Entry initialTreeEntry = (Map.Entry) initialTreeIter.next();
			String currentChildId = initialTreeEntry.getKey().toString();
			ArrayList<String> currentInitialBrance = initialTreeMap.get(currentChildId);
			//System.out.println(currentInitialBrance.size());
			ArrayList<String> newBrance = new ArrayList<String>();
			//Iterator.
			newBrance = iterateParentId(newBrance, currentInitialBrance, initialTreeMap);
			newBrance.addAll(currentInitialBrance);
			for(int i=0; i<newBrance.size(); i++){
				String[] iterativeTreeListEntry = new String[5];
				iterativeTreeListEntry[3] = currentChildId;
				iterativeTreeListEntry[2] = newBrance.get(i);
				iterativeTreeList.add(iterativeTreeListEntry);
			}
		}
		//add all self belongs node
		ArrayList<String> selfBelongsList = new ArrayList<String>();
		for(int i=0; i< initialTreeList.size(); i++){
			String currentChildId = initialTreeList.get(i)[3];
			String currentParentId = initialTreeList.get(i)[2];
			if (!selfBelongsList.contains(currentChildId)){
				selfBelongsList.add(currentChildId);
			}
			if (!selfBelongsList.contains(currentParentId)){
				selfBelongsList.add(currentParentId);
			}
		}
		for(int i=0; i<selfBelongsList.size(); i++){
			String currentId = selfBelongsList.get(i);
			String[] outStrs = new String[5];
			outStrs[3] = currentId;
			outStrs[2] = currentId;
			iterativeTreeList.add(outStrs);
		}
		
		//System.out.println("%%%%%%" + table_pool.get("table_term2term").getTable_data().get("term2term").size());
		FileUtil.writeFile("geneontology", "term2term", iterativeTreeList, false, true);
		iterativeTreeList.remove(0);
		currentTableData.put("term2term", iterativeTreeList);
		//System.out.println("$$$$$$" + iterativeTreeList.size());
		//System.out.println("#######" + table_pool.get("table_term2term").getTable_data().get("term2term").size());
		}
	}

	private void prepareGeneProductCount(Integer table_goAcc_num, String currentTableName,
			HashMap<String, ArrayList<String[]>> geneAssociationData,
			ArrayList<String[]> tableTerm2TermData) {
		//[0] initiating container of newGeneProductCountData
		ArrayList<String[]> newGeneProductCountData = new ArrayList<String[]>();
		ArrayList<String[]> geneProductCountData = table_pool.get("table_gene_product_count").getTable_data().get("gene_product_count");
		int strsLength = geneProductCountData.get(0).length;
		String[] markStr = new String[strsLength];
		for(int i=0; i<strsLength; i++){
			markStr[i] = "GeneProductCount_Iterated";
		}
		newGeneProductCountData.add(markStr);
		
		//[1] preparing termParent2ChildMap, iD_str tansform to int, mapped by parentID
		//this map contains all parentId/their IterateChildId(Including the parentId itself)
		//CONTAINER
		HashMap<String, ArrayList<Integer>> termParent2ChildMap = new HashMap<String, ArrayList<Integer>>();
		//CONTAINER#
		int value_block_cutter = 0;
		int id_matrix_size = tableTerm2TermData.size();
		System.out.println("\t\t\t\t>preparing termParent2ChildMap");
		for(int i=0; i<tableTerm2TermData.size(); i++){
			//*/
			if(id_matrix_size>=10){
				if(value_block_cutter%(id_matrix_size/10) == 0){
					System.out.println("\t\t\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
				}
			}
			//*/
			String currentParentTermId = tableTerm2TermData.get(i)[2];
			Integer currentChildTermId = Integer.valueOf(tableTerm2TermData.get(i)[3]).intValue();
			//System.out.println(currentParentTermId + "\t" + currentChildTermId);
			if (termParent2ChildMap.containsKey(currentParentTermId)){
				termParent2ChildMap.get(currentParentTermId).add(currentChildTermId);
			}
			else{
				ArrayList<Integer> currentChildList = new ArrayList<Integer>();
				termParent2ChildMap.put(currentParentTermId, currentChildList);
				termParent2ChildMap.get(currentParentTermId).add(currentChildTermId);
			}
		}
				
		//[2] preparing gene2GoAccMap, iD_str tansform to int, mapped by GoAcc
		//CONTAINER
		value_block_cutter = 0;
		id_matrix_size = geneAssociationData.size();
		System.out.println("\t\t\t\t>preparing gene2GoAccMap");
		HashMap<Integer, ArrayList<Integer>> goAcc2GeneMap = new HashMap<Integer, ArrayList<Integer>>();
		//CONTAINER#
		Iterator geneAssociationDataIterator = geneAssociationData.entrySet().iterator();
		while(geneAssociationDataIterator.hasNext()){
			//*/
			if(id_matrix_size>=10){
				if(value_block_cutter%(id_matrix_size/10) == 0){
					System.out.println("\t\t\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
				}
			}
			//*/		
			Map.Entry geneAssociationDataEntry = (Map.Entry) geneAssociationDataIterator.next();
			String currentGeneIdStr = geneAssociationDataEntry.getKey().toString();
			ArrayList<String[]> currentAssociationStrList = geneAssociationData.get(currentGeneIdStr);
			for(int i=0; i<currentAssociationStrList.size(); i++){
				Integer currentGeneId = Integer.valueOf(currentGeneIdStr).intValue();
				Integer currentGoAccId = Integer.valueOf(currentAssociationStrList.get(i)[table_goAcc_num]).intValue();
				if(goAcc2GeneMap.containsKey(currentGoAccId)){
					goAcc2GeneMap.get(currentGoAccId).add(currentGeneId);
				}
				else{
					ArrayList<Integer> currentGeneIdList = new ArrayList<Integer>();
					goAcc2GeneMap.put(currentGoAccId, currentGeneIdList);
					goAcc2GeneMap.get(currentGoAccId).add(currentGeneId);
				}
			}
		}
		
		System.out.println(goAcc2GeneMap.size());
		/*/test
		Iterator termParent2ChildMapIter1 = goAcc2GeneMap.entrySet().iterator();
		while(termParent2ChildMapIter1.hasNext()){
			Map.Entry currentTermParent2ChildMapEntry = (Map.Entry)termParent2ChildMapIter1.next();
			String currentParentId = currentTermParent2ChildMapEntry.getKey().toString();
			System.out.println(currentParentId);
			ArrayList<Integer> currentChildIdList = goAcc2GeneMap.get(Integer.valueOf(currentParentId));
			for(Integer i: currentChildIdList){
				System.out.println("\t" + i);
			}
		}
		//*/
		
		//*/
		//[3] preparing a map whose keySet contains every goTerm and
		//value id the unionSet of the geneID associated to every chileTerm(including iterated childTerm and itsself) belongs to the key goTerm
		value_block_cutter = 0;
		id_matrix_size = geneAssociationData.size();
		System.out.println("\t\t\t\t>preparing goAcc2IteratedGeneMap");
		
		//CONTAINER
		HashMap<String, ArrayList<Integer>> goAcc2IteratedGeneMap = new HashMap<String, ArrayList<Integer>>();
		//CONTAINER#
		Iterator termParent2ChildMapIter = termParent2ChildMap.entrySet().iterator();
		while(termParent2ChildMapIter.hasNext()){
			//*/
			if(id_matrix_size>=10){
				if(value_block_cutter%(id_matrix_size/10) == 0){
					System.out.println("\t\t\t\t" + String.format("%.0f", (value_block_cutter*1.0/id_matrix_size*1.0)*100.0) + "% ..." + new Date());
				}
			}
			//*/
			Map.Entry currentTermParent2ChildMapEntry = (Map.Entry)termParent2ChildMapIter.next();
			String currentParentId = currentTermParent2ChildMapEntry.getKey().toString();
			//TEMP CONTAINER
			ArrayList<Integer> currentIteratedGeneList = new ArrayList<Integer>();
			//TEMP CONTAINER#
			ArrayList<Integer> currentChildIdList = termParent2ChildMap.get(currentParentId);
			for(int i=0; i<currentChildIdList.size(); i++){
				Integer currentChildId = currentChildIdList.get(i);
				//get the currentGeneIdList whose geneIds are associated to currentChildId
				ArrayList<Integer> currentGeneIdList = new ArrayList<Integer>();
				if(goAcc2GeneMap.containsKey(currentChildId)){
					currentGeneIdList = goAcc2GeneMap.get(currentChildId);
				}
				for(int j=0; j<currentGeneIdList.size(); j++){
					Integer currentGeneId = currentGeneIdList.get(j);
					if(!currentIteratedGeneList.contains(currentGeneId)){
						currentIteratedGeneList.add(currentGeneId);
					}
				}
			}
			goAcc2IteratedGeneMap.put(currentParentId, currentIteratedGeneList);
		}
		//*/
		
		//[4] put the data back and write
		Iterator goAcc2IteratedGeneMapIter = goAcc2IteratedGeneMap.entrySet().iterator();
		while(goAcc2IteratedGeneMapIter.hasNext()){
			Map.Entry goAcc2IteratedGeneMapEntry = (Map.Entry)goAcc2IteratedGeneMapIter.next();
			String currentGoAcc = goAcc2IteratedGeneMapEntry.getKey().toString();
			String currentGeneProductCount = goAcc2IteratedGeneMap.get(currentGoAcc).size() + "";
			String[] outStrs = new String[2];
			outStrs[0] = currentGoAcc;
			outStrs[1] = currentGeneProductCount;
			newGeneProductCountData.add(outStrs);
		}
		FileUtil.writeFile("geneontology", "gene_product_count", newGeneProductCountData, false, true);
		newGeneProductCountData.remove(0);
		table_pool.get("table_gene_product_count").getTable_data().put("gene_product_count", newGeneProductCountData);
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
	private ArrayList<String> iterateParentId(ArrayList<String> newBrance, 
			ArrayList<String> initialBrance, 
			HashMap<String, ArrayList<String>> initialTreeMap) {
		for(int i =0; i<initialBrance.size(); i++){
			String currentChildId = initialBrance.get(i);
			if(initialTreeMap.containsKey(currentChildId)){
				ArrayList<String> currentInitialBrance = initialTreeMap.get(currentChildId);
				for(int j=0; j<currentInitialBrance.size(); j++){
					String currentParentId = currentInitialBrance.get(j); 
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



/*//
		//this map's childTerm sought to contain the parentTerm itself, so we need to add it to
		//this map does NOT contain leaf, so we need to add leaf to it, the leaf's childTerm is itself
		for(int i=0; i<tableTerm2TermData.size(); i++){
			String currentId = tableTerm2TermData.get(i)[3];
			if(termParent2ChildMap.containsKey(currentId)){
				termParent2ChildMap.get(currentId).add(Integer.valueOf(currentId).intValue());
			}
			else{
				ArrayList<Integer> currentChildList = new ArrayList<Integer>();
				termParent2ChildMap.put(currentId, currentChildList);
				termParent2ChildMap.get(currentId).add(Integer.valueOf(currentId).intValue());
			}
		} 
//*/









