package ml.genetic.paper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GA {
	public static int populationCount=300;
	public static int counter=0;

	
	 public static void main(String[] args) {
		 Population myPop;
		 StringBuilder builder = new StringBuilder();
		 while(counter<50){
			 counter++;
			 // Create an initial population
			 myPop = new Population(populationCount, true);

			 // Evolve our population until we reach an optimum solution
			 int generationCount = 0;
			 myPop.printPopulation();
			
			
			 while (myPop.getFittest().getFitness()*0.7 > FitnessCalc.getAvgFitness()) {


				 generationCount++;
				 System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness() + " Fittest Chromosome: " + myPop.getFittest());
				 builder.append("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness() + " Fittest Chromosome: " + myPop.getFittest()+"\n");
				 System.out.println("FitnessCalc.getAvgFitness():" + FitnessCalc.getAvgFitness());
				 myPop = Algorithm.evolvePopulation(myPop);
				 myPop.printPopulation();
				 //calculatedFitnessFlag=0;
				 //System.out.println("population:" +myPop.size());
			 }
			 System.out.println("Solution found!");
			 System.out.println("Generation: " + generationCount);
			 System.out.println("Genes:");
			 myPop.getFittest().printChromosome();
			 System.out.println("--------------------------------");
			 FitnessCalc.setFitnessTotal(0);
			 
			 builder.append(myPop.getFittest().stringBuilder());
			 builder.append(holdGeneGenerator());
			 builder.append(holdGeneGenerator1());
			 BufferedWriter writer;
			 try {
				 writer = new BufferedWriter(new FileWriter("resources2/GATraining/GATableListTraining_"+counter+".txt"));
				 writer.write(builder.toString());//save the string representation of the board
				 writer.close();
			 } catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }

			 
		 }
		 
		 
	 }

	 public static String holdGeneGenerator() {
		 String line="";
		 int value,interval;
		 int randomValueMax=60;
		 int randomValueMin=40;
		 int randomIntervalMax=20;
		 int randomIntervalMin=2;
		 value= (int)(Math.random()*(randomValueMax-randomValueMin)+randomValueMin);
		 interval= (int)(Math.random()*(randomIntervalMax-randomIntervalMin)+randomIntervalMin);
		 line=line+"0 "+"1:"+String.valueOf(value)+".0"+" 2:"+String.valueOf(interval)+".0"+" 3:0.0"+"\n";
		 return line;
	 }
	 
	 public static String holdGeneGenerator1() {
		 String line="";
		 int value,interval;
		 int randomValueMax=60;
		 int randomValueMin=40;
		 int randomIntervalMax=20;
		 int randomIntervalMin=2;
		 value= (int)(Math.random()*(randomValueMax-randomValueMin)+randomValueMin);
		 interval= (int)(Math.random()*(randomIntervalMax-randomIntervalMin)+randomIntervalMin);
		 line=line+"0 "+"1:"+String.valueOf(value)+".0"+" 2:"+String.valueOf(interval)+".0"+" 3:1.0"+"\n";
		 return line;
	 }
		   
}
