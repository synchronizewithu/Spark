package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark19_RDD_CheckPoint {
  def main(args: Array[String]): Unit = {
    //创建RDD
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    //将创建SparkContext对象的程序称为Driver程序
    val sc = new SparkContext(sparkConf)

    sc.setCheckpointDir("cp")

    val rdd: RDD[String] = sc.makeRDD(Array("atguigu"))

    val time: RDD[String] = rdd.map(_+System.currentTimeMillis())


    // cache方法不会改变RDD的血缘关系
    // checkpoint会改变RDD的血缘关系,因为检查点需要保证不会删除，这样，数据不会丢失，当然就可以改变血缘关系
    time.checkpoint()

    //第一次触发检查点的执行
    time.collect().foreach(println)
    time.collect().foreach(println)
    time.collect().foreach(println)
    time.collect().foreach(println)

  }
}
