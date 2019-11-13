package com.atguigu.bigdata.spark.core

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark26_broadCast {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("broadCast").setMaster("local")
    val sc = new SparkContext(conf)

    val numRDD: RDD[(Int, Int)] = sc.makeRDD(List( (1,1),(2,2),(3,3),(4,4) ))
    val list = List((1,2),(2,3),(3,1))


    // 如果将集合直接传递给Executor执行，那么每一个任务都会传递一份，会造成内存数据冗余
    // 使用广播变量进行优化，可以在Executor只保留一份数据，这个Executor中执行的所有任务可以共享这份数据
    //声明广播变量
    val broadCast: Broadcast[List[(Int, Int)]] = sc.broadcast(list)

    //使用map，需要返回值，否则可以使用foreach
    val joinRDD: RDD[(Int, (Int, Any))] = numRDD.map {
      case (k1, v1) => {
        var v: Any = null
        for ((k2, v2) <- broadCast.value) {
          if (k1 == k2) {
            v = v2
          }
        }
        (k1, (v1, v))
      }
    }

    joinRDD.foreach(println)

    //关闭资源
    sc.stop()
  }
}
