package feature;

import java.io.*;
import java.util.*;

import sem.SEM;
import sem.SEMDataPool;
import statistic.*;
import tools.Idmap;
import tools.Probe;
import util.*;
import data.*;

public abstract class Feature{
	
	//Fundamental Assemblies.
	//protected final ArrayList<Object> value;
	protected final State state;
	protected final HashMap<String, HashMap<String, ArrayList<String[]>>> file_pool;
	protected final HashMap<String, FormatedTable> table_pool;
	protected final HashMap<String, FormatedMatrix> matrix_pool;
	protected final HashMap<String, Calculator> calculator_pool;
	//static
	
	Feature(){
		//value = new ArrayList<Object>();
		state = new State();//this.getClass().getSimpleName().toLowerCase()
		file_pool = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
		table_pool = new HashMap<String, FormatedTable>();
		matrix_pool = new HashMap<String, FormatedMatrix>();
		calculator_pool = new HashMap<String, Calculator>();
	}
	
	public void initialize(String feature_name){
		System.out.println(">Initializing " + feature_name + "...");
		state.initialize(feature_name);
		//state.briefState();
		//state.testState();
		readFeature_files();
		prepareTablePool();
		prepareMatrix_pool();
		prepareData_pool();
		prepareCalculator_pool();
		//System.out.println(calculator_pool.get("countDomain").getCalculator_data_refer_pool().size());
		//Probe.probeMap("matrix_domain_interaction", calculator_pool.get("countDomain").getCalculator_data_refer_pool().get("matrix_domain_interaction"), false);
		System.out.println("\t" + feature_name + " initialized Sucessfully\n");
	}
	
	//calculate
	public void calculate(){
		//System.out.println("####################");
		//*/
		System.out.println("\t\t>Calculating " + state.getFeature_name() + "...");
		Iterator iter = calculator_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String current_entry_name = entry.getKey().toString();
			Calculator current_calculator = calculator_pool.get(current_entry_name);
			//System.out.print(current_calculator);
			current_calculator.calculateValue();
		}		
		System.out.println("\t" + state.getFeature_name() + " calculated Sucessfully\n");
		//*/
	}
	
	/*
	protected void recordValue_cluster() {
		//Original value cluster
	}
	//*/
	/*
	public ArrayList<Object> getValue() {
		return value;
	}
	//*/
	
	//state
	public State getState() {
		return state;
	}
	
	//files
	/*
	public HashMap<String, ArrayList<String[]>> getFile(String file_name){
		return file_pool.get("file_name");
	}
	//*/
	///*
	private void setFile(String file_name, HashMap<String, ArrayList<String[]>> mapped_file){
		this.file_pool.put(file_name, mapped_file);
	}
	//*/
	private void readFeature_files(){
		System.out.println("\tReading File...");
		String feature_name = state.getFeature_name();
		ArrayList<String> file_name_pool = state.getFile_name_pool();
		for(int i=0; i<file_name_pool.size(); i++){
			String current_file_name = file_name_pool.get(i);
			System.out.println("\t\t" + current_file_name);
			//	test
			//	System.out.println("$$" + current_file_name);
			HashMap<String, ArrayList<String>> current_file_info_pool = state.getFile_info(current_file_name);
			ArrayList<String[]> current_file = FileUtil.loadFile(feature_name, current_file_name);
			//	Decide if the indexing is needed.
			//System.out.println(current_file_info_pool);
			if(current_file_info_pool!=null){
				//System.out.println(current_file_info_pool.containsKey("file_index_column_nums"));
				if(current_file_info_pool.containsKey("file_index_column_nums")){
					//	Indexing.
					//	Prepare container and initialize
					HashMap<String, ArrayList<String[]>> mapped_file = new HashMap<String, ArrayList<String[]>>();
					for(int j=0; j<Idmap.getGene_idmap().size(); j++){
						ArrayList<String[]> current_line_cluster = new ArrayList<String[]>();
						String current_id_str = Idmap.getGene_idmap().get(j)[0];
						mapped_file.put(current_id_str, current_line_cluster);
					}
					for(int j=0; j<current_file.size(); j++){
						String[] current_line = current_file.get(j);
						String current_id_str = current_line[1];
						if(!current_id_str.equalsIgnoreCase("NA")){
							mapped_file.get(current_id_str).add(current_line);
						}
					}
					//test
					setFile(current_file_name, mapped_file);
				}
			}
			//	If the file does not need index, the file shall be a pure ArrayList<String[]> which would be put into the pseudo
			//	"mappedFile" as its only element(value) whose key is the original fileName.
			//	So, after tablePreparing or matrix Preparing, you need to get("table/matrixName") to get the data, 
			//	then get("fileName") from the map to get the ArrayList<String[]>.
			else{
				HashMap<String, ArrayList<String[]>> mapped_file = new HashMap<String, ArrayList<String[]>>();
				ArrayList<String[]> lines = new ArrayList<String[]>();
				mapped_file.put(current_file_name, lines);
				for(int j=0; j<current_file.size(); j++){
					mapped_file.get(current_file_name).add(current_file.get(j));
				}
				setFile(current_file_name, mapped_file);
			}
		}
		
		//*test
//		System.out.println("########");
//			Probe.probeMapName(file_pool);
			//Probe.probeMap("list_GEO_1893_RMA.txt", file_pool.get("list_GEO_1893_RMA.txt"), false);
			//System.out.println(file_pool);
			//Probe.probeMap(current_file_name, mapped_file);
		//*/
		System.out.println("\t\tReading File Finished");
		//System.out.println("##"+file_pool.get("test_abiotic_100").get("10997").get(0));
	}

	//tables
	protected abstract void tableOperation(String current_table_name);
	
	private void prepareTablePool(){
		System.out.println("\t>Preparing Tables...");
		//Supermarket.
		HashMap<String, HashMap<String, ArrayList<String[]>>> tableSourceMarket = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
		tableSourceMarket.putAll(file_pool);
		//Customers.
		ArrayList<String> tableNameList = state.getTable_name_pool();
		HashMap<String, ArrayList<String>> tableSourceInfoPool = new HashMap<String, ArrayList<String>>();
		for(int i=0; i<tableNameList.size(); i++){
			//Customer's info& shopping list.
			String currentTableName = tableNameList.get(i);
			HashMap<String, ArrayList<String>> currentTableInfo = state.getTable_info(currentTableName);
				ArrayList<String> currentTableSourceNameList = new ArrayList<String>();
				if(currentTableInfo.containsKey("table_data_source")){
					currentTableSourceNameList.addAll(currentTableInfo.get("table_data_source"));
				}
				if(currentTableInfo.containsKey("table_refer_source")){
					currentTableSourceNameList.addAll(currentTableInfo.get("table_refer_source"));
				}
			tableSourceInfoPool.put(currentTableName, currentTableSourceNameList);
		}
		//Let the shopping begin.
		for(int i=0; i<tableNameList.size(); i++){
			String currentTableName = tableNameList.get(i);
			System.out.println("\t\t>Initializing " + currentTableName + "...");
			if(!(table_pool.containsKey(currentTableName))){
				System.out.println("\t\t\t>Creating " + currentTableName + "...");
				createTable(currentTableName, tableSourceInfoPool, tableSourceMarket);
			}
			System.out.println("\t\t\t" + currentTableName + " initialized Sucessfully\n");
		}
		//*test
		//Probe.probeMapName(table_pool);
		//System.out.println("########");
        //System.out.println(table_pool.get("table_gene_association").getTable_data().get("3"));
        //System.out.println(table_pool.get("table_gene_association").getTable_data().get("5"));
        //System.out.println(table_pool.get("table_gene_association").getTable_data().get("8"));
        //System.out.println(table_pool.get("table_gene_association").getTable_data().get("9"));
        //System.out.println(table_pool);
		//Probe.probeMap("table_arathSuba", table_pool.get("table_arathSuba").getTable_data(), false);
		//Probe.probeMap("table_arathSubaList", table_pool.get("table_arathSubaList").getTable_data(), false);
		//Probe.probeMap("table_term_id_pool_pro", table_pool.get("table_term_id_pool_pro").getTable_data(), false);
		//Probe.probeMap("table_gene_product_count", table_pool.get("table_gene_product_count").getTable_data(), false);
		//System.out.println(table_pool.get("table_term").getTable_data().size());
		//Probe.probeMap("table_term", table_pool.get("table_term").getTable_data(), false);
		//System.out.println(file_pool);
		//Probe.probeMap(current_file_name, mapped_file);
		//*/
		//System.out.println("\t\t"+table_pool);
		System.out.println("\t\tPreparing Table Finished");
	}
	
	private void createTable(
			String currentTableName,
			HashMap<String, ArrayList<String>> tableSourceInfoPool,
			HashMap<String, HashMap<String, ArrayList<String[]>>> tableSourceMarket) {
		// TODO Auto-generated method stub
		ArrayList<String> currentTableSourceNameList = tableSourceInfoPool.get(currentTableName);
		for(int i=0; i<currentTableSourceNameList.size(); i++){
			String currentTableSourceName = currentTableSourceNameList.get(i);
			if(!(tableSourceMarket.containsKey(currentTableSourceName))){
				createTable(currentTableSourceName, tableSourceInfoPool, tableSourceMarket);
			}
		}
		FormatedTable thisTable = new FormatedTable();
		//System.out.println(currentTableName);
		HashMap<String, ArrayList<String>> currentTableInfo = state.getTable_info(currentTableName);
		thisTable.initialize(currentTableName, currentTableInfo, tableSourceMarket);
		table_pool.put(currentTableName, thisTable);
		//System.out.println("### prepareAssociation ###");
		tableOperation(currentTableName);
		table_pool.put(currentTableName, thisTable);
		
		Iterator iter = table_pool.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry currentEntry = (Map.Entry) iter.next();
			String currentEntryName = currentEntry.getKey().toString();
			HashMap<String, ArrayList<String[]>> currentTableData = table_pool.get(currentEntryName).getTable_data();
			tableSourceMarket.put(currentEntryName, currentTableData);
		}
	}

	//matrixes
	protected abstract void matrixOperation(String current_matrix_name);
	/*
	protected HashMap<String, FormatedMatrix> getMatrixe_pool() {
		return matrix_pool;
	}
	//*/
	/*
	public void setMatrixes(HashMap<String , FormatedMatrix> matrix_pool) {
		this.matrix_pool = matrix_pool;
	}
	//*/
	/*
	protected FormatedMatrix getMatrix(String matrix_name){
		return matrix_pool.get(matrix_name);	
	}
	//*/
	public void setMatrix(String matrix_name, FormatedMatrix matrix) {
		this.matrix_pool.put(matrix_name, matrix);
	}
	private void prepareMatrix_pool(){
		System.out.println("\t>Preparing Matrices...");
		ArrayList<String> matrix_name_pool = state.getMatrix_name_pool();
		for(int i=0; i<matrix_name_pool.size(); i++){
			String current_matrix_name = matrix_name_pool.get(i);
			HashMap<String, ArrayList<String>> current_matrix_info = state.getMatrix_info(current_matrix_name);
			ArrayList<String> current_matrix_source_name_pool = new ArrayList<String>();
			System.out.println();
			current_matrix_source_name_pool.addAll(current_matrix_info.get("matrix_data_source"));
			//Prepare matrix_data_source_pool.
			HashMap<String, HashMap<String, ArrayList<String[]>>> current_matrix_data_source_pool = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
			for(int j=0; j<current_matrix_source_name_pool.size(); j++){
				String current_matrix_source_name = current_matrix_source_name_pool.get(j);
				//System.out.println(current_matrix_name);
				//System.out.println(current_matrix_source_name);
				current_matrix_data_source_pool.put(current_matrix_source_name, table_pool.get(current_matrix_source_name).getTable_data());
			}
			FormatedMatrix this_matrix = new FormatedMatrix();
			this_matrix.initialize(current_matrix_name, current_matrix_info, current_matrix_data_source_pool);
			matrix_pool.put(current_matrix_name, this_matrix);
			matrixOperation(current_matrix_name);
			matrix_pool.put(current_matrix_name, this_matrix);
			//System.out.println(matrix_pool.get("matrix_test_abiotic_100").getMatrix_data());
		}
		
		//*/test_matrix
		//Probe.probeMap("matrix_list_GEO_1893_RMA.txt", matrix_pool.get("matrix_list_GEO_1893_RMA.txt").getMatrix_data(), false);
		//Probe.probeMap("matrix_arathSuba", matrix_pool.get("matrix_arathSuba").getMatrix_data(), false);
		//Probe.probeMap("matrix_arathSubaList", matrix_pool.get("matrix_arathSubaList").getMatrix_data(), false);
		//*/
		
		System.out.println("\t\tPreparing Matrices Finished");
	}
	
	//data_pool
	private void prepareData_pool() {
		SEM.data_pool.pushMatrix(state.getFeature_name(), matrix_pool);
		
	}
	
	//calculators
	protected abstract void calculatorOperation(String current_matrix_name);
	/*
	public HashMap<String, Calculator> getCalculator_pool() {
		return calculator_pool;
	}
	/*
	public void setCalculators(HashMap<String, Calculator> calculator_pool) {
		this.calculator_pool = calculator_pool;
	}
	//*/
	private void prepareCalculator_pool(){
		System.out.println("\t>Preparing Calculator...");
		ArrayList<String> calculator_name_pool = state.getCalculator_name_pool();
		for(int i=0; i<calculator_name_pool.size(); i++){
			String current_calculator_name = calculator_name_pool.get(i);
			HashMap<String, ArrayList<String>> current_calculator_info = state.getCalculator_info(current_calculator_name);
			ArrayList<String> current_calculator_source_name_pool = new ArrayList<String>();
			current_calculator_source_name_pool.addAll(current_calculator_info.get("calculator_data_source"));
			//Prepare matrix_data_source_pool.
			HashMap<String, HashMap<String, ArrayList<String[]>>> current_calculator_data_source_pool = new HashMap<String, HashMap<String, ArrayList<String[]>>>();
			for(int j=0; j<current_calculator_source_name_pool.size(); j++){
				String current_calculator_source_name = current_calculator_source_name_pool.get(j);
				current_calculator_data_source_pool.put(current_calculator_source_name, matrix_pool.get(current_calculator_source_name).getMatrix_data());
			}
			Calculator this_calculator = CalculatorCreator.createCalculator(current_calculator_name);
			//System.out.println(this_calculator);
			//System.out.println(state.getFeature_name());
			//System.out.println(current_calculator_name);
			//System.out.println(current_calculator_info);
			this_calculator.initialize(state.getFeature_name(), current_calculator_name, current_calculator_info);
			calculator_pool.put(current_calculator_name, this_calculator);
			calculatorOperation(current_calculator_name);
			calculator_pool.put(current_calculator_name, this_calculator);
		}
		System.out.println("\t\tPreparing Calculator Finished");
	}
	//test
	//test File(done)
	//test Table(working ...)
	//test Matrix(working ...)
	//test Calculator(working ...)
}



















