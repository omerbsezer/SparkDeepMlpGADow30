package ml.genetic.paper;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.ml.tuning.TrainValidationSplit;
import org.apache.spark.ml.tuning.TrainValidationSplitModel;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Phase3 {
	
	public static void PhaseProcess() throws Exception{
		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local[*]");
		sparkConf.setAppName("DL4J Spark MLP");


		SparkSession spark = SparkSession
				.builder()
				.appName("JavaMultilayerPerceptronClassifierExample").config(sparkConf)
				.getOrCreate();



		String pathTrain = "resources2/GATableListTraining.txt";
		Dataset<Row> dataFrameTrain = spark.read().format("libsvm").load(pathTrain);

		String pathTest = "resources2/GATableListTest.txt";
		Dataset<Row> dataFrameTest = spark.read().format("libsvm").load(pathTest);
		

		Dataset<Row> train = dataFrameTrain;
		Dataset<Row> test = dataFrameTest;

		// specify layers for the neural network:
		// input layer of size 4 (features), two intermediate of size 5 and 4
		// and output of size 3 (classes)
		int[] layers = new int[] {3, 20, 10, 8, 6, 5, 3};
		
		//test other layers
		//int[] layers = new int[] {5, 10, 15, 5, 3};
		//int[] layers = new int[] {3, 10, 10, 30, 10, 10, 3};
		
		// create the trainer and set its parameters
		MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
		.setLayers(layers)
		.setBlockSize(128)
		.setSeed(1234L)
		//.setTol(1E-8)
		.setMaxIter(200);

		
		////////////////////////////
		
		/*ParamMap[] paramGrid = new ParamGridBuilder()
		  .addGrid(trainer.maxIter(), new int[] {10,15,20,25,50})
		  .build();
		
		
		CrossValidator cv = new CrossValidator()
		  .setEstimator(trainer)
		  .setEvaluator(new MulticlassClassificationEvaluator())
		  .setEstimatorParamMaps(paramGrid).setNumFolds(2);
		
		
		CrossValidatorModel model = cv.fit(train);
		
		TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
		  .setEstimator(trainer)
		  .setEvaluator(new MulticlassClassificationEvaluator())
		  .setEstimatorParamMaps(paramGrid)
		  .setTrainRatio(0.75);  // 80% for training and the remaining 20% for validation

		// Run train validation split, and choose the best set of parameters.
		TrainValidationSplitModel model = trainValidationSplit.fit(train);*/
		//////////////////////////////
		
		// train the model
		MultilayerPerceptronClassificationModel model = trainer.fit(train);

		// compute accuracy on the test set
		Dataset<Row> result = model.transform(test);
		//result.show();
		Dataset<Row> predictionAndLabels = result.select("prediction", "label");
		//predictionAndLabels.show();

		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
		.setMetricName("accuracy");

		System.out.println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels));
		// $example off$
		// Confusion matrix

		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels);
		Matrix confusion = metrics.confusionMatrix();
		System.out.println("Confusion matrix: \n" + confusion);

		// Overall statistics
		System.out.println("Accuracy = " + metrics.accuracy());

		// Stats by labels
		for (int i = 0; i < metrics.labels().length; i++) {
			System.out.format("Class %f precision = %f\n", metrics.labels()[i],metrics.precision(
					metrics.labels()[i]));
			System.out.format("Class %f recall = %f\n", metrics.labels()[i], metrics.recall(
					metrics.labels()[i]));
			System.out.format("Class %f F1 score = %f\n", metrics.labels()[i], metrics.fMeasure(
					metrics.labels()[i]));
		}

		//Weighted stats
		System.out.format("Weighted precision = %f\n", metrics.weightedPrecision());
		System.out.format("Weighted recall = %f\n", metrics.weightedRecall());
		System.out.format("Weighted F1 score = %f\n", metrics.weightedFMeasure());
		System.out.format("Weighted false positive rate = %f\n", metrics.weightedFalsePositiveRate());


		// Register the DataFrame as a temporary view
		result.createOrReplaceTempView("result");



		Dataset<Row> resultDF = spark.sql("SELECT * FROM result");
		resultDF.show();

		// The columns of a row in the result can be accessed by field index
		Encoder<String> stringEncoder = Encoders.STRING();

		Dataset<String> resultIndexDF = resultDF.map(new MapFunction<Row, String>() {
			public String call(Row row) throws Exception {


				//Precision.round((row.<Double>getAs("close")/100),2);
				return row.<Double>getAs("label") + ";" + row.<SparseVector>getAs("features") +";" +row.<Double>getAs("prediction");
				//return "result: " + row.<Double>getAs("label") + " " +row.<Double>getAs("prediction");
			}
		}, stringEncoder);

		resultIndexDF.write().csv("resources2/outputMLP.csv");

		spark.stop();
	}
	
	/*public static void main(String[] args) {

		SparkConf sparkConf = new SparkConf();
		sparkConf.setMaster("local[*]");
		sparkConf.setAppName("DL4J Spark MLP");


		SparkSession spark = SparkSession
				.builder()
				.appName("JavaMultilayerPerceptronClassifierExample").config(sparkConf)
				.getOrCreate();



		String pathTrain = "resources2/GATableListTraining.txt";
		Dataset<Row> dataFrameTrain = spark.read().format("libsvm").load(pathTrain);

		String pathTest = "resources2/GATableListTest.txt";
		Dataset<Row> dataFrameTest = spark.read().format("libsvm").load(pathTest);
		
		// Random split. Split the data into train and test
		//Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
		//Dataset<Row> train = splits[0];
		//Dataset<Row> test = splits[1];

		Dataset<Row> train = dataFrameTrain;
		Dataset<Row> test = dataFrameTest;

		// specify layers for the neural network:
		// input layer of size 4 (features), two intermediate of size 5 and 4
		// and output of size 3 (classes)
		int[] layers = new int[] {3, 20, 10, 8, 6, 5, 3};
		//test other layers
		//int[] layers = new int[] {5, 10, 15, 5, 3};
		//int[] layers = new int[] {3, 10, 10,30,10,10, 3};
		//int[] layers = new int[] {4, 60, 60, 60, 60,60, 60, 60, 60,60, 60, 60, 60, 3};

		// create the trainer and set its parameters
		MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier()
		.setLayers(layers)
		.setBlockSize(128)
		.setSeed(1234L)
		//.setTol(1E-6)
		.setMaxIter(200);

		// train the model
		MultilayerPerceptronClassificationModel model = trainer.fit(train);

		// compute accuracy on the test set
		Dataset<Row> result = model.transform(test);
		//result.show();
		Dataset<Row> predictionAndLabels = result.select("prediction", "label");
		//predictionAndLabels.show();

		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
		.setMetricName("accuracy");

		System.out.println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels));
		// $example off$
		// Confusion matrix

		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels);
		Matrix confusion = metrics.confusionMatrix();
		System.out.println("Confusion matrix: \n" + confusion);

		// Overall statistics
		System.out.println("Accuracy = " + metrics.accuracy());

		// Stats by labels
		for (int i = 0; i < metrics.labels().length; i++) {
			System.out.format("Class %f precision = %f\n", metrics.labels()[i],metrics.precision(
					metrics.labels()[i]));
			System.out.format("Class %f recall = %f\n", metrics.labels()[i], metrics.recall(
					metrics.labels()[i]));
			System.out.format("Class %f F1 score = %f\n", metrics.labels()[i], metrics.fMeasure(
					metrics.labels()[i]));
		}

		//Weighted stats
		System.out.format("Weighted precision = %f\n", metrics.weightedPrecision());
		System.out.format("Weighted recall = %f\n", metrics.weightedRecall());
		System.out.format("Weighted F1 score = %f\n", metrics.weightedFMeasure());
		System.out.format("Weighted false positive rate = %f\n", metrics.weightedFalsePositiveRate());


		// Register the DataFrame as a temporary view
		result.createOrReplaceTempView("result");



		Dataset<Row> resultDF = spark.sql("SELECT * FROM result");
		resultDF.show();

		// The columns of a row in the result can be accessed by field index
		Encoder<String> stringEncoder = Encoders.STRING();

		Dataset<String> resultIndexDF = resultDF.map(new MapFunction<Row, String>() {
			public String call(Row row) throws Exception {


				//Precision.round((row.<Double>getAs("close")/100),2);
				return row.<Double>getAs("label") + ";" + row.<SparseVector>getAs("features") +";" +row.<Double>getAs("prediction");
				//return "result: " + row.<Double>getAs("label") + " " +row.<Double>getAs("prediction");
			}
		}, stringEncoder);

		resultIndexDF.write().csv("resources2/outputMLP1.csv");

		spark.stop();
	}*/
}
