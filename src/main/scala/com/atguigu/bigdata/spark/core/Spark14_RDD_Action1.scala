package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark14_RDD_Action1 {
  def main(args: Array[String]): Unit = {
    //创建RDD
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    //将创建SparkContext对象的程序称为Driver程序
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[Int] = sc.makeRDD(1 to 10 ,2)

    // 算子中的逻辑操作在Excutor中执行，所以需要考虑分区内和分区间的操作
    // 0.行动算子 - collect
    //将结果收集到Driver端
//    rdd.collect().foreach(println)

    // 1.行动算子 - reduce
//    val i: Int = rdd.reduce(_+_)

    // 2.行动算子 - count
//    val i: Long = rdd.count()

    // 3.行动算子 - first
//    println(rdd.first())

    // 4.行动算子 - take
//    println(rdd.take(3))

    // 5.行动算子 - takeOrdered(3)
    println(rdd.takeOrdered(3).foreach(println))

//    println("i = " + i)
  }
}
