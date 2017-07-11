package ml.genetic.paper;

import java.io.DataInputStream;
import java.io.FileInputStream;

import org.apache.commons.math3.util.Precision;

public class FitnessCalc {
	
	static int maxFitness;
	static int avgFitness;
	public static int fitnessTotal=0;
	static byte[] solution = new byte[4];
 
    // Calculate inidividuals fittness by comparing it to our candidate solution
    static int getFitnessCalc(Chromosome chromosome) {
    	int fitness = 0;
    	
    	try {
			fitness=FitnessCalcScenario.calculateFitness(chromosome);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
    	fitnessTotal=fitnessTotal+fitness;
		if(maxFitness<fitness){
			maxFitness=fitness;
			//System.out.println("maxFitness:"+maxFitness);
	    }
    	
    	return fitness;
	
    }
 
   
    static int getAvgFitness() {
    	avgFitness=fitnessTotal/GA.populationCount;
        return avgFitness;
    }
    
    static void setFitnessTotal(int set) {
    	fitnessTotal=0;
    }
   
    static int getMaxFitnessCalc() {
        return maxFitness;
    }
}
