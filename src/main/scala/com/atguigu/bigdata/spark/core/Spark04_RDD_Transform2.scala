package com.atguigu.bigdata.spark.core

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Spark04_RDD_Transform2 {
  def main(args: Array[String]): Unit = {


    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)

    //构建RDD
    val numRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 4, 5))

    /*
    //转换算子 - sample（ 抽样 ）
    //    val sampleRDD: RDD[Int] = numRDD.sample(false,0.5)
    val sampleRDD: RDD[Int] = numRDD.sample(true, 0.4)

    sampleRDD.collect().foreach(println)
    */


    //转换算子 - distinct （ 去重 ）
    //数据在去重后会打乱重组
    val distinctRDD: RDD[Int] = numRDD.distinct(3) //指定并行度为3
    distinctRDD.collect().foreach(println)


  }
}
