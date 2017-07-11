package ml.genetic.paper;

import java.io.File;
import java.util.Scanner;


public class Scheduler {
	public static int CREATETESTFILE=0;
	public static int CALCULATE=1;
	
	public static int mode=CREATETESTFILE;
	public static String inputFilePathPhase1="resources2/MSFT19972007.csv";
	public static String inputFilePathPhase1Test="resources2/MSFT20072017.csv";
	
	public static String filePathOutputOfRSITest;
	public static String filePathOutputOfRSITestCSV;
	public static String filePathOutputOfMLP;
	public static String filePathOutputOfMLPCSV;
	//required GATableListTraining.txt,
	 public static void main(String[] args) throws Exception {
		 
		
		 	 System.out.println("Phase0");
			 Phase0.PhaseProcess(inputFilePathPhase1Test);
			 
			 System.out.println("Phase1");
			 Phase1.PhaseProcess();

			 String directoryPath = "D:\\workspace_Java\\SparkJava\\resources2\\output.csv";
			 File[] filesInDirectory = new File(directoryPath).listFiles();
			 for(File f : filesInDirectory){
				 filePathOutputOfRSITest = f.getAbsolutePath();
				 String fileExtenstion = filePathOutputOfRSITest.substring(filePathOutputOfRSITest.lastIndexOf(".") + 1,filePathOutputOfRSITest.length());
				 if("csv".equals(fileExtenstion)){
					 filePathOutputOfRSITestCSV=filePathOutputOfRSITest;
					 System.out.println("Phase2");
					 Phase2.PhaseProcess(filePathOutputOfRSITestCSV);
				 }
			 }     

			 System.out.println("Phase3");
			 Phase3.PhaseProcess();

			 String directoryPath2 = "D:\\workspace_Java\\SparkJava\\resources2\\outputMLP.csv";
			 File[] filesInDirectory2 = new File(directoryPath2).listFiles();
			 for(File f : filesInDirectory2){
				 filePathOutputOfMLP = f.getAbsolutePath();
				 String fileExtenstion2 = filePathOutputOfMLP .substring(filePathOutputOfMLP .lastIndexOf(".") + 1,filePathOutputOfMLP.length());
				 if("csv".equals(fileExtenstion2)){
					 filePathOutputOfMLPCSV=filePathOutputOfMLP;
					 System.out.println("Phase4");
					 Phase4.PhaseProcess(filePathOutputOfMLPCSV, filePathOutputOfRSITestCSV );
					
				 }
			 }  
			
			 System.out.println("Phase5");
			 Phase5.PhaseProcess();
		 }



		 

	 


}
