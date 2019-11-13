package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Transform {
  def main(args: Array[String]): Unit = {


    //创建RDD
    //创建sparkContext上下文对象
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)

    //构建RDD
    val numRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4))


    /*
    //RDD转换算子--》map
//    val mapRDD: RDD[Int] = numRDD.map((x)=>x*2)
    val mapRDD: RDD[Int] = numRDD.map(_*2)

    mapRDD.collect().foreach(println)
    */

    /*
    //RDD转换算子 --- mapPartitions
    val mapPartitionsRDD: RDD[Int] = numRDD.mapPartitions(datas=>datas.map(_*2))

    mapPartitionsRDD.collect().foreach(println)

    */

    //RDD转换算子 --- mapPartitionsWithIndex
    val mapPartitionsWithIndexRDD: RDD[(Int, Int)] = numRDD.mapPartitionsWithIndex {
      (index, datas) => {
        datas.map((index, _))
      }
    }

    mapPartitionsWithIndexRDD.collect().foreach(println)

  }
}
