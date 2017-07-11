package ml.genetic.paper;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Phase4 {
	
	public static int numberOfZero,numberOfOne,numberOfTwo;
	public static int lineCount, formula;
	public static int counterZeros=0;
	public static int counterOnes=0;
	public static int counterTwos=0;
	public static int rowPrice=0;
	
	public static void PhaseProcess(String outputOfMLP, String outputOfRSITest) throws Exception{
		//String fName = "resources2/outputOfMLP.csv";
		String fName = outputOfMLP;
		//String fNameTest = "resources2/outputOfRSITest.csv";
		String fNameTest=outputOfRSITest;
		String thisLine; 
		String thisLineTest; 
		int count=0; 
		int countTest=0;
		FileInputStream fis = new FileInputStream(fName);
		FileInputStream fisTest = new FileInputStream(fNameTest);
		DataInputStream myInput = new DataInputStream(fis);
		DataInputStream myInputTest = new DataInputStream(fisTest);
		int i=0; 
		int j=0;
		
		

		String[][] data = new String[0][];//csv data line count=0 initially
		while ((thisLine = myInput.readLine()) != null) {
			++i;//increment the line count when new line found
			
			String[][] newdata = new String[i][2];//create new array for data

			String strar[] = thisLine.split(";");//get contents of line as an array
			newdata[i - 1] = strar;//add new line to the array

			System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
			data = newdata;//set new array as csv data
		}

		
		String[][] dataTest = new String[0][];//csv data line count=0 initially
		while ((thisLineTest = myInputTest.readLine()) != null) {
			++j;//increment the line count when new line found

			String[][] newdataTest = new String[j][2];//create new array for data

			String strarTest[] = thisLineTest.split(";");//get contents of line as an array
			newdataTest[j - 1] = strarTest;//add new line to the array

			System.arraycopy(dataTest, 0, newdataTest, 0, j - 1);//copy previously read values to new array
			dataTest = newdataTest;//set new array as csv data
		}


	
		
		converToFinancialData(data,dataTest);
		System.out.println("finished");
	}
	/*public static void main(String[] args) throws Exception  {

		
		String fName = "resources2/outputOfMLP.csv";
		String fNameTest = "resources2/outputOfRSITest.csv";
		String thisLine; 
		String thisLineTest; 
		int count=0; 
		int countTest=0;
		FileInputStream fis = new FileInputStream(fName);
		FileInputStream fisTest = new FileInputStream(fNameTest);
		DataInputStream myInput = new DataInputStream(fis);
		DataInputStream myInputTest = new DataInputStream(fisTest);
		int i=0; 
		int j=0;
		
		

		String[][] data = new String[0][];//csv data line count=0 initially
		while ((thisLine = myInput.readLine()) != null) {
			++i;//increment the line count when new line found
			
			String[][] newdata = new String[i][2];//create new array for data

			String strar[] = thisLine.split(";");//get contents of line as an array
			newdata[i - 1] = strar;//add new line to the array

			System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
			data = newdata;//set new array as csv data
		}

		
		String[][] dataTest = new String[0][];//csv data line count=0 initially
		while ((thisLineTest = myInputTest.readLine()) != null) {
			++j;//increment the line count when new line found

			String[][] newdataTest = new String[j][2];//create new array for data

			String strarTest[] = thisLineTest.split(";");//get contents of line as an array
			newdataTest[j - 1] = strarTest;//add new line to the array

			System.arraycopy(dataTest, 0, newdataTest, 0, j - 1);//copy previously read values to new array
			dataTest = newdataTest;//set new array as csv data
		}


	
		
		converToFinancialData(data,dataTest);
		System.out.println("finished");


	}*/
	
	

	public static void converToFinancialData(String[][] board, String[][] dataTest) {
		StringBuilder builder = new StringBuilder();
		for(int n = 0; n < board.length; n++)//for each row
		{
			if(board[n][2].substring(0,3).equals("0.0")){
				counterZeros++;
			}else if(board[n][2].substring(0,3).equals("1.0")){
				counterOnes++;
			}else if(board[n][2].substring(0,3).equals("2.0")){
				counterTwos++;
			}
			if((n+1)%20==0){
				
				if(counterZeros>14){
					builder.append(dataTest[rowPrice][0]+";"+"0.0\n");
				} else if(counterOnes>14){
					builder.append(dataTest[rowPrice][0]+";"+"1.0\n");
				} else if(counterTwos>14){
					builder.append(dataTest[rowPrice][0]+";"+"2.0\n");
				} else{
					builder.append(dataTest[rowPrice][0]+";"+"0.0\n");
				}
				counterZeros=0;
				counterOnes=0;
				counterTwos=0;
				rowPrice++;
			}
			
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("resources2/outputOfTestPrediction.txt"));
			writer.write(builder.toString());//save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
