package ml.genetic.paper;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.util.Precision;

public class Phase5 {
	
	public static void PhaseProcess() throws Exception{
		StringBuilder builder = new StringBuilder();
		String fName = "resources2/outputOfTestPrediction.txt";
		String thisLine; 
		int count=0; 
		FileInputStream fis = new FileInputStream(fName);
		DataInputStream myInput = new DataInputStream(fis);
		int i=0, totalTransactionLength=0; 
		Double buyPoint,sellPoint,gain=0.0,totalGain=0.0, money=10000.0, shareNumber=0.0, moneyTemp=0.0, maximumMoney=0.0, minimumMoney=10000.0,maximumGain=0.0, maximumLost=100.0, totalPercentProfit=0.0;
		int transactionCount=0, successTransactionCount=0,failedTransactionCount=0;
		Double buyPointBAH,shareNumberBAH,moneyBAH=10000.0, maximumProfitPercent=0.0, maximumLostPercent=0.0;
		Boolean forceSell=false;



		String[][] data = new String[0][];//csv data line count=0 initially
		while ((thisLine = myInput.readLine()) != null) {
			++i;//increment the line count when new line found

			String[][] newdata = new String[i][2];//create new array for data

			String strar[] = thisLine.split(";");//get contents of line as an array
			newdata[i - 1] = strar;//add new line to the array

			System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
			data = newdata;//set new array as csv data
		}


		int k=0;
		System.out.println("Start Capital: \\$" + money);
		builder.append("Start Capital: \\$" + money+"\n");
		
		while(k<data.length-1){

			if(Double.valueOf(data[k][1])==1.0){

				buyPoint=Double.valueOf(data[k][0]);
				buyPoint=buyPoint*100;
				shareNumber=(money-1.0)/buyPoint;
				forceSell=false;
				for (int j=k; j<data.length-1; j++) {

					sellPoint=Double.valueOf(data[j][0]);
					sellPoint=sellPoint*100;
					moneyTemp=(shareNumber*sellPoint)-1.0;
					//stop loss %5
					if(money*0.85>moneyTemp){
						money=moneyTemp;
						forceSell=true;
					}
					
					if(Double.valueOf(data[j][1])==2.0 || forceSell==true){
						sellPoint=Double.valueOf(data[j][0]);
						sellPoint=sellPoint*100;
						gain=sellPoint-buyPoint;
						if(gain>0){
							successTransactionCount++;
						}
						else{
							failedTransactionCount++;
						}

						if(gain>=maximumGain){
							maximumGain=gain;
							maximumProfitPercent=Double.valueOf(maximumGain)/Double.valueOf(buyPoint)*100;		
						}
						if(gain<=maximumLost){
							maximumLost=gain;
							maximumLostPercent=Double.valueOf(maximumLost)/Double.valueOf(buyPoint)*100;		
						}
						moneyTemp=(shareNumber*sellPoint)-1.0;
						money=moneyTemp;
						if(money>maximumMoney){
							maximumMoney=money;
						}
						if(money<minimumMoney){
							minimumMoney=money;
						}
						transactionCount++;
						//System.out.println("\\\\"+transactionCount+"."+"("+(k+1)+"-"+(j+1)+") => " + Precision.round(sellPoint,2) + "-" + Precision.round(buyPoint,2)+ "= " + Precision.round(gain,2) + " Capital: \\$" + Precision.round(money,2) );
						System.out.println(transactionCount+"."+"("+(k+1)+"-"+(j+1)+") => " + Precision.round((gain*shareNumber),2) + " Capital: $" + Precision.round(money,2) );
						builder.append(transactionCount+"."+"("+(k+1)+"-"+(j+1)+") => " + Precision.round((gain*shareNumber),2) + " Capital: $" + Precision.round(money,2)+"\n");
						
						totalPercentProfit=totalPercentProfit +(gain/buyPoint);

						totalTransactionLength=totalTransactionLength+(j-k);
						k=j+1;
						totalGain=totalGain+gain;
						break;
					}
				}
			}
			k++;
		}

		System.out.println("Our System => totalMoney = $" + Precision.round(money,2) );
		builder.append("Our System => totalMoney = $" + Precision.round(money,2)+"\n");
		
		buyPointBAH=Double.valueOf(data[0][0]);
		shareNumberBAH=(moneyBAH-1.0)/buyPointBAH;
		moneyBAH=(Double.valueOf(data[data.length-1][0])*shareNumberBAH)-1.0;


		System.out.println("BAH => totalMoney = $" + Precision.round(moneyBAH,2) );
		builder.append("BAH => totalMoney = $" + Precision.round(moneyBAH,2)+"\n");

		double numberOfDays=Double.valueOf(data.length-1);
		double numberOfYears=numberOfDays/365;
		//money/10000.0;

		//report the results
		System.out.println("Our System Annualized return % => " + Precision.round(((Math.exp(Math.log(money/10000.0)/numberOfYears)-1)*100),2) + "%" );
		System.out.println("BaH Annualized return % => " +  Precision.round(((Math.exp(Math.log(moneyBAH/10000.0)/numberOfYears)-1)*100),2) +"%");
		System.out.println("Annualized number of transaction => " + Precision.round((Double.valueOf(transactionCount)/numberOfYears),1) + "#");
		System.out.println("Percent success of transaction => " +Precision.round((Double.valueOf(successTransactionCount)/Double.valueOf(transactionCount))*100,2)+ "%");
		System.out.println("Average percent profit per transaction => " + Precision.round((totalPercentProfit/transactionCount*100),2) + "%" );
		System.out.println("Average transaction length => " + totalTransactionLength/transactionCount + "#");
		System.out.println("Maximum profit percent in transaction=> " + Precision.round(maximumProfitPercent,2) + "%");
		System.out.println("Maximum loss percent in transaction=> " + Precision.round(maximumLostPercent,2)+ "%");
		System.out.println("Maximum capital value=> " + "$"+ Precision.round(maximumMoney,2));
		System.out.println("Minimum capital value=> " + "$"+ Precision.round(minimumMoney,2));
		System.out.println("Idle Ratio %=>  " +  Precision.round((Double.valueOf(data.length-totalTransactionLength)/Double.valueOf(data.length)*100),2) + "%");



		

		builder.append("Our System Annualized return % => " + Precision.round(((Math.exp(Math.log(money/10000.0)/numberOfYears)-1)*100),2) + "%" +"\n");
		builder.append("BaH Annualized return % => " +  Precision.round(((Math.exp(Math.log(moneyBAH/10000.0)/numberOfYears)-1)*100),2) +"%"+"\n");
		builder.append("Annualized number of transaction => " + Precision.round((Double.valueOf(transactionCount)/numberOfYears),1) + "#"+"\n");
		builder.append("Percent success of transaction => " +Precision.round((Double.valueOf(successTransactionCount)/Double.valueOf(transactionCount))*100,2)+ "%"+"\n");
		builder.append("Average percent profit per transaction => " + Precision.round((totalPercentProfit/transactionCount*100),2) + "%" +"\n");
		builder.append("Average transaction length => " + totalTransactionLength/transactionCount + "#"+"\n");
		builder.append("Maximum profit percent in transaction=> " + Precision.round(maximumProfitPercent,2) + "%"+"\n");
		builder.append("Maximum loss percent in transaction=> " + Precision.round(maximumLostPercent,2)+ "%"+"\n");
		builder.append("Maximum capital value=> " + "$"+ Precision.round(maximumMoney,2)+"\n");
		builder.append("Minimum capital value=> " + "$"+ Precision.round(minimumMoney,2)+"\n");
		builder.append("Idle Ratio %=>  " +  Precision.round((Double.valueOf(data.length-totalTransactionLength)/Double.valueOf(data.length)*100),2) + "%"+"\n");
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("resources2/Results.txt"));
			writer.write(builder.toString());//save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
	public static void main(String[] args) throws Exception  {

		String fName = "resources2/outputOfTestPrediction.txt";
		String thisLine; 
		int count=0; 
		FileInputStream fis = new FileInputStream(fName);
		DataInputStream myInput = new DataInputStream(fis);
		int i=0, totalTransactionLength=0; 
		Double buyPoint,sellPoint,gain=0.0,totalGain=0.0, money=10000.0, shareNumber=0.0, moneyTemp=0.0, maximumMoney=0.0, minimumMoney=10000.0,maximumGain=0.0, maximumLost=100.0, totalPercentProfit=0.0;
		int transactionCount=0, successTransactionCount=0,failedTransactionCount=0;
		Double buyPointBAH,shareNumberBAH,moneyBAH=10000.0, maximumProfitPercent=0.0, maximumLostPercent=0.0;
		Boolean forceSell=false;



		String[][] data = new String[0][];//csv data line count=0 initially
		while ((thisLine = myInput.readLine()) != null) {
			++i;//increment the line count when new line found

			String[][] newdata = new String[i][2];//create new array for data

			String strar[] = thisLine.split(";");//get contents of line as an array
			newdata[i - 1] = strar;//add new line to the array

			System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
			data = newdata;//set new array as csv data
		}



	


		int k=0;
		System.out.println("Start Capital: \\$" + money);
		while(k<data.length-1){

			
			if(Double.valueOf(data[k][1])==1.0){

				buyPoint=Double.valueOf(data[k][0]);
				buyPoint=buyPoint*100;
				shareNumber=(money-1.0)/buyPoint;
				forceSell=false;
				for (int j=k; j<data.length-1; j++) {

					sellPoint=Double.valueOf(data[j][0]);
					sellPoint=sellPoint*100;
					moneyTemp=(shareNumber*sellPoint)-1.0;
					//stop loss %5
					if(money*0.9>moneyTemp){
						money=moneyTemp;
						forceSell=true;
					}
					
					if(Double.valueOf(data[j][1])==2.0 || forceSell==true){
						sellPoint=Double.valueOf(data[j][0]);
						sellPoint=sellPoint*100;
						gain=sellPoint-buyPoint;
						if(gain>0){
							successTransactionCount++;
						}
						else{
							failedTransactionCount++;
						}

						if(gain>=maximumGain){
							maximumGain=gain;
							maximumProfitPercent=Double.valueOf(maximumGain)/Double.valueOf(buyPoint)*100;		
						}
						if(gain<=maximumLost){
							maximumLost=gain;
							maximumLostPercent=Double.valueOf(maximumLost)/Double.valueOf(buyPoint)*100;		
						}
						moneyTemp=(shareNumber*sellPoint)-1.0;
						money=moneyTemp;
						if(money>maximumMoney){
							maximumMoney=money;
						}
						if(money<minimumMoney){
							minimumMoney=money;
						}
						transactionCount++;
						//System.out.println("\\\\"+transactionCount+"."+"("+(k+1)+"-"+(j+1)+") => " + Precision.round(sellPoint,2) + "-" + Precision.round(buyPoint,2)+ "= " + Precision.round(gain,2) + " Capital: \\$" + Precision.round(money,2) );
						System.out.println(transactionCount+"."+"("+(k+1)+"-"+(j+1)+") => " + Precision.round((gain*shareNumber),2) + " Capital: $" + Precision.round(money,2) );
						
						totalPercentProfit=totalPercentProfit +(gain/buyPoint);

						totalTransactionLength=totalTransactionLength+(j-k);
						k=j+1;
						
						totalGain=totalGain+gain;
						break;
					}
					
				}
			}
			
			k++;
		}

		System.out.println("Our System => totalMoney = $" + Precision.round(money,2) );

		buyPointBAH=Double.valueOf(data[0][0]);
		shareNumberBAH=(moneyBAH-1.0)/buyPointBAH;
		moneyBAH=(Double.valueOf(data[data.length-1][0])*shareNumberBAH)-1.0;


		System.out.println("BAH => totalMoney = $" + Precision.round(moneyBAH,2) );

		double numberOfDays=Double.valueOf(data.length-1);
		double numberOfYears=numberOfDays/365;
		//money/10000.0;

		//report the results
		System.out.println("Our System Annualized return % => " + Precision.round(((Math.exp(Math.log(money/10000.0)/numberOfYears)-1)*100),2) + "%" );
		System.out.println("BaH Annualized return % => " +  Precision.round(((Math.exp(Math.log(moneyBAH/10000.0)/numberOfYears)-1)*100),2) +"%");
		System.out.println("Annualized number of transaction => " + Precision.round((Double.valueOf(transactionCount)/numberOfYears),1) + "#");
		System.out.println("Percent success of transaction => " +Precision.round((Double.valueOf(successTransactionCount)/Double.valueOf(transactionCount))*100,2)+ "%");
		System.out.println("Average percent profit per transaction => " + Precision.round((totalPercentProfit/transactionCount*100),2) + "%" );
		System.out.println("Average transaction length => " + totalTransactionLength/transactionCount + "#");
		System.out.println("Maximum profit percent in transaction=> " + Precision.round(maximumProfitPercent,2) + "%");
		System.out.println("Maximum loss percent in transaction=> " + Precision.round(maximumLostPercent,2)+ "%");
		System.out.println("Maximum capital value=> " + "$"+ Precision.round(maximumMoney,2));
		System.out.println("Minimum capital value=> " + "$"+ Precision.round(minimumMoney,2));
		System.out.println("Idle Ratio %=>  " +  Precision.round((Double.valueOf(data.length-totalTransactionLength)/Double.valueOf(data.length)*100),2) + "%");

		
	}
	
	

}

