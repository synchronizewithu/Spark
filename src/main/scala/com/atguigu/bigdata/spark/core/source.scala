package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object source {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(sparkConf)

    val listRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4))

    listRDD.collect().foreach(println)
  }
}
