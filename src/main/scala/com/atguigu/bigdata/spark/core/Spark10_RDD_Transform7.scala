package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark10_RDD_Transform7 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val kvRDD = sc.makeRDD(List(("a",3),("a",2),("c",4),("b",3),("c",6),("c",8)),2)

    /*
    // 转换算子 - aggregateByKey （使用key对数据进行聚合）
    // aggregateByKey的第二个参数列表，表示分区内计算，分区间计算
//    val aggregateByKeyRDD: RDD[(String, Int)] = kvRDD.aggregateByKey(0)(math.max(_,_),_+_)
    val aggregateByKeyRDD: RDD[(String, Int)] = kvRDD.aggregateByKey(0)(_+_,_+_)

    aggregateByKeyRDD.collect().foreach(println)
    */

    /*
    // 转换算子 - foldByKey（使用key对数据进行聚合）
    //aggregateByKey的简化操作，seqop和combop相同
    val rdd: RDD[(String, Int)] = kvRDD.foldByKey(0)(_+_)

    rdd.collect().foreach(println)
    */

    //转换算子 - combineByKey
    val combineByKeyRDD: RDD[(String, (Int, Int))] = kvRDD.combineByKey(
      (x) => (x, 1),
      (t: (Int, Int), v: Int) => (t._1 + v, t._2 + 1),
      (t1: (Int, Int), t2: (Int, Int)) => (t1._1 + t2._1, t1._2 + t2._2)
    )

    val result: RDD[(String, Int)] = combineByKeyRDD.map {
      case (key, (sum, count)) => {
        (key, sum / count)
      }
    }


    result.collect().foreach(println)

  }
}
