package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark16_RDD_Action3 {
  def main(args: Array[String]): Unit = {
    //创建RDD
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    //将创建SparkContext对象的程序称为Driver程序
    val sc = new SparkContext(sparkConf)

    val numRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4),2)
    val kvRDD: RDD[(String, Int)] = sc.makeRDD(List( ("a",1),("b",2) ))

    // 存储的行动算子 - saveAsTextFile，saveAsSequenceFile，saveAsObjectFile
    numRDD.saveAsTextFile("output")
    kvRDD.saveAsSequenceFile("output1")
    numRDD.saveAsObjectFile("output2")


    //


  }
}
