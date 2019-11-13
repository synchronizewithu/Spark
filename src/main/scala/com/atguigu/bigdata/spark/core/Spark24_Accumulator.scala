package com.atguigu.bigdata.spark.core

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.AccumulatorV2

object Spark22_Accumulator {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    val sc = new SparkContext(sparkConf)

    val wordRDD: RDD[String] = sc.makeRDD(List("Hello","Hadoop","Hive","Zookeeper","Spark"))

    //创建累加器
    val accumulator = new BlackAccumulator()
    //向Spark中注册累加器
    sc.register(accumulator,"black")

    wordRDD.foreach{
      word=>{
        accumulator.add(word)
      }
    }

    //打印累加器的值
    println(accumulator.value)

    //关闭资源
    sc.stop()

  }
}

class BlackAccumulator extends AccumulatorV2[String,java.util.HashSet[String]]{

  private var blackHashSet = new util.HashSet[String]

  //判断是否初始化
  override def isZero: Boolean = {
    blackHashSet.isEmpty
  }

  //复制累加器
  override def copy(): AccumulatorV2[String, util.HashSet[String]] = {
    new BlackAccumulator()
  }

  //重置累加器
  override def reset(): Unit = {
    blackHashSet.clear()
  }

  //向累加器中添加数据
  override def add(v: String): Unit = {
    if (v.contains("h")){
      blackHashSet.add(v)
    }
  }

  //合并不同的executor中的数据
  override def merge(other: AccumulatorV2[String, util.HashSet[String]]): Unit = {
    blackHashSet.addAll(other.value)
  }

  //获取到集合中的数据
  override def value: util.HashSet[String] = {
    blackHashSet
  }
}