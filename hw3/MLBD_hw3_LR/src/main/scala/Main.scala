package LRPackage

import breeze.linalg.{DenseMatrix, DenseVector}
import java.util.logging.Level


object Main extends App {

  val logger = Utils.getLogger(
    path = "results/log.txt",
    name="LR"
  )

  logger.log(Level.INFO, "Loading data...")
  val dataTrain: (DenseMatrix[Double], DenseVector[Double]) = Utils.getData(
    dfPath = "../data/X_train.csv",
    targetPath = "../data/y_train.csv"
  )
  val dfTrain: DenseMatrix[Double] = dataTrain._1
  val targetTrain: DenseVector[Double] = dataTrain._2
  logger.log(Level.INFO,"Train data loaded.")
  logger.log(Level.INFO,f"Total train objects ${targetTrain.length}")

  val dataTest: (DenseMatrix[Double], DenseVector[Double]) = Utils.getData(
    dfPath = "../data/X_test.csv",
    targetPath = "../data/y_test.csv"
  )
  val dfTest: DenseMatrix[Double] = dataTest._1
  val targetTest: DenseVector[Double] = dataTest._2
  logger.log(Level.INFO,"Test data loaded.")
  logger.log(Level.INFO,f"Total test objects ${targetTest.length}")

  val linReg = LR(logger)
  logger.log(Level.INFO,"Fitting LR...")
  linReg.fit(df=dfTrain, target=targetTrain)

  logger.log(Level.INFO,"Predicting result...")
  val result: DenseVector[Double] = linReg.predict(dfTest)
  val resultMSE = Utils.getMSE(targetTest, result)
  val resultR2score = Utils.getR2score(targetTest, result)
  logger.log(Level.INFO,f"Final Test MSE = $resultMSE, r2score = $resultR2score")



  logger.log(Level.INFO,"Writing result...")
  Utils.writeData(result, path="results/out.csv")
  logger.log(Level.INFO,"Done!")
}