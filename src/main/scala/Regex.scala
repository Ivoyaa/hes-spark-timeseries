import java.io.{BufferedWriter, File, FileOutputStream, FileWriter, OutputStreamWriter}

object Regex extends App {
  import scala.io.Source

  val FILE_WITH_RAW_DATA = "SKAB_5_TRAIN_SHIFTED.txt"
  val OUTPUT_FILE = "SKAB_5_TRAIN_SHIFTED_PREPROCESSED.txt"

  val source = Source.fromFile(s"/Users/ivoya/IdeaProjects/spark-stream/data/$FILE_WITH_RAW_DATA")
  val txt = (for (line <- source.getLines())
     yield line.replaceAll(",", " ").replaceFirst(" ", ",") + "\n").toArray
  source.close()

  val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(s"/Users/ivoya/IdeaProjects/spark-stream/data/$OUTPUT_FILE"))))
  for (line <- txt) writer.write(line)
  writer.close()
}
