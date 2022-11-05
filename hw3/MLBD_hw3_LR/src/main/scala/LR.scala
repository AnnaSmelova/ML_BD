package LRPackage

import breeze.linalg.{DenseMatrix, DenseVector, inv}
import java.util.logging.{Level, Logger}


case class LR(Logger: Logger){
  var logger: Logger = Logger
  var weights: DenseVector[Double] = DenseVector.fill(1)(0)

  def fit(df: DenseMatrix[Double], target: DenseVector[Double]): Unit = {
    val ones = DenseMatrix.fill[Double](df.rows, cols = 1)(1)
    val X = DenseMatrix.horzcat(ones, df)

    val splittedData = Utils.splitData(X, target, true, 0.8)
    val trainX = splittedData._1
    val trainTarget = splittedData._3
    val valX = splittedData._2
    val valTarget = splittedData._4

    logger.log(Level.INFO,f"Train data length ${trainTarget.length}")
    logger.log(Level.INFO,f"Validation data length ${valTarget.length}")

    weights = inv(trainX.t * trainX) * trainX.t * trainTarget

    val mse = Utils.getMSE(valTarget, valX * weights)
    val r2score = Utils.getR2score(valTarget, valX * weights)
    logger.log(Level.INFO,  f"Validation MSE=$mse, r2score=$r2score")
  }

  def predict(df: DenseMatrix[Double]): DenseVector[Double] = {
    val ones = DenseMatrix.fill[Double](df.rows, cols = 1)(1)
    val X = DenseMatrix.horzcat(ones, df)
    X * weights
  }
}