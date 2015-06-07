package statistic;
import java.text.Format;
import java.util.*;

import javax.xml.crypto.dsig.Transform;

import tools.Idmap;

public class MutualCalculator extends Calculator {
	private int bit = 2;

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
				double current_value = calculateMutualInformation(current_vector_0, current_vector_1, bit);
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
	
	//Method[1]
	private double calculateMutualInformation(double[] arrayA,double[] arrayB,int bit){
		double mi = 0.0;
		double[] probA = probDistribute(arrayA,bit);
		double[] probB = probDistribute(arrayB,bit);
		HashMap<String,Double> join = joinDistribute(arrayA,arrayB,bit);
		Iterator<String> iterator = join.keySet().iterator();
		double log2 = Math.log(2);
		while(iterator.hasNext()){
			String pair = iterator.next();
			String[] ab = pair.split("\t");
			int a = Integer.valueOf(ab[0]);
			int b = Integer.valueOf(ab[1]);
			double prob_a = probA[a];
			double prob_b = probB[b];
			double join_prob = join.get(pair);
			mi += join_prob*Math.log(join_prob/(prob_a*prob_b))/log2;
		}
		return mi;
	}
	
	//Method[2]
	private HashMap<String,Double> joinDistribute(double[] arrayA,double[] arrayB,int bit){
		HashMap<String,Double> dist = new HashMap<String,Double>();
		for(int i=0;i<arrayA.length;i++){
			String pair = turnCut(arrayA[i],bit)+"\t"+turnCut(arrayB[i],bit);
			if(dist.containsKey(pair)){
				double count = dist.get(pair);
				count ++;
				dist.put(pair, count);
			}else{
				double one = 1;
				dist.put(pair, one);
			}
		}
		Iterator<String> iterator = dist.keySet().iterator();
		while(iterator.hasNext()){
			String pair = iterator.next();
			double prob = dist.get(pair);
			prob = prob/arrayA.length;
			dist.put(pair, prob);
		}
		return dist;
	}
	
	private double[] probDistribute(double[] array,int bit){
		double[] dist = new double[bit];
		for(int i=0;i<array.length;i++){
			int index = turnCut(array[i],bit);
			dist[index] ++;
		}
		
		for(int i=0;i<dist.length;i++){
			dist[i] = dist[i]/array.length;
		}
		return dist;
	}
	
	//Method[3]
	private int turnCut(double value,int bit){
		int index = 0;
		if(bit == 2){
			if (value>0){
				index = 1;
			}
			else{
				index = 0;
			}
		}else if(bit == 10){
			index = (int) value/2;
			if(value/2>0  && value/2<1){
				index = 1;
			}
		}
		return index;
	}

	@Override
	protected void prepareCalculatorDataRefpool() {
		// TODO Auto-generated method stub
		
	}
}
