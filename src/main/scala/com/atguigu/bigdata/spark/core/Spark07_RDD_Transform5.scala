package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark07_RDD_Transform5 {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)
    //构建RDD
    //    val numRDD: RDD[Int] = sc.makeRDD(List(5, 7, 6, 3, 4, 1, 2))
    val numRDD1: RDD[Int] = sc.makeRDD(List(1,2,3,4),3)
    val numRDD2: RDD[Int] = sc.makeRDD(List(4,5,6,7),4)

    //双value的RDD操作
    //并集 ： 相同的元素不去重
//    val rdd: RDD[Int] = numRDD1.union(numRDD2)

    //差集
//    val rdd: RDD[Int] = numRDD1.subtract(numRDD2)

    //交集
//    val rdd: RDD[Int] = numRDD1.intersection(numRDD2)

    // 乘积:笛卡尔积
//    val rdd: RDD[(Int, Int)] = numRDD1.cartesian(numRDD2)

//    rdd.collect().foreach(println)

    //RDD拉链
    // 1.每一个分区的数据量相同
    // 2.拉链的两个RDD的分区应该相同
    val rdd: RDD[(Int, Int)] = numRDD1.zip(numRDD2)
    rdd.collect().foreach(println)
  }
}
