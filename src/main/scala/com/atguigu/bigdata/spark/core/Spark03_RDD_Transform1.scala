package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Transform1 {
  def main(args: Array[String]): Unit = {

    //创建RDD
    //创建sparkContext上下文对象
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //构建RDD
    val numRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4))


    /*
    //RDD转换算子 --- flatMap ，扁平化操作
//    val flatMapRDD: RDD[Int] = numRDD.flatMap(x=>1 to x)
//    flatMapRDD.collect().foreach(println)


    val list: RDD[List[Int]] = sc.makeRDD(List(List(1,2),List(3,4)))

    val flatMapRDD: RDD[Int] = list.flatMap(x=>x)

    flatMapRDD.collect().foreach(println)

    */

    /*
    //RDD转换算子 --- glom ：将一个分区的数据形成一个数组
    val numRDD1: RDD[Int] = sc.makeRDD(1 to 16,4)

    val glomRDD: RDD[Array[Int]] = numRDD1.glom()

    val mapRDD: RDD[Int] = glomRDD.map( _.max)

    mapRDD.collect().foreach(println)

    */

    /*
    //RDD转换算子 --- groupby 分组，按照传入函数的返回值进行分组。将相同的key对应的值放入一个迭代器。
    val groupByRDD: RDD[(Int, Iterable[Int])] = numRDD.groupBy(x=>x%2)

    groupByRDD.collect().foreach(println)

    */

    //RDD转换算子 --- filter
//    var sourceFilter = sc.parallelize(Array("xiaoming","xiaojiang","xiaohe","dazhi"))
//    val filterRDD: RDD[String] = sourceFilter.filter(str=>str.contains("xiao"))
//    filterRDD.collect().foreach(println)

    val strRDD: RDD[String] = sc.makeRDD(List("1","2","3"))

     val filterRDD: RDD[String] = strRDD.filter(str=>str.contains("1"))

    filterRDD.collect().foreach(println)

  }
}
