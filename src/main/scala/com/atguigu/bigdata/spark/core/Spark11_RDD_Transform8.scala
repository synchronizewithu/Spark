package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark11_RDD_Transform8 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val kvRDD = sc.parallelize(Array((3,"aa"),(6,"cc"),(2,"bb"),(1,"dd")))

    //转换算子 -sortByKey
//    val rdd: RDD[(Int, String)] = kvRDD.sortByKey(false)
//    rdd.collect().foreach(println)

    //转换算子 - mapValues
    val rdd: RDD[(Int, String)] = kvRDD.mapValues(str=>str+"|||||")
    rdd.collect().foreach(println)

    // 转换算子 - join(相同key聚合,将value连接在一起：(key, (v1, v2))  )


  }
}
