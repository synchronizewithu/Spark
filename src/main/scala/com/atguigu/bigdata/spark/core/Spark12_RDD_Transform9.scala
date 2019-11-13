package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark12_RDD_Transform9 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("MakeRDD").setMaster("local")
    val sc = new SparkContext(sparkConf)

    val kvRDD = sc.parallelize(Array((3,"aa"),(6,"cc"),(2,"bb"),(1,"dd")))

    //转换算子 -sortByKey
    //    val rdd: RDD[(Int, String)] = kvRDD.sortByKey(false)
    //    rdd.collect().foreach(println)

    //转换算子 - mapValues
//    val rdd: RDD[(Int, String)] = kvRDD.mapValues(str=>str+"|||||")
//    rdd.collect().foreach(println)

    // 转换算子 - join(相同key聚合,将value连接在一起：(key, (v1, v2))  )
    val kvRDD1 = sc.makeRDD(Array((1,"a"),(2,"b"),(3,"c")))
    val kvRDD2 = sc.makeRDD(Array((1,4),(2,5),(3,6),(4,7)))

    val joinRDD: RDD[(Int, (String, Int))] = kvRDD1.join(kvRDD2)
    joinRDD.collect().foreach(println)


    // 转换算子 - cogroup
    val rdd: RDD[(Int, (Iterable[String], Iterable[Int]))] = kvRDD1.cogroup(kvRDD2)
    rdd.collect().foreach(println)

  }
}
