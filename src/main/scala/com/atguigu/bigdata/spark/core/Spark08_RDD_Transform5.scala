package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

//key-value类型的RDD
object Spark08_RDD_Transform5 {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)


    val rdd = sc.parallelize(Array((1,"aaa"),(2,"bbb"),(3,"ccc"),(4,"ddd")),4)

//    sc.textFile()
//    rdd.reduceByKey()

    //转换算子 - partitionBy - （分区，分区器）
    // 分区器实现
    // 1. 继承抽象类Partitioner
    // 2. 重写抽象方法：numPartitions（获取分区数量），getPartition（根据key计算分区号）
//    val partitonByRDD: RDD[(Int, String)] = rdd.partitionBy(new org.apache.spark.HashPartitioner(2))
    val partitonByRDD: RDD[(Int, String)] = rdd.partitionBy(new MyPartitioner(2))

    val mapPartitionsWithIndexRDD: RDD[(Int, (Int, String))] = partitonByRDD.mapPartitionsWithIndex {
      (index, datas) => {
        datas.map((index, _))
      }
    }

    mapPartitionsWithIndexRDD.collect().foreach(println)


  }
}

//自定以分区器
class MyPartitioner(num:Int) extends Partitioner{
  //分区的数量
  override def numPartitions: Int = num

  //根据key计算进入哪个分区
  override def getPartition(key: Any): Int = {
    key match {
      case null => 0
      case _ => 1
    }
  }
}