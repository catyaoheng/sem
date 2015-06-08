package feature;

import java.util.ArrayList;
import java.util.HashMap;

public class Domain extends Feature {	
	@Override
	protected void tableOperation(String currentTableName) {
		//*/
		if(currentTableName.equalsIgnoreCase("table_domain_interaction")){//	refer
			ArrayList<String[]> tableDomain_interaction = table_pool.get("table_domain_interaction").getTable_data().get("domain_interaction");
			//System.out.println(table_pool.get("table_term").getTable_data().get("term"));
			prepareInteractionList(currentTableName, tableDomain_interaction);
		}
		//*/
		
	}

	@Override
	protected void matrixOperation(String current_matrix_name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void calculatorOperation(String currentCalculatorName) {
		
	}

	

	//Method[1]
	private void prepareInteractionList(String currentTableName,
			ArrayList<String[]> tableDomain_interaction) {
		// TODO Auto-generated method stub
		for(int i=0; i<tableDomain_interaction.size(); i++){
			tableDomain_interaction.get(i)[1] = tableDomain_interaction.get(i)[0]+tableDomain_interaction.get(i)[1];
		}
	}
	
	private void prepareInteractionMap(String currentCalculatorName,
			ArrayList<String[]> tableDomain_interaction) {
		// TODO Auto-generated method stub
		
	}
}
