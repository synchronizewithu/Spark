package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark09_RDD_Transform6 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val kvRDD: RDD[(String, Int)] = sc.makeRDD(List(("a",1),("b",2),("b",3),("a",2)))



    //转换算子 - groupbykey (使用key对数据进行分组)
    val groupRDD: RDD[(String, Iterable[Int])] = kvRDD.groupByKey()

    val mapRDD: RDD[(String, Int)] = groupRDD.map {
      case (key, datas) => {
        (key, datas.sum)
      }
//      datas=>{
//        val value: Iterable[Int] = datas._2
//        val sum: Int = value.sum
//        (datas._1,sum)
//      }
    }

    mapRDD.collect().foreach(println)



    //转换算子 - reduceByKey （ 分组后聚合 ）
    val reduceByKeyRDD: RDD[(String, Int)] = kvRDD.reduceByKey(_+_)

    reduceByKeyRDD.collect().foreach(println)
  }
}
