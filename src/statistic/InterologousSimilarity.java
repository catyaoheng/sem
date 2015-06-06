package statistic;
import java.text.Format;
import java.util.*;
import tools.Idmap;

public class InterologousSimilarity extends Calculator {

	@Override
	protected Object transformDataFormat(ArrayList<String[]> cluster){
		return cluster;
	}
	
	@Override
	//pearsonsDotProductAbs
	protected ArrayList<String[]> calculateDotProduct(String[] currentClusterName, Object vector_cluster_0, Object vector_cluster_1){
		ArrayList<String[]> pearsonsDotproduct = new ArrayList<String[]>();
		ArrayList<String[]> vector_cluster_double_0 = (ArrayList<String[]>)vector_cluster_0;
		ArrayList<String[]> vector_cluster_double_1 = (ArrayList<String[]>)vector_cluster_1;
		for (int i=0; i<vector_cluster_double_0.size(); i++) {
			String[] current_vector_0 = vector_cluster_double_0.get(i);
			for (int j=0; j<vector_cluster_double_1.size(); j++) {
				String[] current_vector_1 = vector_cluster_double_1.get(j);
//				for(double s:current_vector_0){System.out.println(s);}
//				for(double s:current_vector_1){System.out.println(s);}
				double current_value = calculateSubLocSimilarity(current_vector_0, current_vector_1);
				///*
				if(calculator_info.get("is_Abs").equals("1")){
					if(current_value<0.0){
						current_value = current_value*(-1.0);
					}
				}
				//*/
				String[] currentValueStrs = new String[3];
				currentValueStrs[0] = currentClusterName[0];
				currentValueStrs[1] = currentClusterName[1];
				currentValueStrs[2] = current_value + "";
				pearsonsDotproduct.add(currentValueStrs);
			}
		}
		return pearsonsDotproduct;
	}
	

	@Override
	protected String[] mergeValue(ArrayList<String[]> valueCluster) {
		Double mergedValue = 0.0;
		String[] mergedValueStr = valueCluster.get(0);
		for(int i=0; i<valueCluster.size(); i++){
			String currentValueStr = valueCluster.get(i)[2];
			Double currentValue = Double.valueOf(currentValueStr).doubleValue();
			if(currentValue > mergedValue){
				mergedValue = currentValue;
			}
		}
		mergedValueStr[2] = mergedValue+"";
		return mergedValueStr;
	}

	@Override
	protected void operateDataRefer() {
		// TODO Auto-generated method stub
	}
    
	//[Method_1]
	public int calculateSubLocSimilarity(String[] locationsA,String[] locationsB){
		int co = 0;
		for(int i=0;i<locationsA.length;i++){
			String a = locationsA[i];
			for(int j=0;j<locationsB.length;j++){
				String b = locationsB[j];
				if(a.equals(b)){
					co = 1;
				}
			}
		}
		return co;
	}
}
