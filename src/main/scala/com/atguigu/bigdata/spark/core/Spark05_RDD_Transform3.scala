package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Transform3 {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)
    //构建RDD
    val numRDD: RDD[Int] = sc.makeRDD(1 to 16, 4)


    /*
    //合并分区
    //转换算子 - coalesce （合并分区）
    // 不采用shuffle操作，减少了一个分区，将最后两个分区合并，可能导致数据倾斜
//    val coalesceRDD: RDD[Int] = numRDD.coalesce(3)

    // 合并时，采用shuffle操作: 数据将打散到其他的分区中
    val coalesceRDD: RDD[Int] = numRDD.coalesce(3,true)

    val result: RDD[(Int, Int)] = coalesceRDD.mapPartitionsWithIndex {
      (index, datas) => {
        datas.map((index, _))
      }
    }
    result.collect().foreach(println)
    */

    // 转换算子 - repartition （重分区）
    // 从源码的角度上讲，repartition实际上就是coalesce的shuffle机制
    val repartitionRDD: RDD[Int] = numRDD.repartition(3)


    val result: RDD[(Int, Int)] = repartitionRDD.mapPartitionsWithIndex {
      (index, datas) => {
        datas.map((index, _))
      }
    }
    result.collect().foreach(println)
  }
}
