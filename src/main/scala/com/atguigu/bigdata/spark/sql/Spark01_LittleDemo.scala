package com.atguigu.bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_LittleDemo {
  def main(args: Array[String]): Unit = {

    //第一个SparkSession的程序

    //创建SparkSession环境
    val sparkConf: SparkConf = new SparkConf().setAppName("SQL").setMaster("local")
    val sparkSession: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //创建DataFrame对象
    val df: DataFrame = sparkSession.read.json("input/user.json")
    //显示信息
//    df.show()

    //使用SQL的方式查询数据
    //创建视图对象
//    df.createOrReplaceTempView("user")
//    sparkSession.sql("select * from user").show()

    //使用DSL（对象）的方式查询数据
    df.select("name").show()


    //关闭资源
    sparkSession.close()

  }
}
