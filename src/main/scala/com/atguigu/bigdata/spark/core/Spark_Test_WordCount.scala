package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.StringOps
import scala.collection.mutable

object Spark_Test_WordCount {
  def main(args: Array[String]): Unit = {
    //创建环境
    val sparkConf: SparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(sparkConf)

    //创建RDD
    val listRDD: RDD[(String, Int)] = sc.makeRDD(List(("Hello Spark", 4 ), ("Hello Scala", 5), ("Spark Scala", 4), ("Hello Scala", 6), ("Hello Spark", 5), ("Scala Spark", 6)))

    //扁平化操作,并根据次数进行map
    val wordToCountRDD: RDD[(String, Int)] = listRDD.flatMap {
      t => {}
        val line: StringOps = t._1
        val splitWord: mutable.ArrayOps[String] = line.split(" ")
        splitWord.map(word => (word, t._2))
    }


    //（Hello，4）(Hello,5)...
    val groupRDD: RDD[(String, Iterable[Int])] = wordToCountRDD.groupByKey()

//
//    //方法1：
//    //Hello:(4,5,6,5)
//    val wordCount: RDD[(String, Int)] = groupRDD.map {
//      case (word, datas) => {
//        (word, datas.sum)
//      }
//    }

    //方法2：
    val wordCount: RDD[(String, Int)] = groupRDD.mapValues {
//      case datas => {
//        datas.sum
//      }
      datas=>{
        datas.sum
      }
    }

    //方法3：
//    val wordCount: RDD[(String, Int)] = wordToCountRDD.reduceByKey(_+_)

    //方法4：
//    val wordCount: RDD[(String, Int)] = wordToCountRDD.foldByKey(0)(_+_)

    //方法5：
//    val wordCount: RDD[(String, Int)] = wordToCountRDD.aggregateByKey(0)(_+_,_+_)

    //方法6：
//    val wordCount: RDD[(String, Int)] = wordToCountRDD.combineByKey(
//      x => x,
//      (x1: Int, v: Int) => (x1 + v),
//      (v1: Int, v2: Int) => (v1 + v2)
//    )


    wordCount.collect().foreach(println)
  }
}
