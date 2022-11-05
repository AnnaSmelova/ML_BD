package LRPackage

import breeze.linalg.{DenseMatrix, DenseVector, csvread, sum}
import breeze.numerics.pow
import breeze.stats.mean
import scala.util.Random
import java.io.{File, PrintWriter}
import java.util.logging.{FileHandler, Logger, SimpleFormatter}

object Utils {

  def getData(dfPath: String, targetPath: String): (DenseMatrix[Double], DenseVector[Double]) = {
    val df: DenseMatrix[Double] = csvread(new File(dfPath), separator = ',', skipLines = 1).toDenseMatrix
    val target: DenseVector[Double] = csvread(new File(targetPath), separator = ',', skipLines = 1).toDenseVector

    (df, target)
  }

  def splitData(df: DenseMatrix[Double], target: DenseVector[Double], shuffle: Boolean, splitSize: Double): (
    DenseMatrix[Double], DenseMatrix[Double], DenseVector[Double], DenseVector[Double]
    ) = {
    val splitId = (splitSize * target.length).toInt

    var ids = Array.range(0, target.length)
    if (shuffle) {
      ids = Random.shuffle(ids).toArray
    }

    val trainIds = ids.slice(0, splitId).toIndexedSeq
    val dfTrain = df(trainIds, ::).toDenseMatrix
    val targetTrain = target(trainIds).toDenseVector

    val valIds = ids.slice(splitId + 1, target.length).toIndexedSeq
    val dfVal = df(valIds, ::).toDenseMatrix
    val targetVal = target(valIds).toDenseVector

    (dfTrain, dfVal, targetTrain, targetVal)
  }

  def writeData(result: DenseVector[Double], path: String): Unit = {
    val outFile = new PrintWriter(
      new File(path)
    )
    result
      .foreach(vec =>
        outFile.write(vec.toString.drop(1).dropRight(1) + "\n")
      )
    outFile.close()
  }

  def getLogger(path: String, name: String): Logger = {
    val logger = Logger.getLogger(name)
    val handler = new FileHandler(path)
    val formatter = new SimpleFormatter()
    handler.setFormatter(formatter)
    logger.addHandler(handler)
    logger
  }

  def getMSE(targetTrue: DenseVector[Double], targetPred: DenseVector[Double]): Double = {
    sum(pow(targetTrue - targetPred, 2)) / targetTrue.length
  }

  def getR2score(targetTrue: DenseVector[Double], targetPred: DenseVector[Double]): Double = {
    val numerator = sum(pow((targetTrue - targetPred), 2))
    val denominator = sum(pow((targetTrue - mean(targetTrue)),2))
    1 - (numerator / denominator)
  }
}