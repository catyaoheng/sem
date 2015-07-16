package statistic;
import java.text.Format;
import java.util.*;

import javax.xml.crypto.dsig.Transform;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import tools.Idmap;

public class PearsonsCalculator extends Calculator {

	@Override
	protected Object transformDataFormat(ArrayList<String[]> cluster){
		ArrayList<double[]> transformed_vector_cluster = new ArrayList<double[]>();
		for(int i=0; i<cluster.size(); i++){
			String[] current_vector = cluster.get(i);
			double[] transformed_vector = new double[current_vector.length];
			for(int j=0; j<current_vector.length; j++){
				transformed_vector[j] = Double.valueOf(current_vector[j]).doubleValue();
			}
			transformed_vector_cluster.add(transformed_vector);
		}
		return transformed_vector_cluster;
	}
	
	@Override
	//pearsonsDotProductAbs
	protected ArrayList<String[]> calculateDotProduct(String[] currentClusterName, Object vector_cluster_0, Object vector_cluster_1){
		ArrayList<String[]> pearsonsDotproduct = new ArrayList<String[]>();
		ArrayList<double[]> vector_cluster_double_0 = (ArrayList<double[]>)vector_cluster_0;
		ArrayList<double[]> vector_cluster_double_1 = (ArrayList<double[]>)vector_cluster_1;
		for (int i=0; i<vector_cluster_double_0.size(); i++) {
			double[] current_vector_0 = vector_cluster_double_0.get(i);
			for (int j=0; j<vector_cluster_double_1.size(); j++) {
				double[] current_vector_1 = vector_cluster_double_1.get(j);
//				for(double s:current_vector_0){System.out.println(s);}
//				for(double s:current_vector_1){System.out.println(s);}
				double current_value = new PearsonsCorrelation().correlation(current_vector_0, current_vector_1);
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
		String[] mergedValueStr = valueCluster.get(0);
		Double mergedValue = 0.0;
		int isHit = 0;
		for(int i=0; i<valueCluster.size(); i++){
			String currentValueStr = valueCluster.get(i)[2];
			Double currentValue = Double.valueOf(currentValueStr).doubleValue();
			if(currentValue >= mergedValue){
				mergedValue = currentValue;
				isHit = 1;
			}
		}
		if(isHit == 1){
			mergedValueStr[2] = mergedValue+"";
		}
		else{
			mergedValueStr[2] = "NA";
		}
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
