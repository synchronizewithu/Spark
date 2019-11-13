package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark17_RDD_Action4 {
  def main(args: Array[String]): Unit = {
    //创建RDD
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    //将创建SparkContext对象的程序称为Driver程序
    val sc = new SparkContext(sparkConf)

    val kvRDD = sc.makeRDD(List(("a",1),("a",1),("a",1),("a",1)))

    // 行动算子 - countByKey
    kvRDD.countByKey().foreach(println) //(a,4)


  }
}
