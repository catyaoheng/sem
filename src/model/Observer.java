package model;

import util.*;

public class Observer{
	
	Observer(){
		
	}
	
	public void initialize(){
		
	}
	
	//Method[0]
	public void observe(String featureName, String measurementName, Boolean is_merged, int valueNum){
		if(featureName.equalsIgnoreCase("all")){
			// Coming soon.
		}
		else{
			readMeasurementValue(featureName, measurementName, is_merged);
			writeOverservation(featureName, measurementName, is_merged);
		}
		
	}
	
	public void observe(String geneNum, String geneArgs){
		
	}
	
	public String[] observe(String geneId_0, String geneId_1, Boolean is_merged){
		decipherFileName(geneId_0, geneId_1);
		return null;
	}
	
	

	//Method[1]
	private void decipherFileName(String geneId_0, String geneId_1) {
		// TODO Auto-generated method stub
		
	}
	
	private void readMeasurementValue(String featureName, String measurementName, Boolean is_merged) {
		// TODO Auto-generated method stub
		
	}

	public void writeOverservation(String featureName, String measurementName, Boolean is_merged){
		
	}
	
	//Method[2]
	
}







