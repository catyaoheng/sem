package data;

import java.io.*;
import java.util.*;

import util.*;
import data.*;

public class DataCreator{
	public static FormatedData createData(String data_type){
		if(data_type.equalsIgnoreCase("Table")) return new Table();
		else if(data_type.equalsIgnoreCase("Matrix")) return new Matrix();
		else return null;
	}
}