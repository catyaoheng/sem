package main;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.*;
import sem.SEM;
import sem.SEMDataPool;
import tools.Idmap;
import util.*;
import feature.*;

public class Main {
	
	Config config = null;
	Idmap idmap = null;
	SEM sem = null;
	SEMDataPool dataPool =null;
	
	public void initialize() {
		config = new Config();
		config.initialize();
		config.testPathways();
		idmap = new Idmap();
		idmap.initialize();
		//idmap.mapFile("geneontology", "gene_association", true);
		//idmap.mapFile("domain", "arth_all_domains", true);
		//idmap.mapAll();
		idmap.mapAll();
		sem = new SEM();
		sem.initialize();
	}

	public void run(){
		sem.calculateFeature();
		sem.buildModel();
		sem.validateLatentfactor();
		sem.structureLearning();
	}
	
	public static void main(String[] args) {
		System.out.println(new Date());
		Main mainObject = new Main();
		mainObject.initialize();
		mainObject.run();
		System.out.println("SEM Ran Sucessfully");
		System.out.println(new Date());
	}
}


