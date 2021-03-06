package statistic;

import java.io.*;
import java.util.*;

import util.*;
import data.*;

public class CalculatorCreator{
	public static Calculator createCalculator(String calculator_name){
		
		if(calculator_name.equalsIgnoreCase("Pearsons")) {
			return new PearsonsCalculator();
		}
		else if(calculator_name.equalsIgnoreCase("Mutual")) {
			return new MutualCalculator();
		}
		else if(calculator_name.equalsIgnoreCase("SubLocSimilarity")) {
			return new SimpleSubLocSimilarity();
		}
		else if(calculator_name.equalsIgnoreCase("SubaSimilarity")) {
			return new SubaSimilarity();
		}
		else if(calculator_name.equalsIgnoreCase("InparanoidSimilarity")) {
			return new InparanoidSimilarity();
		}
		else if(calculator_name.equalsIgnoreCase("BlastSimilarity")) {
			return new BlastSimilarity();
		}
		else if(calculator_name.equalsIgnoreCase("Simple")) {
			return new Simple();
		}
		else if(calculator_name.equalsIgnoreCase("Minimum_count")) {
			return new MinimumCount();
		}
		else if(calculator_name.equalsIgnoreCase("CountDomain")) {
			return new CountDomain();
		}
		else if(calculator_name.equalsIgnoreCase("CountDomainFraction")) {
			return new CountDomainFraction();
		}
		return null;
	}
}
