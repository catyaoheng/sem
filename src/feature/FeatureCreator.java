package feature;

import java.io.*;
import java.util.*;

import util.*;
import data.*;

public class FeatureCreator{
	public static Feature createFeature(String feature_name){
		
		if(feature_name.equalsIgnoreCase("Coexpression")) {
			Coexpression coexpression = new Coexpression();
			coexpression.initialize(feature_name);
			return coexpression;
		}
		else if(feature_name.equalsIgnoreCase("Domain")) {
			Domain domain = new Domain();
			domain.initialize(feature_name);
			return domain;
		}
		else if(feature_name.equalsIgnoreCase("Geneontology")) {
			Geneontology geneontology = new Geneontology();
			geneontology.initialize(feature_name);
			return geneontology;
		}
		else if(feature_name.equalsIgnoreCase("Colocalization")) {
			Colocalization colocalization = new Colocalization();
			colocalization.initialize(feature_name);
			return colocalization;
		}
		else if(feature_name.equalsIgnoreCase("Phylogenetic")) {
			Phylogenetic phylogenetic = new Phylogenetic();
			phylogenetic.initialize(feature_name);
			return phylogenetic;
		}
		else if(feature_name.equalsIgnoreCase("Interology")) {
			Interology interology = new Interology();
			interology.initialize(feature_name);
			return interology;
		}
//		else throw new Exception();
		else return null;
	}
}