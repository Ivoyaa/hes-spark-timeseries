import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}

object PredictionsFormatter extends App{
  import scala.io.Source

  val FILE_WITH_PREDICTIONS = "skab_prediction_5.txt"
  val TARGET_FILE = "skab_predictions_5_form.txt"
  val source = Source.fromFile(s"/Users/ivoya/IdeaProjects/spark-stream/data/$FILE_WITH_PREDICTIONS")
  val txt = (for (line <- source.getLines())
    yield line.replaceAll("\\(", "").replaceAll("\\)", "") + "\n").toArray
  source.close()

  val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(s"/Users/ivoya/IdeaProjects/spark-stream/data/$TARGET_FILE"))))
  for (line <- txt) writer.write(line)
  writer.close()
}
