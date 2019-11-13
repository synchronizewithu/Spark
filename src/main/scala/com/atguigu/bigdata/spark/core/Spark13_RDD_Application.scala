package com.atguigu.bigdata.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark13_RDD_Application {
  def main(args: Array[String]): Unit = {

    //创建RDD
    val sparkConf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(sparkConf)

    // 使用算子完成需求：统计出每一个省份广告被点击次数的TOP3
    // TODO 1.读取事先准备好的文件
    val lineRDD: RDD[String] = sc.textFile("input/agent.log")

    // TODO 2.分解数据，保留省份，广告
    val priAdstoClickRDD: RDD[(String, Int)] = lineRDD.map {
      line => {
        val strings: Array[String] = line.split(" ")
        (strings(1) + "_" + strings(4), 1)
      }
    }


    // TODO 3.将数据按照（省份 + 广告）的形式，进行统计点击次数
    val priAdstoSumClickRDD: RDD[(String, Int)] = priAdstoClickRDD.reduceByKey(_+_)


    // TODO 4.统计结果为（省份 + 广告，sumClick）
    // TODO 4.1 将数据转化为（省份，（广告，sumClick））
    val priToAdsSumClickRDD: RDD[(String, (String, Int))] = priAdstoSumClickRDD.map {
      case (priAds, sumClick) => {
        val keys: Array[String] = priAds.split("_")
        (keys(0), (keys(1), sumClick))
      }
    }

    // TODO 4.2 根据省份分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = priToAdsSumClickRDD.groupByKey()

    // TODO 5.将分组后的广告信息进行排序（降序），取前三
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues (
      datas => {
        datas.toList.sortWith {
          (left, right) => {
            left._2 > right._2
          }
        }.take(3)
      })

    resultRDD.collect().foreach(println)


  }
}
