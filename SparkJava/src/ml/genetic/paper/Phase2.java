package ml.genetic.paper;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.util.Precision;

public class Phase2 {
	
	public static int numberOfZero,numberOfOne,numberOfTwo;
	public static int lineCount, formula;
	public static Double sma50,sma200,trend;
	public static String trendString;
	
	public static void PhaseProcess(String inputFilePath) throws Exception{
		//String fName = "resources2/outputOfRSITest.csv";
		String fName = inputFilePath;
		String thisLine; 
		int count=0; 
		FileInputStream fis = new FileInputStream(fName);
		DataInputStream myInput = new DataInputStream(fis);
		int i=0; 


		String[][] data = new String[0][];//csv data line count=0 initially
		while ((thisLine = myInput.readLine()) != null) {
			++i;//increment the line count when new line found

			String[][] newdata = new String[i][2];//create new array for data

			String strar[] = thisLine.split(";");//get contents of line as an array
			newdata[i - 1] = strar;//add new line to the array

			System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
			data = newdata;//set new array as csv data
		}

		converToTestData(data);
		
	}
	
	/*public static void main(String[] args) throws Exception  {

		String fName = "resources2/outputOfRSITest.csv";
		String thisLine; 
		int count=0; 
		FileInputStream fis = new FileInputStream(fName);
		DataInputStream myInput = new DataInputStream(fis);
		int i=0; 


		String[][] data = new String[0][];//csv data line count=0 initially
		while ((thisLine = myInput.readLine()) != null) {
			++i;//increment the line count when new line found

			String[][] newdata = new String[i][2];//create new array for data

			String strar[] = thisLine.split(";");//get contents of line as an array
			newdata[i - 1] = strar;//add new line to the array

			System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
			data = newdata;//set new array as csv data
		}

		


	
		
		converToTestData(data);
		


	}*/
	
	

	public static void converToTestData(String[][] board) {
		StringBuilder builder = new StringBuilder();
		for(int n = 0; n < board.length; n++)//for each row
		{
			for(int j = 0; j < board[0].length-2; j++)//for each column - except last 2 columns smas
			{
				sma50=Double.valueOf(board[n][21]);
				sma200=Double.valueOf(board[n][22]);
				
				trend=sma50-sma200;
				
				if(trend>0){//uptrend
					trendString="1.0";
				}else{ //downtrend
					trendString="0.0";
				}
				if(j>0)
					builder.append("5 1:"+board[n][j]+" "+"2:"+j+" "+"3:"+trendString+"\n");

			}
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("resources2/GATableListTest.txt"));
			writer.write(builder.toString());//save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
