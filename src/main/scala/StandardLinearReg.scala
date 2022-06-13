import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object StandardLinearReg extends App {

  import org.apache.spark.mllib.linalg.Vectors
  import org.apache.spark.mllib.regression.LabeledPoint

  val sparkSession = SparkSession
    .builder
    .master("local[*]")
    .appName("Dhine")
    .getOrCreate()


  val PREPROCESSED_DATA = "SKAB_5_TRAIN_SHIFTED_PREPROCESSED.txt"
  val PREPROCESSED_TEST_DATA = "SKAB_5_TEST_SHIFTED.txt"

  val sparkContext = sparkSession.sparkContext
  sparkContext.setLogLevel("ERROR")
  val streamingContext = new StreamingContext(sparkContext, batchDuration = Seconds(5))

  val scaleData = textFileToDStream(s"/Users/ivoya/IdeaProjects/spark-stream/data/$PREPROCESSED_DATA", sparkContext, streamingContext)
  scaleData.print()

  val trainingLines = scaleData //streamingContext.socketTextStream("localhost", 9090)
  val testLines = textFileToDStream(s"/Users/ivoya/IdeaProjects/spark-stream/data/$PREPROCESSED_TEST_DATA", sparkContext, streamingContext)
//  val testLines = streamingContext.socketTextStream("0.0.0.0", 9091)

  val trainingData = trainingLines.map(LabeledPoint.parse).cache()
  val testData = testLines.map(LabeledPoint.parse)


  val numFeatures = 5
  val model = new AR(numFeatures)
    .setInitialWeights(Vectors.zeros(numFeatures))

  model.trainOn(trainingData)

  model.predictOnValues(testData.map(lp => model.makeLabeledPointBase(lp))).print(1000)

  streamingContext.start()
  streamingContext.awaitTermination()

  def textFileToDStream(filePath: String, sparkContext: SparkContext, streamingContext: StreamingContext): DStream[String] = {
    val queue = mutable.Queue[RDD[String]]()
    val dstream = streamingContext.queueStream(queue)
    val lines = sparkContext.textFile(filePath)
    queue += lines
    dstream
  }

}

