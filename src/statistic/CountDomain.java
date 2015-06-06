package statistic;

import java.util.ArrayList;
import java.util.HashMap;

import tools.Probe;


public class CountDomain extends Calculator {

	private HashMap<String,int[]> domainInteractionMap;
	
	@Override
	protected void operateDataRefer() {
		domainInteractionMap = new HashMap<String, int[]>();
		ArrayList<String[]> domainInteractionList = calculator_data_refer_pool.get("matrix_domain_interaction").get("domain_interaction");
		for(int i=0; i<domainInteractionList.size(); i++){
			String currentDomainPair = domainInteractionList.get(i)[1];
			int[] domainCount = new int[15];
			for(int j=2; j<17; j++){
				domainCount[j-2] = Integer.valueOf(domainInteractionList.get(i)[j]);
			}
			domainInteractionMap.put(currentDomainPair, domainCount);
		}
	}
	
	@Override
	protected Object transformDataFormat(ArrayList<String[]> cluster) {
		// TODO Auto-generated method stub
		return cluster;
	}

	@Override
	protected ArrayList<String[]> calculateDotProduct(String[] currentClusterName,
			Object vectorCluster_0, Object vectorCluster_1) {
		// TODO Auto-generated method stub
		ArrayList<String[]> dotProduct = new ArrayList<String[]>();
		ArrayList<String[]> vectorDomainCluster_0 = (ArrayList<String[]>)vectorCluster_0;
		ArrayList<String[]> vectorDomainCluster_1 = (ArrayList<String[]>)vectorCluster_1;
		String[] blankCount = new String[17];
		blankCount[0] = currentClusterName[0];
		blankCount[1] = currentClusterName[1];
		for(int i=2; i<17; i++){
			blankCount[i] = "0";
		}
		//System.out.println("Cluster_0");
		//Probe.probeStringArray(vectorDomainCluster_0);
		//System.out.println("Cluster_1");
		//Probe.probeStringArray(vectorDomainCluster_1);
		ArrayList<String[]> domainInteractionList = calculator_data_refer_pool.get("matrix_domain_interaction").get("domain_interaction");
		for(int i=0; i<vectorDomainCluster_0.size(); i++) {
			String currentPfamId_0 = vectorDomainCluster_0.get(i)[0];
			for(int j=0; j<vectorDomainCluster_1.size(); j++) {
				String currentPfamId_1 = vectorDomainCluster_1.get(j)[0];
				String currentPfamIdPair_0 = currentPfamId_0 + currentPfamId_1;
				String currentPfamIdPair_1 = currentPfamId_1 + currentPfamId_0;
				if(domainInteractionMap.containsKey(currentPfamIdPair_0)){
					//System.out.print("Before\t#####\t");
					//Probe.probeStrings(blankCount);
					//Probe.probeStrings(domainInteractionList.get(k));
					for(int l=2; l<17; l++){
						blankCount[l] = (Integer.valueOf(blankCount[l]) + domainInteractionMap.get(currentPfamIdPair_0)[l-2])+ "";
					}
					//System.out.print("After\t#####\t ");
					//Probe.probeStrings(blankCount);
				}
				else if(domainInteractionMap.containsKey(currentPfamIdPair_1)){
					for(int l=2; l<17; l++){
						blankCount[l] = (Integer.valueOf(blankCount[l]) + domainInteractionMap.get(currentPfamIdPair_1)[l-2])+ "";
					}
				}
			}
		}
		//System.out.print("Filnal\t#####\t ");
		//Probe.probeStrings(blankCount);
		dotProduct.add(blankCount);
		//Probe.probeStringArray(dotProduct);
		return dotProduct;
	}

	@Override
	protected String[] mergeValue(ArrayList<String[]> dotProduct) {
		String[] mergedValueStr = dotProduct.get(0);
		return mergedValueStr;
	}



}

