package data;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class FormatedData implements Formated{
	
	protected int line_num;
	protected int column_num;
	protected int index_num;
	protected String feature_name;
	protected Object data;
	protected ArrayList<String> index;
	protected HashMap<String, ArrayList<Object>> formated_data;
	
	FormatedData(){
		setLine_num(0);
		setColumn_num(0);
		setLine_num(0);
		feature_name = new String();
		data = new Object();
		index = new ArrayList<String>();
	}
	public abstract void initialize();
	public abstract void initialize(String data_source_name, Object data);
//	public void conunt_line_num() {
//		setLine_num(this.data.size());
//	}
//	private void conunt_column_num();
	public int getLine_num(){
		return line_num;
	}
	public void setLine_num(int line_num){
		this.line_num = line_num;
	}
	
	public int getColumn_num(){
		return column_num;
	}
	public void setColumn_num(int column_num){
		this.column_num = column_num;
	}
	
	public int getIndex_num() {
		return index_num;
	}
	public void setIndex_num(int index_num) {
		this.index_num = index_num;
	}
	
	public String getFeature_name() {
		return feature_name;
	}
	public void setFeature_name(String feature_name) {
		this.feature_name = feature_name;
	}
	
	public Object getData(){
		return data;
	}
	public void setData(Object data){
		this.data = data;
	}
	
	public ArrayList<String> getIndex() {
		return index;
	}
	public void setIndex(ArrayList<String> index) {
		this.index = index;
	}
}