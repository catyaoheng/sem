package data;

import java.util.ArrayList;

public interface Formated{
	public void initialize();
	public void initialize(String data_source_name, Object data);
	public int getLine_num();
	public void setLine_num(int line_num);
	public int getColumn_num();
	public void setColumn_num(int line_num);
	public int getIndex_num();
	public void setIndex_num(int index_num);
	public String getFeature_name();
	public void setFeature_name(String feature_name);
	public Object getData();
	public void setData(Object data);
	public ArrayList<String> getIndex();
	public void setIndex(ArrayList<String> index);
}