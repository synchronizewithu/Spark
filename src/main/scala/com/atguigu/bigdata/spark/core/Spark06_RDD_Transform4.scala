package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_Transform4 {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)
    //构建RDD
    //    val numRDD: RDD[Int] = sc.makeRDD(List(5, 7, 6, 3, 4, 1, 2))
    val numRDD = sc.makeRDD(List(("a", 1), ("c", 3), ("d", 4), ("b", 2)))

    //传递什么值就用什么值排序
    //    val sortByRDD: RDD[Int] = numRDD.sortBy(x=>x,false)
    val sortByRDD: RDD[(String, Int)] = numRDD.sortBy(t => t._2)

    sortByRDD.collect().foreach(println)


  }
}
