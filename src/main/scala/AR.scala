import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.regression.{LabeledPoint, StreamingLinearRegressionWithSGD}

import scala.collection.mutable.ListBuffer

class AR(p: Int) extends StreamingLinearRegressionWithSGD {
  var featuresHistoric: ListBuffer[Double] = ListBuffer.empty

  var currentLp: (Double, Vector) = (0.0, Vectors.zeros(p))

  def makeLabeledPointBase(lp: LabeledPoint): (Double, Vector) = {
    println(featuresHistoric)
    this.addToFeatures(lp.features.toArray.head)
    if (this.featuresHistoric.size < p) {
      (lp.label, Vectors.zeros(p))
    }
    else {
      (lp.label, Vectors.dense(this.featuresHistoric.toArray))
    }

  }

  def addToFeatures(v: Double): ListBuffer[Double] =
    if (featuresHistoric.size >= p) {
      featuresHistoric -= featuresHistoric.head
      featuresHistoric += v
    }
    else featuresHistoric += v
}
