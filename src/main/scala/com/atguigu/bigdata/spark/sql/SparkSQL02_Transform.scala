package com.atguigu.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSQL02_Transform {
  def main(args: Array[String]): Unit = {

    //RDD,DataFrame,DataSet之间的转换

    //创建环境
    val sparkConf: SparkConf = new SparkConf().setAppName("transform").setMaster("local")
    val sparkSession: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    //在RDD转换为df,ds时，需要进行隐式转换，所以需要导入相应的功能
    // 下面的导入语句，即使不使用，也最好加上。★★★★
    import sparkSession.implicits._

    //创建RDD
    val rdd: RDD[(Int, String, Int)] = sparkSession.sparkContext.makeRDD(List((1, "zhangsan", 20), (2, "lisis", 25), (3, "wangwu", 30)))

    //向上转型
    // rdd=>df
//    val df: DataFrame = rdd.toDF("id", "name", "age")
//    df.show()

    //df=>ds
//    val ds: Dataset[User] = df.as[User]
//    ds.show()

    //rdd=>ds
    val mapRDD: RDD[User] = rdd.map {
      case (id, name, age) => {
        User(id, name, age)
      }
    }

    val ds: Dataset[User] = mapRDD.toDS()
//    ds.show()


    //给类起别名
    type str = String


    //向下转型
    //ds=>df
    val df: DataFrame = ds.toDF()
//    df.show()

    //df=>rdd
    val rdd1: RDD[Row] = df.rdd

    //ds=>rdd
    val rdd2: RDD[User] = ds.rdd


    //关闭资源
    sparkSession.close()
  }
}

//声明样例类
case class User(id: Int, name: String, age: Int)