package statistic;

import java.util.ArrayList;
import java.util.HashMap;

public class MinimumCount extends Calculator {

	@Override
	protected Object transformDataFormat(ArrayList<String[]> cluster) {
		ArrayList<int[]> transformedVectorCluster = new ArrayList<int[]>();
		for(int i=0; i<cluster.size(); i++){
			String[] currentVector = cluster.get(i);
			int[] transformedVector = new int[currentVector.length];
			for(int j=0; j<currentVector.length; j++){
				transformedVector[j] = Integer.valueOf(currentVector[j]).intValue();
			}
			transformedVectorCluster.add(transformedVector);
		}
		return transformedVectorCluster;
	}

	@Override
	protected ArrayList<String[]> calculateDotProduct(String[] currentClusterName,
			Object vectorCluster_0, Object vectorCluster_1) {
		ArrayList<String[]> dotProduct = new ArrayList<String[]>();
		ArrayList<int[]> vectorClusterInt_0 = (ArrayList<int[]>)vectorCluster_0;
		ArrayList<int[]> vectorClusterInt_1 = (ArrayList<int[]>)vectorCluster_1;
		ArrayList<int[]> sharedProfileList = new ArrayList<int[]>();
		for(int i=0; i<vectorClusterInt_0.size(); i++) {
			int curretTermId_0 = vectorClusterInt_0.get(i)[0];
			for(int j=0; j<vectorClusterInt_1.size(); j++) {
				int currentTermId_1 = vectorClusterInt_1.get(j)[0];
				if(curretTermId_0 == currentTermId_1){
					sharedProfileList.add(vectorClusterInt_0.get(i));
				}
			}
		}
		String defaultValue = "NA";
		if(sharedProfileList.size()!=0){
			int count = 30322;
			for(int i=0; i<sharedProfileList.size(); i++){
				int[] currentProfile = sharedProfileList.get(i);
				int currentCount = currentProfile[1];
				if(currentCount<=count){
					count = currentCount;
				}
			}
			defaultValue = count + "";
		}
		String[] currentValueStrs = new String[3];
		currentValueStrs[0] = currentClusterName[0];
		currentValueStrs[1] = currentClusterName[1];
		currentValueStrs[2] = defaultValue;
		dotProduct.add(currentValueStrs);
		return dotProduct;
	}

	@Override
	protected String[] mergeValue(ArrayList<String[]> dotProduct) {
		String[] mergedValueStr = dotProduct.get(0);
		return mergedValueStr;
	}

	@Override
	protected void operateDataRefer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void prepareCalculatorDataRefpool() {
		// TODO Auto-generated method stub
		
	}

}
