package sem;

import java.util.Iterator;
import java.util.Map;

import feature.Feature;
import feature.FeatureCreator;

public class SEM {
//	private Feature coexpression;
//	private Feature geneontology;
//	private Feature domain;
	private Feature phylogenetic;
	//static
	public static SEMDataPool data_pool;
	
	public SEM(){
	}
	
	public void initialize(){
		System.out.println(">Initializing SEM ...");
		data_pool = new SEMDataPool();
		data_pool.initialize();
//		coexpression = FeatureCreator.createFeature("coexpression");
//		geneontology = FeatureCreator.createFeature("geneontology");
//		domain = FeatureCreator.createFeature("domain");
		phylogenetic = FeatureCreator.createFeature("phylogenetic");
		System.out.println("\tSEM Initialized Sucessfully\n");
//		domain.initialize();
//		geneontology.initialize();
//		colacolization.initialize();
//		phylogenetic.initialize();
//		interologs.initialize();
	}
	
	public void calculateFeature(){
		System.out.println(">Calculating SEM Feature(s) ...");
		phylogenetic.calculate();
//		coexpression.calculate();
//		System.out.println(domain);
//		domain.calculate();
//		geneontology.calculate();
//		colacolization.calculate();
//		phylogenetic.calculate();
//		interologs.calculate();
		System.out.println("\tSEM Feature(s) Calculated Sucessfully\n");
	}
	
	public void buildModel(){
		System.out.println(">Building SEM Model ...");
		System.out.println("\tSEM Model built Sucessfully\n");
	}
	
	public void validateLatentfactor(){
		System.out.println(">Validating SEM Latentfactor(s) ...");
		System.out.println("\tSEM Latentfactor(s) Validated Sucessfully\n");
	}
	
	public void structureLearning(){
		System.out.println(">Running SEM Structure Learning ...");
		System.out.println("\tSEM Structure Learning Run Sucessfully\n");
	}
}




















