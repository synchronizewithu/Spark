package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object Spark23_Test {
  def main(args: Array[String]): Unit = {

    //Spark提供了三大数据结构
    // 1.RDD 弹性分布式数据集
    // 2.广播变量：分布式共享只读变量
    // 3.累加器：分布式共享只写变量

    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    // 将当前创建Spark上下文环境对象的程序称之driver程序
    val sc = new SparkContext(sparkConf)

    val numRDD : RDD[Int] = sc.makeRDD(List(1,2,3,4))

    //Shuffle太慢
//    val i: Int = numRDD.reduce(_+_)
    //没有Shuffle
//    val sum: Int = numRDD.collect().sum

    /*
    var sum:Int = 0

    //数据操作有问题
    numRDD.foreach{
      num=>{
        sum = sum + num
      }
    }

    println(sum)
    */

    //声明累加器
    // 1.创建累加器
    // 2.将累加器注册到Spark环境中
    // 3.使用累加器对象
    val sum: LongAccumulator = sc.longAccumulator("sum")

    numRDD.foreach{
      num=>{
        sum.add(num)
      }
    }

    //获取累加器的变量
    println(sum.value)


    //关闭资源
    sc.stop()
  }
}
