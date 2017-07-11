package ml.genetic.paper;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.util.Precision;

public class FitnessCalcScenario {
	
	public static Double buyPoint,sellPoint,gain,totalGain, money, shareNumber, moneyTemp, maximumMoney, minimumMoney,maximumGain, maximumLost, totalPercentProfit;
	public static int transactionCount=0;
	public static Double sma50,sma200,trend;
	public static Boolean forceSell=false;
	
	int  successTransactionCount=0,failedTransactionCount=0;

	
	public static int calculateFitness(Chromosome chromosome)  {
		buyPoint=0.0;sellPoint=0.0;gain=0.0;totalGain=0.0;shareNumber=0.0; moneyTemp=0.0; maximumMoney=0.0; minimumMoney=10000.0;maximumGain=0.0; maximumLost=100.0; totalPercentProfit=0.0;
		money=10000.0;
		String fName = "resources2/outputOfRSI.csv";
		String thisLine; 
		int count=0; 
		FileInputStream fis;
		String[][] data = new String[0][];

		try {
			fis = new FileInputStream(fName);
			DataInputStream myInput = new DataInputStream(fis);
			int i=0; 



			while ((thisLine = myInput.readLine()) != null) {
				++i;//increment the line count when new line found

				String[][] newdata = new String[i][2];//create new array for data

				String strar[] = thisLine.split(";");//get contents of line as an array
				newdata[i - 1] = strar;//add new line to the array

				System.arraycopy(data, 0, newdata, 0, i - 1);//copy previously read values to new array
				data = newdata;//set new array as csv data
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		//System.out.println("------------------------------"); //debug print array secondData


		int k=0;
		/*chromosome.getGene(0);//RSIBuyValue
		chromosome.getGene(1);//RSIBuyInterval
		chromosome.getGene(2);//RSISellValue
		chromosome.getGene(3);//RSISellInterval
		 */
		while(k<data.length-1){
			
			sma50=Double.valueOf(data[k][21]);
			sma200=Double.valueOf(data[k][22]);
			
			trend=sma50-sma200;
			if(trend>0){ //upTrend
				//BuyPoint
				if(Double.valueOf(data[k][chromosome.getGene(5)])<=Double.valueOf(chromosome.getGene(4))){

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

						if(Double.valueOf(data[j][chromosome.getGene(7)])>=Double.valueOf(chromosome.getGene(6))|| forceSell==true){
							sellPoint=Double.valueOf(data[j][0]);
							sellPoint=sellPoint*100;
							gain=sellPoint-buyPoint;
							
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
							
							totalPercentProfit=totalPercentProfit +(gain/buyPoint);

							
							k=j+1;
							totalGain=totalGain+gain;
							break;
						}
					}
				}
			}else { //downTrend
				//BuyPoint
				if(Double.valueOf(data[k][chromosome.getGene(1)])<=Double.valueOf(chromosome.getGene(0))){

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
					

						if(Double.valueOf(data[j][chromosome.getGene(3)])>=Double.valueOf(chromosome.getGene(2))){
							sellPoint=Double.valueOf(data[j][0]);
							sellPoint=sellPoint*100;
							gain=sellPoint-buyPoint;
							
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
							
							totalPercentProfit=totalPercentProfit +(gain/buyPoint);

							
							k=j+1;
							totalGain=totalGain+gain;
							break;
						}
					}
				}
			}
			

			k++;
		}

		return money.intValue();

	}


}
