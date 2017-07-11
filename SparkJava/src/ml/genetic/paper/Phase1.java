package ml.genetic.paper;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.joda.time.DateTime;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.CMOIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.VolumeIndicator;
import eu.verdelhan.ta4j.indicators.statistics.CovarianceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import eu.verdelhan.ta4j.indicators.trackers.ROCIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.WMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.WilliamsRIndicator;

public class Phase1 {

	public final static int WINDOW_SIZE=11; //Sh 6
	//public final static int WINDOW_SIZE=21; //Sh 11
	//public final static int WINDOW_SIZE=41; //Sh 22

	public static Double previousCloseValue=0.0;
	public static Double currentCloseValue=0.0;
	public static int counterSell=0;
	public static int counterBuy=0;
	public static int counterRow=0;
	public static DecimalFormat decim = new DecimalFormat("#.##");
	public static List<Tick> ticksList=new ArrayList<Tick>();
	public static TimeSeries timeSeries;
	public static RSIIndicator rsi1,rsi2,rsi3,rsi4,rsi5,rsi6,rsi7,rsi8,rsi9,rsi10,rsi11,rsi12,rsi13,rsi14,rsi15,rsi16,rsi17,rsi18,rsi19,rsi20;
	public static WilliamsRIndicator wr ;
	public static EMAIndicator ema;
	public static SMAIndicator sma50,sma200;
	public static VolumeIndicator volumeIndicator;
	public static CMOIndicator cmo;
	public static CovarianceIndicator covar;
	public static MACDIndicator macd;
	public static ROCIndicator roc;
	public static WMAIndicator wma;

	public static List<Double> closeList=new ArrayList<Double>();
	public static int windowBeginIndex=0, windowEndIndex=0, windowMiddleIndex=0, minIndex=0, maxIndex=0;
	public static String result="Yok";
	public static Double label=0.0;
	public static Double min = 10000.0;
	public static Double max = 0.0;
	public static Double number = 0.0;


	public static class SharePrice implements Serializable {
		private String date;
		private Double open;
		private Double high;
		private Double low;
		private Double close;
		private Double volume;
		private Double adjClose;

		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public Double getOpen() {
			return open;
		}
		public void setOpen(Double open) {
			this.open = open;
		}
		public Double getHigh() {
			return high;
		}
		public void setHigh(Double high) {
			this.high = high;
		}
		public Double getLow() {
			return low;
		}
		public void setLow(Double low) {
			this.low = low;
		}
		public Double getClose() {
			return close;
		}
		public void setClose(Double close) {
			this.close = close;
		}
		public Double getVolume() {
			return volume;
		}
		public void setVolume(Double volume) {
			this.volume = volume;
		}
		public Double getAdjClose() {
			return adjClose;
		}
		public void setAdjClose(Double adjClose) {
			this.adjClose = adjClose;
		}
	}
	
	public static void PhaseProcess() throws Exception{
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local[*]");
		sparkConf.setAppName("DL4J Spark MLP");

		SparkSession spark = SparkSession
				.builder()
				.appName("Java Spark SQL basic example")
				.config(sparkConf)
				.getOrCreate();

		runPhase1(spark);

		spark.stop();
	}

	public static void main(String[] args) throws AnalysisException {

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local[*]");
		sparkConf.setAppName("DL4J Spark MLP");

		SparkSession spark = SparkSession
				.builder()
				.appName("Java Spark SQL basic example")
				.config(sparkConf)
				.getOrCreate();

		runPhase1(spark);

		spark.stop();
	}

	private static void runPhase1(SparkSession spark) {

		JavaRDD<SharePrice> sharePriceRDD = spark.read()
				.textFile("resources2/reverseFile.csv")
				.javaRDD()
				.map(new Function<String, SharePrice>() {
					public SharePrice call(String line) throws Exception {
						String[] parts = line.split(",");
						SharePrice sharePrice=new SharePrice();
						sharePrice.setDate(parts[0]);
						Double AdjOpen=Double.parseDouble(parts[1].trim())*(Double.parseDouble(parts[6].trim())/Double.parseDouble(parts[4].trim()));
						//sharePrice.setOpen(Double.parseDouble(parts[1].trim()));
						sharePrice.setOpen(AdjOpen);

						Double AdjHigh=Double.parseDouble(parts[2].trim())*(Double.parseDouble(parts[6].trim())/Double.parseDouble(parts[4].trim()));
						//sharePrice.setHigh(Double.parseDouble(parts[2].trim()));
						sharePrice.setHigh(AdjHigh);

						Double AdjLow=Double.parseDouble(parts[3].trim())*(Double.parseDouble(parts[6].trim())/Double.parseDouble(parts[4].trim()));
						//sharePrice.setLow(Double.parseDouble(parts[3].trim()));
						sharePrice.setLow(AdjLow);

						Double AdjClose=Double.parseDouble(parts[6].trim());
						//sharePrice.setClose(Double.parseDouble(parts[4].trim()));
						sharePrice.setClose(AdjClose);

						sharePrice.setVolume(Double.parseDouble(parts[5].trim()));

						sharePrice.setAdjClose(Double.parseDouble(parts[6].trim()));

						String year=sharePrice.getDate().split("-")[0];
						String month=sharePrice.getDate().split("-")[1];
						String day=sharePrice.getDate().split("-")[2];

						DateTime dt = new DateTime(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day),12, 0, 0, 0);
						ticksList.add(new Tick(dt,Decimal.valueOf(sharePrice.getOpen()),Decimal.valueOf(sharePrice.getHigh()),Decimal.valueOf(sharePrice.getLow()),Decimal.valueOf(sharePrice.getClose()),Decimal.valueOf(sharePrice.getVolume())));
						return sharePrice;
					}
				});
		timeSeries=new TimeSeries(ticksList);
		rsi1 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 1);
		rsi2 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 2);
		rsi3 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 3);
		rsi4 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 4);
		rsi5 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 5);
		rsi6 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 6);
		rsi7 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 7);
		rsi8 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 8);
		rsi9 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 9);
		rsi10 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 10);
		rsi11 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 11);
		rsi12 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 12);
		rsi13 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 13);
		rsi14 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 14);
		rsi15 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 15);
		rsi16 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 16);
		rsi17 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 17);
		rsi18 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 18);
		rsi19 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 19);
		rsi20 = new RSIIndicator(new ClosePriceIndicator(timeSeries), 20);
		sma50= new SMAIndicator(new ClosePriceIndicator(timeSeries), 50);
		sma200= new SMAIndicator(new ClosePriceIndicator(timeSeries), 200);
		
		/*wr = new WilliamsRIndicator(new ClosePriceIndicator(timeSeries), 10, new MaxPriceIndicator(timeSeries),
				new MinPriceIndicator(timeSeries));
		ema = new EMAIndicator(new ClosePriceIndicator(timeSeries), 10);
		volumeIndicator = new VolumeIndicator(timeSeries, 5);
		cmo = new CMOIndicator(new ClosePriceIndicator(timeSeries), 9);
		covar = new CovarianceIndicator(new ClosePriceIndicator(timeSeries), new VolumeIndicator(timeSeries, 5), 5);
		roc = new ROCIndicator(new ClosePriceIndicator(timeSeries), 12);
		macd = new MACDIndicator(new ClosePriceIndicator(timeSeries), 12,28);
		wma = new WMAIndicator(new ClosePriceIndicator(timeSeries), 9);*/
		// Apply a schema to an RDD of JavaBeans to get a DataFrame
		Dataset<Row> sharePriceDF = spark.createDataFrame(sharePriceRDD, SharePrice.class);

		// Register the DataFrame as a temporary view
		sharePriceDF.createOrReplaceTempView("sharePrice");
		//sharePriceDF.show();

		// SQL statements can be run by using the sql methods provided by spark
		Dataset<Row> sharePriceCloseDF = spark.sql("SELECT date,close FROM sharePrice");

		// The columns of a row in the result can be accessed by field index
		Encoder<String> stringEncoder = Encoders.STRING();

		closeList.add(currentCloseValue);
		Dataset<String> sharePriceCloseByIndexDF = sharePriceCloseDF.map(new MapFunction<Row, String>() {
			public String call(Row row) throws Exception {
				currentCloseValue=row.<Double>getAs("close");
				closeList.add(currentCloseValue);
				counterRow++;
				result="--";

				System.out.println("counterRow:" + counterRow);
			



				//System.out.println("Label | " + " Close | "  +  " RSI | " +  " WilliamR | " +  " EMA | " +  " CMO " +  " COVAR ");
				//return   decim.format(label) + ";"  +   Precision.round((row.<Double>getAs("close")/100),4) + ";"  + Precision.round((rsi.getValue(counterRow-1).toDouble()/100),4) + ";" + Precision.round((wr.getValue(counterRow-1).toDouble()/100),4)+ ";" + Precision.round((macd.getValue(counterRow-1).toDouble()),4);
				return   Precision.round((row.<Double>getAs("close")),4) + ";"  + Precision.round((rsi1.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi2.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi3.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi4.getValue(counterRow-1).toDouble()),0) + ";" 
				+ Precision.round((rsi5.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi6.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi7.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi8.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi9.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi10.getValue(counterRow-1).toDouble()),0) + ";"  
				+ Precision.round((rsi11.getValue(counterRow-1).toDouble()),0) + ";" + Precision.round((rsi12.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi13.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi14.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi15.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi16.getValue(counterRow-1).toDouble()),0) + ";"
				+ Precision.round((rsi17.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi18.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi19.getValue(counterRow-1).toDouble()),0) + ";"+ Precision.round((rsi20.getValue(counterRow-1).toDouble()),0)+ ";"+ Precision.round((sma50.getValue(counterRow-1).toDouble()),2)+ ";"+ Precision.round((sma200.getValue(counterRow-1).toDouble()),2);
				
			}
		}, stringEncoder);


		//sharePriceCloseByIndexDF.show();

		sharePriceCloseByIndexDF.write().csv("resources2/output.csv");

	}
}
