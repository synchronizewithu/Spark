package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD {
  def main(args: Array[String]): Unit = {

    //创建RDD
    //创建sparkContext上下文对象
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //1.从内存中创建RDD
    //1.
//    val numRDD: RDD[Int] = sc.parallelize(List(1,2,3,4))
    //2.makeRDD底层调用的就是parallelize方法
//    val numRDD1: RDD[Int] = sc.makeRDD(List(1,2,3,4))
    //makeRDD的第二个参数表示并行度，其实指的就是分区的数量
    val numRDD1: RDD[Int] = sc.makeRDD(List(1,2,3,4),3)

//    numRDD.collect().foreach(println)
    //numRDD1.collect().foreach(println)
    numRDD1.saveAsTextFile("output")


    //2.从存储系统中创建
    //默认情况下，路径为相对路径，指向项目的根路径
    //textfile可以设定文件的路径，读取数据时可以和hadoop读取文件一致
    //第二个参数表示最小的分区数，可以大于这个数
//    val fileRDD: RDD[String] = sc.textFile("input/3.txt",2)

//    fileRDD.collect().foreach(println)
//    fileRDD.saveAsTextFile("output")

  }
}
